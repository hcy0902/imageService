package com.owenl.image.Model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetectedObject {

    String name;
    String confidence;

    public DetectedObject (String name, String confidence){
        this.name = name;
        this.confidence = confidence;
    }

}
