package com.image.Service;

import com.image.Model.ImageRequest;
import com.image.Model.ImageResponse;
import com.image.Utils.ImageException;
import org.springframework.stereotype.Service;

@Service
public class AddImageService {

    public ImageResponse addImage(ImageRequest request) throws ImageException {

        ImageResponse response = new ImageResponse();

        return response;
    }
}
