package com.supermarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SupermarketExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SupermarketException.class)
    public ResponseEntity<String> handleSupermarketException(SupermarketException supermarketException) {
        return ResponseEntity.badRequest().body(supermarketException.getMessage());
    }
}
