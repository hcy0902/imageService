package com.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = ApplicationController.class)
public class ImageService {

	public static void main(String[] args) {
		SpringApplication.run(ImageService.class, args);
	}

}
