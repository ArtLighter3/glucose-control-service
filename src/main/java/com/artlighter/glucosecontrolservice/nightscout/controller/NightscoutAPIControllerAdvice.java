package com.artlighter.glucosecontrolservice.nightscout.controller;

import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.nightscout.util.exception.NightscoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = NightscoutAPIController.class)
public class NightscoutAPIControllerAdvice {
    @ExceptionHandler(NightscoutException.class)
    public ResponseEntity nightscoutException(NightscoutException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(405)).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(405)).build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity resourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(404)).build();
    }
}
