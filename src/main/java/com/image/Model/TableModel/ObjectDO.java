package com.image.Model.TableModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ObjectDO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int imageId;

    String confidence;

    String name;

    public ObjectDO (String confidence,String name){
        this.confidence = confidence;
        this.name = name;
    }

}
