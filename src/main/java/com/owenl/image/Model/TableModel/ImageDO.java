package com.owenl.image.Model.TableModel;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity(name = "image")
public class ImageDO {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "label")
    private String label;
    @Column(name = "url")
    private String url;

    public ImageDO() {}

    public ImageDO (String label, String url){
        this.label = label;
        this.url = url;
    }

    public ImageDO (int id, String label, String url){
        this.id = id;
        this.label = label;
        this.url = url;
    }


}
