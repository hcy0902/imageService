package com.owenl.image.Model;

import com.owenl.image.Utils.Status;
import lombok.Data;

import java.util.List;

@Data
public class Response {

    Status status;
    String message;
    List<String> errorMessages;

    public Response () {

    }

}
