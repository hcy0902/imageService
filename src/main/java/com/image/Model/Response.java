package com.image.Model;

import com.image.Utils.Status;
import lombok.Data;

@Data
public class Response {

    Status status;
    String message;

    Exception exception;

    public Response () {

    }

}
