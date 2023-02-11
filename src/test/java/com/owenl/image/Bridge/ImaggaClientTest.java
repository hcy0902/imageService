package com.owenl.image.Bridge;

import com.owenl.image.Model.ApplicationException;
import com.owenl.image.Model.DetectedObject;
import com.owenl.image.Model.Imagga.ImaggaResponse;
import com.owenl.image.Utils.Model.Category;
import com.owenl.image.Utils.Model.Name;
import com.owenl.image.Utils.Model.Result;
import com.owenl.image.Utils.Status;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImaggaClientTest {

    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    ImaggaClient imaggaClient;

    private AutoCloseable closeable;

    @BeforeEach
    public void initService(){
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void detectObjectTest() throws ApplicationException {

        String imageUrl = "testUrl";
        ImaggaResponse response = buildResponse();

        HttpEntity<String> request = buildRequest();

        ResponseEntity<ImaggaResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange("https://api.imagga.com/v2/categories/personal_photos?image_url=testUrl"
                , HttpMethod.GET, request, ImaggaResponse.class))
                .thenReturn(responseEntity);

        List<DetectedObject> expected = List.of(new DetectedObject("pets", "testConfidence"));


        List<DetectedObject> actual = imaggaClient.detectObject(imageUrl);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void detectObjectInvalidURLExceptionTest() throws ApplicationException {

        String imageUrl = "testUrl";
        ImaggaResponse response = buildResponse();

        HttpEntity<String> request = buildRequest();

        ResponseEntity<ImaggaResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange("https://api.imagga.com/v2/categories/personal_photos?image_url=testUrl"
                , HttpMethod.GET, request, ImaggaResponse.class))
                .thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(400)));

        ApplicationException actual = Assertions.assertThrows(ApplicationException.class, ()-> {
            imaggaClient.detectObject(imageUrl);
        });


        Assertions.assertEquals(Status.BAD_REQUEST, actual.getStatus());
        Assertions.assertEquals("BAD_REQUEST", actual.getMessage());

    }

    @Test
    public void detectObjectServerExceptionTest() throws ApplicationException {

        String imageUrl = "testUrl";
        ImaggaResponse response = buildResponse();

        HttpEntity<String> request = buildRequest();

        ResponseEntity<ImaggaResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange("https://api.imagga.com/v2/categories/personal_photos?image_url=testUrl"
                , HttpMethod.GET, request, ImaggaResponse.class))
                .thenThrow(new HttpServerErrorException(HttpStatusCode.valueOf(500)));

        ApplicationException actual = Assertions.assertThrows(ApplicationException.class, ()-> {
            imaggaClient.detectObject(imageUrl);
        });


        Assertions.assertEquals(Status.INTERNAL_SERVER_ERROR, actual.getStatus());
        Assertions.assertEquals("Server error during session with Imagga", actual.getMessage());

    }

    private ImaggaResponse buildResponse() {
        ImaggaResponse response = new ImaggaResponse();
        Category category = new Category();
        category.setConfidence("testConfidence");
        Name name = new Name();
        name.setEn("pets");
        category.setName(name);
        Result result = new Result();
        result.setCategories(List.of(category));
        response.setResult(result);
        return response;
    }


    private HttpEntity<String> buildRequest() {
        String credential = "acc_e25711c7fc594ce:ee34c6cd62883abc94befa7a5e487680";
        byte[] plainCredsBytes = credential.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + new String(base64CredsBytes));

        return new HttpEntity<String>(headers);
    }

}
