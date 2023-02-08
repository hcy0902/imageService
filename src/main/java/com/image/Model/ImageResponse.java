package com.image.Model;

import com.image.Utils.Status;
import lombok.Data;

import java.util.List;
@Data
public class ImageResponse extends Response{

    List<Image> images;



}
