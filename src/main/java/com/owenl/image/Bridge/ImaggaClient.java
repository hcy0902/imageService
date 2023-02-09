package com.owenl.image.Bridge;

import com.owenl.image.Model.ApplicationException;
import com.owenl.image.Model.DetectedObject;
import com.owenl.image.Model.Imagga.ImaggaResponse;
import com.owenl.image.Utils.Model.Category;
import com.owenl.image.Utils.Status;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImaggaClient {
    //APIkey = acc_e25711c7fc594ce
    //API secrit = ee34c6cd62883abc94befa7a5e487680

    private final String imaggaUri = "https://api.imagga.com/v2/categories/personal_photos?";
    private static final String IMMAGA_SERVER_ERROR_MESSAGE = "Error session with Immaga server";
    String credential = "acc_e25711c7fc594ce:ee34c6cd62883abc94befa7a5e487680";
    public List<DetectedObject> detectObject (String imageUrl) throws ApplicationException {

        List<DetectedObject> objects = new ArrayList<>();

        StringBuilder url = new StringBuilder();
        url.append(imaggaUri).append("image_url=").append(imageUrl);

        byte[] plainCredsBytes = credential.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + new String(base64CredsBytes));

        HttpEntity<String> request = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ImaggaResponse> response;
        try {
            response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, ImaggaResponse.class);
        } catch (Exception ex){
            ApplicationException exception = new ApplicationException(ex, Status.INTERNAL_SERVER_ERROR, IMMAGA_SERVER_ERROR_MESSAGE );
            throw exception;
        }


        ImaggaResponse imaggaResponse = response.getBody();

        List<Category> categories = imaggaResponse.getResult().getCategories();

        for (Category category : categories){
            objects.add(new DetectedObject(category.getName().getEn(), category.getConfidence()));
        }

        return objects;

    }

}
