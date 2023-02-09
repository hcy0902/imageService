package com.owenl.image.Model;

import lombok.Data;

import java.util.List;
@Data
public class ImageResponse extends Response{

    List<Image> images;


}
