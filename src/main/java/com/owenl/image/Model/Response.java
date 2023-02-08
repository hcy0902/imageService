package com.owenl.image.Model;

import com.owenl.image.Utils.Status;
import lombok.Data;

@Data
public class Response {

    Status status;
    String message;

    public Response () {

    }

}
