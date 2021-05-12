package com.example.radarheatmapinference;

import java.util.ArrayList;
import java.util.List;

public class Heatmap {

    private String filename;
    private int imageId;

    public Heatmap(String filename, int imageId){
        this.filename = filename;
        this.imageId = imageId;
    }

    public static List<Heatmap> getAllHeatmaps(){
        List<Heatmap> heatmaps = new ArrayList<Heatmap>();
        //heatmaps.add(new Heatmap("heatmap00000.png",R.drawable.heatmap00000));
        //heatmaps.add(new Heatmap("heatmap00010.png",R.drawable.heatmap00010));
        //heatmaps.add(new Heatmap("heatmap00110.png",R.drawable.heatmap00110));
        //heatmaps.add(new Heatmap("heatmap01000.png",R.drawable.heatmap01000));
        //heatmaps.add(new Heatmap("heatmap01010.png",R.drawable.heatmap01010));
        //heatmaps.add(new Heatmap("heatmap01110.png",R.drawable.heatmap01110));
        //heatmaps.add(new Heatmap("heatmap01111.png",R.drawable.heatmap01111));
        //heatmaps.add(new Heatmap("heatmap10000.png",R.drawable.heatmap10000));
        //heatmaps.add(new Heatmap("heatmap11000.png",R.drawable.heatmap11000));
        //heatmaps.add(new Heatmap("heatmap11010.png",R.drawable.heatmap11010));
        //heatmaps.add(new Heatmap("heatmap11011.png",R.drawable.heatmap11011));
        //heatmaps.add(new Heatmap("heatmap11111.png",R.drawable.heatmap11111));
        return heatmaps;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
