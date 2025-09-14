package com.darkness.userService.exception;

/**
 * @author darkness
 **/
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
