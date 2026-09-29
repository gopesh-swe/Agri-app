package com.agri.app.exception;

import com.agri.app.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIlligalArgumentException(IllegalArgumentException exception){
        ApiResponse<Void> response = ApiResponse.error(exception.getMessage());
        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchElementException(NoSuchElementException exception){
        ApiResponse<Void> response = ApiResponse.error(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
