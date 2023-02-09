package com.owenl.image.Model;

import lombok.Data;

import java.util.List;

@Data
public class Image {

    List<DetectedObject> objects;
    String imageUrl;
    String imageLabel;
    int id;

    public Image () {}

    public Image (int id, String imageLabel, String imageUrl) {
        this.id = id;
        this.imageLabel = imageLabel;
        this.imageUrl = imageUrl;
    }

}
