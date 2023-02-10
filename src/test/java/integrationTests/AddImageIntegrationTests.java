package integrationTests;

import com.owenl.image.Model.ImageRequest;
import com.owenl.image.Model.Response;
import com.owenl.image.Model.TableModel.ImageDO;
import com.owenl.image.Repository.TestRepository;
import com.owenl.image.Utils.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddImageIntegrationTests {

   @LocalServerPort
   private int port;
   private String baseUrl="http://localhost";
   private static final String imageUrl = "https://images.pexels.com/photos/4001296/pexels-photo-4001296.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2";

   private static RestTemplate template;

   @Autowired
   private TestRepository repository;

   @BeforeAll
   public static void init() {
      template = new RestTemplate();
   }

   @BeforeEach
   public void setUp() {
      baseUrl = baseUrl.concat(":").concat(port + "").concat("/images");
   }


   @Test
   public void testAddImage() {
      ImageRequest request = new ImageRequest();

      request.setDetectObject(true);
      request.setImageUrl(imageUrl);
      request.setImageLabel("testLabel");

      Response response = template.postForObject(baseUrl, request, Response.class);

      Assertions.assertEquals("Successfully inserted image and detected object", response.getMessage());
      Assertions.assertTrue(response.getErrorMessages().isEmpty());
      Assertions.assertEquals(Status.Ok, response.getStatus());

      //DB check
      ImageDO imageDO = repository.fetchImageByUrl(request.getImageUrl());
      Assertions.assertEquals("testLabel", imageDO.getLabel());
      Assertions.assertEquals(imageUrl, imageDO.getUrl());
      Assertions.assertTrue(imageDO.getId() != 0);

   }


}
