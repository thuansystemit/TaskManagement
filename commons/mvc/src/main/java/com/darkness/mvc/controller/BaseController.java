package com.darkness.mvc.controller;

import com.darkness.mvc.dto.ApiResponse;
import com.darkness.mvc.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

//    // Common exception handling
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception e) {
//        logger.error("Error occurred: ", e);
//        ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }

    // Common validation error handling
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse("Validation Error", message);
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(error);
    }

    // Common success response helper
    protected <T> ResponseEntity<ApiResponse<T>> successResponse(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(true, message, data);
        return ResponseEntity.ok(response);
    }

    // Common error response helper
    protected ResponseEntity<ApiResponse<Object>> errorResponse(String message, HttpStatus status) {
        ApiResponse<Object> response = new ApiResponse<>(false, message, null);
        return ResponseEntity.status(status).body(response);
    }
}
