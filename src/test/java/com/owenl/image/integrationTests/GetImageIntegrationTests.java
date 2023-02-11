package com.owenl.image.integrationTests;

import com.owenl.image.Model.Response;
import com.owenl.image.Model.TableModel.ImageDO;
import com.owenl.image.TestImageRepository;
import com.owenl.image.TestObjectRepository;
import com.owenl.image.Utils.Status;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetImageIntegrationTests {

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
    public void getAllImagesTest() {
        List<ImageDO> testDOs = new ArrayList<>();

        for (int i = 0; i < 3; i++){
            testDOs.add(new ImageDO("test_label", "test_url"));
        }
        imageRepository.saveAll(testDOs);

        Response response = template.getForObject(baseUrl, Response.class);

        //Response check
        Assertions.assertEquals("Retrieve images successful", response.getMessage());
        Assertions.assertTrue(response.getErrorDetails().isEmpty());
        Assertions.assertEquals(Status.Ok, response.getStatus());


    }

    @Test
    public void getAllImagesAndNoImageExistTest() {

        Response response = template.getForObject(baseUrl, Response.class);

        //Response check
        Assertions.assertEquals("No images in DB", response.getMessage());
        Assertions.assertTrue(response.getErrorDetails().isEmpty());
        Assertions.assertEquals(Status.Ok, response.getStatus());

    }

    @Test
    @Sql(statements = "INSERT INTO IMAGE (id,  label, url) VALUES (8, 'TEST_LABEL', 'TEST_URL')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getImageByIdTest() {

        Response response = template.getForObject(baseUrl+"/{id}", Response.class, 8);

        //Response check
        Assertions.assertEquals("Retrieve images successful", response.getMessage());
        Assertions.assertTrue(response.getErrorDetails().isEmpty());
        Assertions.assertEquals(Status.Ok, response.getStatus());

    }

    @Test
    public void getImageByIdFailureTest() {


        Response response = template.getForObject(baseUrl+"/{id}",  Response.class, 8);

        //Response check
        Assertions.assertEquals("Not able to find Image with ID : 8", response.getMessage());
        Assertions.assertTrue(response.getErrorDetails().isEmpty());
        Assertions.assertEquals(Status.Ok, response.getStatus());

    }

    @Test
    public void getImageByObjectFailureTest() {

        String url = "/objects?objects={name}";

        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl).append(url);

        Map<String, String> params = Collections.singletonMap("name", "pets");


        Response response = template.getForObject(sb.toString(), Response.class, params);

        //Response check
        Assertions.assertEquals("Not able to find images related to objects", response.getMessage());
        Assertions.assertTrue(response.getErrorDetails().isEmpty());
        Assertions.assertEquals(Status.Ok, response.getStatus());

    }


}
