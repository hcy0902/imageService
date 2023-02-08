package com.owenl.image.Model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ImageRequest {

    boolean detectObject;

    @NotNull
    String imageUrl;

    String imageLabel;


}
