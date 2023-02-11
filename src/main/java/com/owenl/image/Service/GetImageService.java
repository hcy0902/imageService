package com.owenl.image.Service;

import com.owenl.image.Model.*;
import com.owenl.image.Model.TableModel.ImageDO;
import com.owenl.image.Model.TableModel.ImageObjDO;
import com.owenl.image.Repository.ImageRepository;
import com.owenl.image.Repository.ObjectRepository;
import com.owenl.image.Utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetImageService {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ObjectRepository objectRepository;

    private static final String RETRIEVE_DATA_ERROR_MESSAGE = "Error retrieving from DB for: ";
    private static final String SUCCESS_MESSAGE = "Retrieve images successful";
    private static final String NOT_FOUND_FOR_OBJECTS = "Not able to find images related to objects";
    private static final String NOT_FOUND_FOR_ID = "Not able to find Image with ID : ";
    private static final String NO_IMAGES_MESSAGE = "No images in DB";
    private static final String FAILURE_RETRIEVE_IMAGES = "Failed to retrieve from DB";
    public ImageResponse getImage(int imageId){

        ImageResponse response = new ImageResponse();
        response.setErrorDetails(new ArrayList<>());

        List<ImageObjDO> imageObjDOs = new ArrayList<>();

        try {
            imageObjDOs = imageRepository.fetchImageById(imageId);
        } catch (Exception ex){
            return handleException(new ApplicationException(ex, Status.INTERNAL_SERVER_ERROR, RETRIEVE_DATA_ERROR_MESSAGE +
                    "ImageObjDO"));
        }

        Image image;

        if (!imageObjDOs.isEmpty()){
            image = new Image(imageId, imageObjDOs.get(0).getImageLabel(), imageObjDOs.get(0).getUrl());
            List<DetectedObject> objects = new ArrayList();
            for (ImageObjDO imageObjDO : imageObjDOs) {
                if (Objects.isNull(imageObjDO.getName())){
                    continue;
                }
                objects.add(new DetectedObject(imageObjDO.getName(), imageObjDO.getConfidence()));
            }

            image.setObjects(objects);

            response.setStatus(Status.Ok);
            response.setMessage(SUCCESS_MESSAGE);
            response.setImages(Arrays.asList(image));

            return response;
        }

        response.setMessage(NOT_FOUND_FOR_ID + imageId);
        response.setStatus(Status.Ok);

        return response;
    }

    public ImageResponse getAllImages() {
        ImageResponse response = new ImageResponse();
        response.setErrorDetails(new ArrayList<>());

        List<ImageObjDO> imageObjDOs = new ArrayList();

        //Retrieve all Images and objects info
        try {
            imageObjDOs = imageRepository.fetchImageObjectLeftOuterJoin();
        } catch (Exception ex) {
            return handleException(new ApplicationException(ex, Status.INTERNAL_SERVER_ERROR, RETRIEVE_DATA_ERROR_MESSAGE +
                    "ImageObjectDO"));
        }

        if (imageObjDOs.isEmpty()){
            response.setStatus(Status.Ok);
            response.setMessage(NO_IMAGES_MESSAGE);
            return response;
        }

        //Key as imageId
        Map<Integer, List<DetectedObject>> imageToObjectsMap = new HashMap<>();
        List<Image> images = new ArrayList();

        mapImageObjDO(imageObjDOs, images, imageToObjectsMap);

        response.setImages(images);
        response.setStatus(Status.Ok);
        response.setMessage(SUCCESS_MESSAGE);

        return response;
    }

    public ImageResponse getImageByObject(List<String> objects) {

        ImageResponse response = new ImageResponse();
        response.setErrorDetails(new ArrayList<>());

        List<ImageObjDO> imageObjDOs = new ArrayList<>();
        try {
            imageObjDOs = imageRepository.fetchImagesByObject(objects);
        } catch (Exception ex){
            return handleException(new ApplicationException(ex, Status.INTERNAL_SERVER_ERROR, RETRIEVE_DATA_ERROR_MESSAGE +
                    "ImageDO"));
        }

        //if not found any images related to the objects, simply return
        if (imageObjDOs.isEmpty()){
            response.setStatus(Status.Ok);
            response.setMessage(NOT_FOUND_FOR_OBJECTS);
            return response;
        }

        //Map objects to images
        Map<Integer, List<DetectedObject>> imageToObjectsMap = new HashMap<>();
        List<Image> images = new ArrayList();
        mapImageObjDO(imageObjDOs, images, imageToObjectsMap);

        response.setImages(images);
        response.setStatus(Status.Ok);
        response.setMessage(SUCCESS_MESSAGE);
        return response;

    }

    private List<Image> mapDOToImage(List<ImageDO> imageDOs) {

        List<Image> images = new ArrayList<>();

        for (ImageDO imageDO : imageDOs){
            images.add(new Image(imageDO.getId(), imageDO.getLabel(), imageDO.getUrl()));
        }

        return images;

    }

    private void mapImageObjDO(List<ImageObjDO> imageObjDOs, List<Image> images,
                               Map<Integer, List<DetectedObject>> imageToObjectsMap) {

        List<ImageDO> imageDOs = new ArrayList<>();

        for (ImageObjDO imageObjDO : imageObjDOs){
            if (!imageToObjectsMap.containsKey(imageObjDO.getImageId())){
                imageToObjectsMap.put(imageObjDO.getImageId(), new ArrayList<>());
                imageDOs.add(new ImageDO(imageObjDO.getImageId(), imageObjDO.getImageLabel(), imageObjDO.getUrl()));
            }
            if (!Objects.isNull(imageObjDO.getName()) && !imageObjDO.getName().isEmpty()){
                imageToObjectsMap.get(imageObjDO.getImageId())
                        .add(new DetectedObject(imageObjDO.getName(), imageObjDO.getConfidence()));
            }
        }

        for (ImageDO imageDO : imageDOs){
            Image curImage = new Image(imageDO.getId(), imageDO.getLabel(), imageDO.getUrl());
            List<DetectedObject> cur = imageToObjectsMap.get(imageDO.getId());
            if (!cur.isEmpty()){
                curImage.setObjects(cur);
            }
            images.add(curImage);
        }
    }

    private ImageResponse handleException(ApplicationException exception) {

        ImageResponse response = new ImageResponse();
        response.setStatus(exception.getStatus());
        response.setErrorDetails(List.of(exception.getMessage()));
        response.setMessage(FAILURE_RETRIEVE_IMAGES);

        return response;

    }
}
