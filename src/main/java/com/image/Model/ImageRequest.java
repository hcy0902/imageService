package com.image.Model;

import jakarta.validation.constraints.NotNull;

public class ImageRequest {

    Boolean detectObject;

    @NotNull
    String imageUrl;

    String imageLabel;


}
