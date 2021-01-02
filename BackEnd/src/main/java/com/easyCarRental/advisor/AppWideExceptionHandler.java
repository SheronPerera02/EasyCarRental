package com.easyCarRental.advisor;

import com.easyCarRental.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class AppWideExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity handleException(RuntimeException ex) {
        StandardResponse response = new StandardResponse(500, ex.getMessage(), null);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}



