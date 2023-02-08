package com.image.Model;

import com.image.Model.ImageResponse;
import com.image.Model.Response;
import com.image.Utils.Status;
import lombok.Data;

@Data
public class ImageException extends Exception {
    String message;

    public ImageException (String message){
        this.message = message;
    }

}
