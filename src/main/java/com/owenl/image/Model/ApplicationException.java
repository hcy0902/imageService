package com.owenl.image.Model;

import com.owenl.image.Utils.Status;
import lombok.Data;

@Data
public class ApplicationException extends Exception{

    Status status;
    String message;
    Exception exception;

    public ApplicationException (Exception exception, Status status, String message){
        this.exception = exception;
        this.status = status;
        this.message = message;
    }

}
