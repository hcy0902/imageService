package com.owenl.image.Model.TableModel;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "object")
@Data
public class ObjectDO {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "image_id")
    private int image_Id;

    @Column(name = "confidence")
    String confidence;

    @Column(name = "name")
    String name;

    public ObjectDO (String confidence,String name, int image_Id){
        this.confidence = confidence;
        this.name = name;
        this.image_Id = image_Id;
    }

}
