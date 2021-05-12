package com.example.radarheatmapinference;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Classifier mClassifier;
    Bitmap bitmap;
    TextView tv02, tv03;
    ImageView iv03, iv04, iv05, iv06, iv07, iv02, iv08, iv09;
    //Button btn;
    //Switch sw01;

    Thread thread;
    //int[] o = {R.drawable.offroad_00000, R.drawable.offroad_00011, R.drawable.offroad_00100, R.drawable.offroad_00110, R.drawable.offroad_01001, R.drawable.offroad_01010,R.drawable.offroad_10001, R.drawable.offroad_10111, R.drawable.offroad_11011, R.drawable.offroad_11111};
    //int[] b = {R.drawable.basement_00000, R.drawable.basement_00010, R.drawable.basement_00100, R.drawable.basement_01010, R.drawable.basement_11000, R.drawable.basement_11010, R.drawable.basement_11011, R.drawable.basement_11111};
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        classifierInit();
        ViewInit();


        //int[] a  = {R.drawable.heatmap00000, R.drawable.heatmap00010, R.drawable.heatmap00110, R.drawable.heatmap01000, R.drawable.heatmap01010, R.drawable.heatmap01110, R.drawable.heatmap01111, R.drawable.heatmap10000, R.drawable.heatmap11000, R.drawable.heatmap11010, R.drawable.heatmap11011};
         final String[] labelCase = {"00000", "00011", "00100", "00110", "01001", "01010", "10001", "11011", "11111"};
         i = 0;
         thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(2500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                String offroad_gd_fileName = "c" + labelCase[i];
                                String offroad_fileName = "h" + labelCase[i];
                                iv09.setImageResource(getResourceId(MainActivity.this, offroad_gd_fileName));
                                tv03.setText(labelCase[i]);
                                int r = getResourceId(MainActivity.this, offroad_fileName);
                                bitmapInit(r);
                                Inference();
                                i += 1;
                                if (i >= 9){
                                    i = 0;
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();
        //String label = "11111";
        //String offroad_gd_fileName = "c" + label;
        //String offroad_fileName = "h" + label;
        //iv09.setImageResource(getResourceId(MainActivity.this, offroad_gd_fileName));
        //tv03.setText(label);
        //int r = getResourceId(MainActivity.this, offroad_fileName);
        //bitmapInit(r);
        //Inference();

    }

    public static int getResourceId(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    private void bitmapInit(int r){
        Drawable myDrawable = getResources().getDrawable(r);
        bitmap = ((BitmapDrawable) myDrawable).getBitmap();
    }

    private void Inference(){
        //Drawable myDrawable = getResources().getDrawable(R.drawable.heatmap11000);
        //bitmap = ((BitmapDrawable) myDrawable).getBitmap();
        String res = mClassifier.classify(bitmap);
        //Toast.makeText(this, res, Toast.LENGTH_LONG).show();
        ParkingStatus(res);
    }

    private void classifierInit(){
        try {
            mClassifier = new Classifier(this,false);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void ViewInit(){

        tv02=findViewById(R.id.textView9);
        tv03=findViewById(R.id.textView11);
        iv03= findViewById(R.id.imageView3);
        iv04= findViewById(R.id.imageView4);
        iv05= findViewById(R.id.imageView5);
        iv06= findViewById(R.id.imageView6);
        iv07= findViewById(R.id.imageView7);
        iv02=findViewById(R.id.imageView2);
        iv08=findViewById(R.id.imageView8);
        iv02.setBackgroundColor(Color.RED);
        iv08.setBackgroundColor(Color.GREEN);
        iv09=findViewById(R.id.imageView9);
    }
    /***
    private void InferenceListenerInit(){
        iv02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bitmap bitmap = BitmapFactory.decodeResource();
                String res = mClassifier.classify(bitmap);
                long costTime = mClassifier.getCostTime();
                //String resShow = " cost time : " + costTime +" ms";
                //Toast.makeText(MainActivity.this, resShow,Toast.LENGTH_SHORT).show();
                ParkingStatus(res);
            }
        });
    }***/
    /***
    private void gpuListenerInit(){
        sw01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recreateClassifier(isChecked);
            }
        });
    }***/

    private void ParkingStatus(String res){
        if(res.charAt(0) == '1'){
            iv03.setBackgroundColor(Color.RED);
        }else {
            iv03.setBackgroundColor(Color.GREEN);
        }
        if(res.charAt(1) == '1'){
            iv04.setBackgroundColor(Color.RED);
        }else {
            iv04.setBackgroundColor(Color.GREEN);
        }
        if(res.charAt(2) == '1'){
            iv05.setBackgroundColor(Color.RED);
        }else {
            iv05.setBackgroundColor(Color.GREEN);
        }
        if(res.charAt(3) == '1'){
            iv06.setBackgroundColor(Color.RED);
        }else {
            iv06.setBackgroundColor(Color.GREEN);
        }
        if(res.charAt(4) == '1'){
            iv07.setBackgroundColor(Color.RED);
        }else {
            iv07.setBackgroundColor(Color.GREEN);
        }
    }

    private void recreateClassifier(boolean gpu){
        if(mClassifier != null){
            mClassifier.close();
            mClassifier = null;
        }
        try {
            mClassifier = new Classifier(this,gpu);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy(){
        mClassifier.close();
        super.onDestroy();
    }
    /***
    public class HeatmapAdapter extends ArrayAdapter<Heatmap> {
        public HeatmapAdapter(Context context, int resource, List<Heatmap> objects){
            super(context, resource, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final Heatmap heatmap = getItem(position);

            View oneHeatmapView = LayoutInflater.from(getContext()).inflate(R.layout.heatmap_item, parent, false);

            TextView tv02 = (TextView) oneHeatmapView.findViewById(R.id.textView2);
            tv02.setText(heatmap.getFilename());
            oneHeatmapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv02.setImageResource(heatmap.getImageId());
                    tv01.setText(heatmap.getFilename().substring(7,12));
                    bitmap = ((BitmapDrawable)((ImageView) iv02).getDrawable()).getBitmap();
                }
            });
            return oneHeatmapView;
        }
    }***/
}