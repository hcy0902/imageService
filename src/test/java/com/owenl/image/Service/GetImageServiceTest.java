package com.owenl.image.Service;

import com.owenl.image.Model.ImageResponse;
import com.owenl.image.Model.TableModel.ImageObjDO;
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
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetImageServiceTest {

    @Mock
    ImageRepository imageRepository;

    @Mock
    ObjectRepository objectRepository;

    @InjectMocks
    GetImageService imageService;

    private AutoCloseable closeable;

    @BeforeEach
    public void initService(){
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getImageByIdTest() {

        int imageId = 8;

        List<ImageObjDO> imageObjDOs = List.of(new ImageObjDO(imageId, "test label", "test Url", 3,
                "confidence", "pets"));
        when(imageRepository.fetchImageById(imageId)).thenReturn(imageObjDOs);

        ImageResponse actual = imageService.getImage(8);

        Assertions.assertEquals(Status.Ok, actual.getStatus());
        Assertions.assertEquals("Retrieve images successful", actual.getMessage());
        Assertions.assertFalse(actual.getImages().isEmpty());
        Assertions.assertEquals(imageId, actual.getImages().get(0).getId());

    }

    @Test
    public void getImageByIdNotFoundTest() {

        int imageId = 8;

        when(imageRepository.fetchImageById(imageId)).thenReturn(new ArrayList<>());

        ImageResponse actual = imageService.getImage(8);

        Assertions.assertEquals(Status.Ok, actual.getStatus());
        Assertions.assertEquals("Not able to find Image with ID : 8", actual.getMessage());
        Assertions.assertNull(actual.getImages());

    }

    @Test
    public void getImageDBExceptionTest() {

        int imageId = 8;

        when(imageRepository.fetchImageById(imageId)).thenThrow(new DataAccessResourceFailureException(""));

        ImageResponse actual = imageService.getImage(8);

        Assertions.assertEquals(Status.INTERNAL_SERVER_ERROR, actual.getStatus());
        Assertions.assertFalse(actual.getErrorDetails().isEmpty());
        Assertions.assertEquals("Error retrieving from DB for: ImageObjDO", actual.getErrorDetails().get(0));
        Assertions.assertEquals("Failed to retrieve from DB", actual.getMessage());
        Assertions.assertNull(actual.getImages());

    }

    @Test
    public void getAllImageNoImageInDBTest() {

        when(imageRepository.fetchImageObjectLeftOuterJoin()).thenReturn(new ArrayList<>());

        ImageResponse actual = imageService.getAllImages();

        Assertions.assertEquals(Status.Ok, actual.getStatus());
        Assertions.assertTrue(actual.getErrorDetails().isEmpty());
        Assertions.assertEquals("No images in DB", actual.getMessage());
        Assertions.assertNull(actual.getImages());

    }
}
