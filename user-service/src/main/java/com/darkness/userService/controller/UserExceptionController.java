package com.darkness.userService.controller;

import com.darkness.mvc.controller.BaseController;
import com.darkness.mvc.dto.ErrorResponse;
import com.darkness.userService.exception.DuplicateUserException;
import com.darkness.userService.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserExceptionController extends BaseController {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        logger.error("Error occurred: ", e);
        ErrorResponse error = new ErrorResponse("User not found exception.", e.getMessage());
        error.setErrorCode(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleException(DuplicateUserException e) {
        logger.error("Error occurred: ", e);
        ErrorResponse error = new ErrorResponse("User already existing.", e.getMessage());
        error.setErrorCode(HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthorizationDeniedException e) {
        logger.error("Error occurred: ", e);
        ErrorResponse error = new ErrorResponse("User don't have permission to access this api.", e.getMessage());
        error.setErrorCode(HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}
