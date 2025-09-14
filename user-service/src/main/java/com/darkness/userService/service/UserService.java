package com.darkness.userService.service;

import com.darkness.commons.dto.user.UserDto;
import com.darkness.userService.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserByUserId(String userId);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
}
