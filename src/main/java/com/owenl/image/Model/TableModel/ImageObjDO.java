package com.owenl.image.Model.TableModel;

import lombok.Data;

@Data
public class ImageObjDO {

    //image attributes
    int imageId;
    String imageLabel;
    String url;

    //object attributes
    Integer objectId;
    String confidence;
    String name;

    public ImageObjDO (int imageId, String imageLabel, String url, Integer objectId, String confidence, String name) {
        this.imageId = imageId;
        this.imageLabel = imageLabel;
        this.url = url;
        this.objectId = objectId;
        this.confidence = confidence;
        this.name = name;
    }

}
