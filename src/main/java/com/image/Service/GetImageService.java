package com.image.Service;

import com.image.Model.ImageRequest;
import com.image.Model.ImageResponse;
import org.springframework.stereotype.Service;

@Service
public class GetImageService {

    public ImageResponse getImage(String imageId){

        ImageResponse response = new ImageResponse();

        return response;
    }

    public ImageResponse getAllImages() {
        ImageResponse response = new ImageResponse();

        return response;
    }
}
