package com.image.Bridge;

import com.image.Model.DetectedObject;
import com.image.Model.Imagga.ImaggaResponse;
import com.image.Utils.Model.TagItem;
import org.apache.tomcat.util.codec.binary.Base64;
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

    private final String imaggaUri = "https://api.imagga.com/v2/tags?";
    String url;
    String credential = "acc_e25711c7fc594ce:ee34c6cd62883abc94befa7a5e487680";
    public List<DetectedObject> detectObject (String imageUrl) {

        List<DetectedObject> objects = new ArrayList<>();

        StringBuilder url = new StringBuilder();
        url.append(imaggaUri).append("image_url=").append(imageUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + credential);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ImaggaResponse> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, ImaggaResponse.class);

        ImaggaResponse imaggaResponse = response.getBody();

        List<TagItem> tagItems = imaggaResponse.getResult().getTags();

        for (TagItem tagItem : tagItems){
            objects.add(new DetectedObject(tagItem.getTag().getEn(), tagItem.getConfidence()));
        }

        return objects;

    }

}
