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
        heatmaps.add(new Heatmap("test000000.png",R.drawable.test000000));
        heatmaps.add(new Heatmap("test000100.png",R.drawable.test000100));
        heatmaps.add(new Heatmap("test001100.png",R.drawable.test001100));
        heatmaps.add(new Heatmap("test010000.png",R.drawable.test010000));
        heatmaps.add(new Heatmap("test010101.png",R.drawable.test010101));
        heatmaps.add(new Heatmap("test011100.png",R.drawable.test011100));
        heatmaps.add(new Heatmap("test011111.png",R.drawable.test011111));
        heatmaps.add(new Heatmap("test100000.png",R.drawable.test100000));
        heatmaps.add(new Heatmap("test110000.png",R.drawable.test110000));
        heatmaps.add(new Heatmap("test110001.png",R.drawable.test110001));
        heatmaps.add(new Heatmap("test110101.png",R.drawable.test110101));
        heatmaps.add(new Heatmap("test110111.png",R.drawable.test110111));
        heatmaps.add(new Heatmap("test111111.png",R.drawable.test111111));
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
