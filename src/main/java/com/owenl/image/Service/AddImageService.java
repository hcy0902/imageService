package com.owenl.image.Service;

import com.owenl.image.Bridge.ImaggaClient;
import com.owenl.image.Model.*;
import com.owenl.image.Model.TableModel.ImageDO;
import com.owenl.image.Model.TableModel.ObjectDO;
import com.owenl.image.Repository.ImageRepository;
import com.owenl.image.Repository.ObjectRepository;
import com.owenl.image.Utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private static final String DB_INSERTION_ERROR_MESSAGE = "Error inserting into DB for: ";
    private static final String IMAGE_INSERT_AND_DETECT_OBJECT_SUCCESS = "Successfully inserted image and detected object";
    private static final String INSERT_IMAGE_SUCCESS = "Successfully inserted image";
    private ApplicationException exception;

    public Response addImage(ImageRequest request) {

        ImageResponse response = new ImageResponse();

        Image image = new Image();
        List<DetectedObject> objectList = new ArrayList<>();
        String label = "";

        //call to imagga client
        // if we get error detecting object, do we still continue to save image into DB?
        if (request.isDetectObject()) {
            try {
                objectList = imaggaClient.detectObject(request.getImageUrl());
                image.setObjects(objectList);
            } catch (ApplicationException ex) {
                return handleException(ex);
            }
        }

        //label generation
        //if it is detecting objects, append first Object from the list to image's label
        if (Objects.isNull(request.getImageLabel()) || request.getImageLabel().isEmpty()) {
            label = generateLabel(request.isDetectObject(), objectList);
        } else {
            label = request.getImageLabel();
        }

        //DB operation
        ImageDO imageDO = new ImageDO(label, request.getImageUrl());

        try {
            imageDO = imageRepository.save(imageDO);

        } catch (Exception ex) {
            exception = new ApplicationException(ex, Status.INTERNAL_SERVER_ERROR, DB_INSERTION_ERROR_MESSAGE +
                    "Image");
            return handleException(exception);
        }

        List<ObjectDO> objectDOS = new ArrayList<>();
        if (!objectList.isEmpty()) {
            for (DetectedObject object : objectList) {
                objectDOS.add(new ObjectDO(object.getConfidence(), object.getName(), imageDO.getId()));
            }
        }

        if (!objectDOS.isEmpty()) {

            try {
                for (ObjectDO objectDO : objectDOS) {
                    objectRepository.save(objectDO);
                }

            } catch (Exception ex) {
                exception = new ApplicationException(ex, Status.INTERNAL_SERVER_ERROR, DB_INSERTION_ERROR_MESSAGE +
                        "Objects");
                return handleException(exception);
            }

        }


        image.setImageUrl(imageDO.getUrl());
        image.setImageLabel(imageDO.getLabel());
        image.setId(imageDO.getId());

        response.setStatus(Status.Ok);
        response.setImages(List.of(image));

        if (request.isDetectObject()){
            response.setMessage(IMAGE_INSERT_AND_DETECT_OBJECT_SUCCESS);
        }else {
            response.setMessage(INSERT_IMAGE_SUCCESS);
        }


        if (Objects.isNull(exception)){
            return response;
        }


        return handleException(exception);
    }

    private Response handleException(ApplicationException exception) {

        Response response = new Response();
        response.setStatus(exception.getStatus());
        response.setMessage(exception.getMessage());

        return response;

    }

    private String generateLabel(boolean detectObject, List<DetectedObject> objectList) {
        if (!detectObject){
            return sdf1.format(new Timestamp(System.currentTimeMillis()));
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();

        sb.append(objectList.get(0).getName());
        sb.append(" at time: ");
        sb.append(sdf1.format(timestamp));


        return sb.toString();
    }
}
