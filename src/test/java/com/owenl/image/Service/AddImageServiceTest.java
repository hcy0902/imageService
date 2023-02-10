package com.owenl.image.Service;


import com.owenl.image.Bridge.ImaggaClient;
import com.owenl.image.Model.*;
import com.owenl.image.Model.TableModel.ImageDO;
import com.owenl.image.Model.TableModel.ObjectDO;
import com.owenl.image.Repository.ImageRepository;
import com.owenl.image.Repository.ObjectRepository;
import com.owenl.image.Utils.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddImageServiceTest {

    @Mock
    ImaggaClient imaggaClient;

    @Mock
    ImageRepository imageRepository;

    @Mock
    ObjectRepository objectRepository;

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    @InjectMocks
    private AddImageService addImageService;

    private AutoCloseable closeable;

    @BeforeEach
    public void initService(){
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void generateLabelDetectObjectTest(){

        boolean detecObject = true;

        List<DetectedObject> objectList = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            objectList.add(new DetectedObject("test1", "123"));
        }

        String expected = "test1 at time: " + sdf1.format(new Timestamp(System.currentTimeMillis()));
        String actual = addImageService.generateLabel(detecObject, objectList);

        Assertions.assertEquals(actual, expected);

    }

    @Test
    public void generateLabelNotDetectObjectTest(){
        boolean detectObject = false;

        List<DetectedObject> ob = new ArrayList();


        String actual = addImageService.generateLabel(detectObject, ob);

        String expected = sdf1.format(new Timestamp(System.currentTimeMillis()));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void addImageWithObjectDetectionTest() throws ApplicationException {

        ImageRequest request = new ImageRequest();
        request.setImageLabel("test label");
        request.setImageUrl("test URL");
        request.setDetectObject(true);

        Response response = new Response();
        response.setStatus(Status.Ok);

        List<DetectedObject> objectList = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            objectList.add(new DetectedObject("test1", "123"));
        }

        ImageDO imageDO = new ImageDO(request.getImageLabel(), request.getImageUrl());
        imageDO.setId(8);
        when(imaggaClient.detectObject(request.getImageUrl())).thenReturn(objectList);
        when(imageRepository.save(any())).thenReturn(imageDO);
        when(objectRepository.save(any())).thenReturn(new ObjectDO());
        ImageResponse actual = addImageService.addImage(request);

        Assertions.assertEquals(response.getStatus(),actual.getStatus());
        Assertions.assertTrue(actual.getErrorMessages().isEmpty());
        Assertions.assertEquals("Successfully inserted image and detected object", actual.getMessage());
        Assertions.assertFalse(actual.getImages().isEmpty());
        Assertions.assertEquals(5, actual.getImages().get(0).getObjects().size());
        Assertions.assertEquals("test1", actual.getImages().get(0).getObjects().get(0).getName());

    }
}
