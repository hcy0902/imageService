package com.image;

import com.image.Model.ImageRequest;
import com.image.Model.ImageResponse;
import com.image.Service.AddImageService;
import com.image.Service.GetImageService;
import com.image.Utils.ImageException;
import com.image.Utils.Status;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@Validated
@RequestMapping("images")
public class ApplicationController {

    @Autowired
    AddImageService addImageService;
    @Autowired
    GetImageService getImageService;

    @PostMapping
    public ResponseEntity<ImageResponse> addImage (@Valid @RequestBody ImageRequest imageRequest){

        ImageResponse imageResponse = new ImageResponse();
        try {
            imageResponse = addImageService.addImage(imageRequest);
        } catch (ImageException ex){
            return handleException(ex);
        }

        return ResponseEntity.ok(imageResponse);
    }


    @GetMapping
    public ResponseEntity<ImageResponse> getAllImages(){
        ImageResponse imageResponse = getImageService.getAllImages();
        if (imageResponse.getStatus().equals(Status.NOT_FOUND)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageResponse);
    }

    @GetMapping("/?objects=")
    public ResponseEntity<ImageResponse> getAllImagesWithObject(){
        ImageResponse imageResponse = new ImageResponse();
        return ResponseEntity.ok(imageResponse);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ImageResponse> getImageById(@PathVariable("imageId") String imageId){
        ImageResponse imageResponse = getImageService.getImage(imageId);
        return ResponseEntity.ok(imageResponse);
    }

    @GetMapping("/test")
    public List<String> blogCreation (){

        return Arrays.asList("test", "success");
    }

    private ResponseEntity<ImageResponse> handleException(ImageException ex) {
        if (Status.NOT_FOUND.equals(ex.getStatus())){
            return ResponseEntity.notFound().build();
        }else if (Status.BAD_REQUEST.equals(ex.getStatus())){
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }


}
