package com.klasha.globalgeostat.core.advice;

import com.klasha.globalgeostat.core.base.Response;
import com.klasha.globalgeostat.core.exception.ConflictException;
import com.klasha.globalgeostat.core.exception.GeoServiceException;
import com.klasha.globalgeostat.core.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(GeoServiceException.class)
    public ResponseEntity<Response<?>> handleGeoServiceException(GeoServiceException ex) {
        return new ResponseEntity<>(new Response<>(false, ex.getLocalizedMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new Response<>(false, ex.getLocalizedMessage()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Response<?>> handleConflictException(ConflictException ex) {
        return new ResponseEntity<>(new Response<>(false, ex.getLocalizedMessage()),
            HttpStatus.CONFLICT);
    }
}
