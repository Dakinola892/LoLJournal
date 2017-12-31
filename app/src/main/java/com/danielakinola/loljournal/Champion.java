package com.danielakinola.loljournal;

/**
 * Created by Guest on 25/12/2017.
 */

public class Champion {

    private String name;
    private int imgResource;

    public Champion(String name, int imgResource) {
        this.name = name;
        this.imgResource = imgResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }
}

