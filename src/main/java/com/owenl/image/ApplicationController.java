package com.owenl.image;

import com.owenl.image.Model.ImageRequest;
import com.owenl.image.Model.ImageResponse;
import com.owenl.image.Model.Response;
import com.owenl.image.Service.AddImageService;
import com.owenl.image.Service.GetImageService;
import com.owenl.image.Utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/images")
public class ApplicationController {

    @Autowired
    AddImageService addImageService;
    @Autowired
    GetImageService getImageService;

    private static final String BAD_REQUEST_MESSAGE = "Bad Request, missing required field: ";

    @PostMapping
    public ResponseEntity<Response> addImage (@RequestBody ImageRequest request){

        Response response = new Response();

        if (!validateRequest(request)){
            response.setStatus(Status.BAD_REQUEST);
            return handleResponse(response);
        }

        response = addImageService.addImage(request);

        return handleResponse(response);
    }


    @GetMapping
    public ResponseEntity<Response> getAllImages(){

        ImageResponse response = getImageService.getAllImages();

        return handleResponse(response);
    }

    @GetMapping("/?objects=")
    public ResponseEntity<Response> getAllImagesWithObject(@RequestParam List<String> objects){
        ImageResponse response = new ImageResponse();
        return handleResponse(response);
    }

//    @GetMapping("/{imageId}")
//    public ResponseEntity<Response> getImageById(@PathVariable("imageId") String imageId){
//        ImageResponse response = getImageService.getImage(imageId);
//        return handleResponse(response);
//    }

    @GetMapping("/test")
    public List<String> blogCreation (){

        return Arrays.asList("test", "success");
    }

    private boolean validateRequest(ImageRequest request) {
        if (request.getImageUrl().isEmpty()){
            return false;
        }

        return true;
    }

    private ResponseEntity<Response> handleResponse(Response response) {
        if (Status.NOT_FOUND.equals(response.getStatus())){
            return ResponseEntity.notFound().build();
        }else if (Status.BAD_REQUEST.equals(response.getStatus())){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }


}
