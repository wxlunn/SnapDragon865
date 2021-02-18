package com.example.radarheatmapinference;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.SystemClock;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Map;

public class Classifier {

    private static final String LOG_TAG = Classifier.class.getSimpleName();

    private static final String MODEL_NAME = "cnn.tflite";

    private static final String LABEL_NAME = "label.txt";

    private MappedByteBuffer tfliteModel;

    private GpuDelegate gpuDelegate = null;

    private final Interpreter.Options options = new Interpreter.Options();

    private Interpreter tflite;

    private List<String> labels;

    private TensorImage inputImageBuffer;

    private TensorBuffer outputProbabilityBuffer;

    private TensorProcessor probabilityProcessor;

    private static final float PROBABILITY_MAEN = 0.0f;
    private static final float PROBABILITY_STD = 1.0f;

    private static final float IMAGE_MEAN = 0f;
    private static final float IMAGE_STD = 255f;
    private final int imageSizeBatch;
    private final int imageSizeX;
    private final int imageSizeY;
    private final int imageSizeChannel;
    private final int outputSizeX;
    private final int outputSizeY;
    int[] imageshape;
    int[] probabilityShape;
    long costTimeForInference;
    DataType imageDataType;
    DataType probabilityDataType;

    public Classifier(Activity activity,boolean gpu) throws IOException{

        tfliteModel = loadModelFile(activity);
        labels = loadLabels(activity);
        if(gpu){
            gpuDelegate = new GpuDelegate();
            options.addDelegate(gpuDelegate);
        }
        options.setNumThreads(1);
        tflite = new Interpreter(tfliteModel, options);

        int imageTensorIndex = 0;
        imageshape = tflite.getInputTensor(imageTensorIndex).shape();
        imageSizeBatch = imageshape[0];
        imageSizeY = imageshape[1];
        imageSizeX = imageshape[2];
        imageSizeChannel = imageshape[3];
        imageshape[0] = 1;
        //DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();
        imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();
        //tflite.resizeInput(imageTensorIndex, new int[]{1, 256, 86, 3});
        tflite.resizeInput(imageTensorIndex, imageshape);

        int probabilityTensorIndex = 0;
        probabilityShape = tflite.getInputTensor(probabilityTensorIndex).shape();
        outputSizeX = probabilityShape[0];
        outputSizeY = probabilityShape[1];
        //probabilityShape[0] = 1;
        probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

        inputImageBuffer = new TensorImage(imageDataType);
        outputProbabilityBuffer = TensorBuffer.createFixedSize(new int[]{1, 32}, probabilityDataType);
        //outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
        probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();
    }

    public String classify(Bitmap bitmap){
        costTimeForInference = 0;
        inputImageBuffer.load(bitmap);
        //
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                .add(getPreprocessNormalizeOp())
                .build();
        inputImageBuffer = imageProcessor.process(inputImageBuffer);
        //
        long startTimeForInference = SystemClock.uptimeMillis();
        tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());
        costTimeForInference = SystemClock.uptimeMillis() - startTimeForInference;
        Map<String, Float> labeledProbability = new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer)).getMapWithFloatValue();
        return MaxProb(labeledProbability);
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException{
        return FileUtil.loadMappedFile(activity, MODEL_NAME);
    }

    private List<String> loadLabels(Activity activity) throws IOException{
        return FileUtil.loadLabels(activity, getLabelPath());
    }

    private static String MaxProb(Map<String, Float> labelProb){
        float maxProb = 0;
        String ClassifiedResult = "";
        for (Map.Entry<String, Float> entry : labelProb.entrySet()){
            if(entry.getValue() > maxProb){
                maxProb = entry.getValue();
                ClassifiedResult = entry.getKey() + "  Prob: " + entry.getValue() + " ";
            }
        }
        return ClassifiedResult;
    }
    ///////
    protected TensorOperator getPostprocessNormalizeOp(){
        return new NormalizeOp(PROBABILITY_MAEN, PROBABILITY_STD);
    }

    protected TensorOperator getPreprocessNormalizeOp(){
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    public void close(){
        if(tflite != null){
            tflite.close();
            tflite = null;
        }
        if(gpuDelegate != null){
            gpuDelegate.close();
            gpuDelegate = null;
        }
        tfliteModel = null;
    }

    protected String getLabelPath(){
        return LABEL_NAME;
    }

    public int getImageSizeBatch(){return imageSizeBatch;}
    public int getImageSizeChannel(){return imageSizeChannel;};
    public int getImageSizeX(){return imageSizeX;};
    public int getImageSizeY(){return imageSizeY;};
    public int getOutputSizeX(){return outputSizeX;};
    public int getOutputSizeY(){return outputSizeY;};
    public String getshape(){
        String size = "";
        for(int i = 0;i<probabilityShape.length;i++){
            size = size + probabilityShape[i] + " ";
        }
        return size;
    }
    public long getCostTime(){
        return costTimeForInference;
    }

    public String getDtype(){return imageDataType.name() + probabilityDataType.name();}
}
