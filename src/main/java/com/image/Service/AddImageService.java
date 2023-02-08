package com.image.Service;

import com.image.Bridge.ImaggaClient;
import com.image.Model.*;
import com.image.Model.TableModel.ImageDO;
import com.image.Model.TableModel.ObjectDO;
import com.image.Repository.ImageRepository;
import com.image.Repository.ObjectRepository;
import com.image.Utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class AddImageService {

    @Autowired
    ImaggaClient imaggaClient;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ObjectRepository objectRepository;

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public Response addImage(ImageRequest request) {

        ImageResponse response = new ImageResponse();

        Image image = new Image();
        List<DetectedObject> objectList = new ArrayList<>();
        String label = "";

        //label generation
        if (Objects.isNull(request.getImageLabel())){
            label = generateLabel();
        }else{
            label = request.getImageLabel();
        }

        //call to imagga client
        if (request.isDetectObject()){
            try{
                objectList = imaggaClient.detectObject(request.getImageUrl());
                image.setObjects(objectList);
            } catch (Exception ex){
                return handleException(ex);
            }
        }

        //DB operation
        ImageDO imageDO = new ImageDO(label, request.getImageUrl());
        List<ObjectDO> objectDOS = new ArrayList<>();
        if (!objectList.isEmpty()){
            for (DetectedObject object : objectList){
                objectDOS.add(new ObjectDO(object.getConfidence().toString(), object.getName()));
            }
        }

        try{
            imageDO = imageRepository.save(imageDO);
            if (!objectDOS.isEmpty()){
                for (ObjectDO objectDO : objectDOS){
                    objectRepository.save(objectDO);
                }
            }
        } catch (Exception ex){
            return handleException(ex);
        }


        image.setImageUrl(imageDO.getUrl());
        image.setImageLabel(imageDO.getLabel());
        image.setId(imageDO.getId());

        response.setStatus(Status.Ok);
        response.setImages(List.of(image));

        return response;
    }

    private Response handleException(Exception exception) {

        Response response = new Response();
        response.setException(exception);
        response.setStatus(Status.INTERNAL_SERVER_ERROR);
        response.setMessage(exception.getMessage());

        return response;

    }

    private String generateLabel() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf1.format(timestamp);
    }
}
