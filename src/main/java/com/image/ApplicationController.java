package com.image;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ApplicationController {

    @PostMapping("/blogCreationService")
    public ResponseEntity<BlogCreationResponse> blogCreation (@Valid @RequestBody BlogCreationRequest blogCreationRequest){

        BlogCreationResponse blogCreationResponse = new BlogCreationResponse();
        blogCreationResponse = blogService.createBlog(blogCreationRequest);

        return ResponseEntity.ok(blogCreationResponse);
    }


}
