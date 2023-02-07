package com.image.Utils;

import com.image.Model.ImageResponse;
import lombok.Data;

@Data
public class ImageException extends Throwable {

    Status status;
    String message;

}
