package com.owenl.image.Model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetectedObject {

    String name;
    BigDecimal confidence;

    public DetectedObject (String name, BigDecimal confidence){
        this.name = name;
        this.confidence = confidence;
    }

}
