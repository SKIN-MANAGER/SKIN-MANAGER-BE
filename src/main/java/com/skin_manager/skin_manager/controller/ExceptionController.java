package com.skin_manager.skin_manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ApplicationContextException.class)
    public ResponseEntity<Void> applicationContextException(ApplicationContextException e) {
        if (log.isErrorEnabled()) {
            log.error("Controller Error : {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
