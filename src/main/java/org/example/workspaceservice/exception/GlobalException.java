package org.example.workspaceservice.exception;


import org.example.workspaceservice.utils.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException notFoundException){
        ExceptionResponse response = ExceptionResponse.builder()
                .errorMessage(notFoundException.getMessage()+"!")
                .errorStatus(HttpStatus.NOT_FOUND)
                .errorStatusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> conflictException(ConflictException conflictException) {
        ExceptionResponse response= ExceptionResponse.builder()
                .errorMessage(conflictException.getMessage())
                .errorStatus(HttpStatus.CONFLICT)
                .errorStatusCode(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
