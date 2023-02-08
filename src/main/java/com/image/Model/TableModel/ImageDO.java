package com.image.Model.TableModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ImageDO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String label;
    private String url;

    public ImageDO() {}

    public ImageDO (String label, String url){
        this.label = label;
        this.url = url;
    }


}
