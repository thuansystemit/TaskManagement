package com.darkness.userService.controller;

import com.darkness.mvc.controller.BaseController;
import com.darkness.mvc.dto.ErrorResponse;
import com.darkness.userService.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserExceptionController extends BaseController {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error("Error occurred: ", e);
        ErrorResponse error = new ErrorResponse("User not found exception.", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
