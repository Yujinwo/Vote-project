package com.react.voteproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



public class CreationException extends RuntimeException {


    public CreationException(String message) {
        super(message);
    }
}
