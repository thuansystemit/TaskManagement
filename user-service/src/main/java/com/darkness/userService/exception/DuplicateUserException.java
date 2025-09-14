package com.darkness.userService.exception;
/**
 * @author darkness
 **/
public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String msg) {
        super(msg);
    }
}
