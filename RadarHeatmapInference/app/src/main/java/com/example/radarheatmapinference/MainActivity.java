package com.example.radarheatmapinference;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    ImageView iv01;

    Bitmap bitmap;

    TextView tv01;

    ImageView iv02;

    Switch sw01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        try {
            mClassifier = new Classifier(this,false);
        }catch (IOException e){
            e.printStackTrace();
        }

        HeatmapAdapter heatmapAdapter = new HeatmapAdapter(this,R.layout.heatmap_item,Heatmap.getAllHeatmaps());
        ListView lv01 = (ListView) findViewById(R.id.listview);
        lv01.setAdapter(heatmapAdapter);

        iv02= findViewById(R.id.imageView2);
        iv02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = mClassifier.classify(bitmap);
                long costTime = mClassifier.getCostTime();
                String resShow = res + " cost time : " + costTime +" ms";
                Toast.makeText(MainActivity.this, resShow,Toast.LENGTH_SHORT).show();
            }
        });

        sw01 = findViewById(R.id.switch1);
        sw01.setChecked(false);
        sw01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               recreateClassifier(isChecked);
            }
        });
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
                    bitmap = ((BitmapDrawable)((ImageView) iv02).getDrawable()).getBitmap();
                }
            });
            return oneHeatmapView;
        }
    }
}