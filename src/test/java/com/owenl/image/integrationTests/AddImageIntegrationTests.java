package com.owenl.image.integrationTests;

import com.owenl.image.Model.ImageRequest;
import com.owenl.image.Model.Response;
import com.owenl.image.Model.TableModel.ImageDO;
import com.owenl.image.Model.TableModel.ObjectDO;
import com.owenl.image.TestImageRepository;
import com.owenl.image.TestObjectRepository;
import com.owenl.image.Utils.Status;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddImageIntegrationTests {

   @LocalServerPort
   private int port;
   private String baseUrl="http://localhost";
   private static final String imageUrl = "https://images.pexels.com/photos/4001296/pexels-photo-4001296.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2";

   private static RestTemplate template;

   @Autowired
   private TestImageRepository imageRepository;
   @Autowired
   private TestObjectRepository objectRepository;

   @BeforeAll
   public static void init() {
      template = new RestTemplate();
   }

   @BeforeEach
   public void setUp() {
      baseUrl = baseUrl.concat(":").concat(port + "").concat("/images");
   }

   @AfterEach
   void deleteEntities() {
      imageRepository.deleteAll();
      objectRepository.deleteAll();
   }


   @Test
   public void testAddImage() {
      ImageRequest request = new ImageRequest();

      request.setDetectObject(true);
      request.setImageUrl(imageUrl);
      request.setImageLabel("testLabel");

      Response response = template.postForObject(baseUrl, request, Response.class);

      //Response check
      Assertions.assertEquals("Successfully inserted image and detected object", response.getMessage());
      Assertions.assertTrue(response.getErrorDetails().isEmpty());
      Assertions.assertEquals(Status.Ok, response.getStatus());

      //Image check
      List<ImageDO> imageDOs = imageRepository.findByUrl(request.getImageUrl());
      Assertions.assertEquals("testLabel", imageDOs.get(0).getLabel());
      Assertions.assertEquals(imageUrl, imageDOs.get(0).getUrl());
      Assertions.assertTrue(imageDOs.get(0).getId() != 0);

      //DetectedObject check
      List<ObjectDO> objectDOs = objectRepository.findAll();
      Assertions.assertFalse(objectDOs.isEmpty());
      Assertions.assertEquals(imageDOs.get(0).getId(), objectDOs.get(0).getImage_Id());

   }

   @Test
   public void testAddImageWithNoObjectDetection() {
      ImageRequest request = new ImageRequest();

      request.setDetectObject(false);
      request.setImageUrl(imageUrl);
      request.setImageLabel("testLabel");

      Response response = template.postForObject(baseUrl, request, Response.class);

      //Response check
      Assertions.assertEquals("Successfully inserted image", response.getMessage());
      Assertions.assertTrue(response.getErrorDetails().isEmpty());
      Assertions.assertEquals(Status.Ok, response.getStatus());

      //Image check
      List<ImageDO> imageDOs = imageRepository.findAll();
      Assertions.assertFalse(imageDOs.isEmpty());
      Assertions.assertEquals("testLabel", imageDOs.get(0).getLabel());
      Assertions.assertEquals(imageUrl, imageDOs.get(0).getUrl());
      Assertions.assertTrue(imageDOs.get(0).getId() != 0);

      //Object check
      List<ObjectDO> objectDOs = objectRepository.findAll();

      Assertions.assertTrue(objectDOs.isEmpty());

   }

   @Test
   public void testAddImageWithEmptyURL() {
      ImageRequest request = new ImageRequest();

      request.setDetectObject(false);
      request.setImageUrl("");
      request.setImageLabel("testLabel");

      Response response;
      try {
         response = template.postForObject(baseUrl, request, Response.class);
      } catch (HttpStatusCodeException e) {
         response = e.getResponseBodyAs(Response.class);
      }

      Assertions.assertEquals("Missing field in request. Image URL is either missing or empty ", response.getMessage());
      Assertions.assertEquals(Status.BAD_REQUEST, response.getStatus());

   }

   @Test
   public void testAddImageWithInvalidDetectionURL() {
      ImageRequest request = new ImageRequest();

      request.setDetectObject(true);
      request.setImageUrl("invalid_URL");
      request.setImageLabel("testLabel");

      Response response;
      try {
         response = template.postForObject(baseUrl, request, Response.class);
      } catch (HttpStatusCodeException e) {
         response = e.getResponseBodyAs(Response.class);
      }

      Assertions.assertEquals("Failure evaluating image", response.getMessage());
      Assertions.assertEquals(Status.BAD_REQUEST, response.getStatus());
      Assertions.assertFalse(response.getErrorDetails().isEmpty());

   }



}
