package com.darkness.userService.controller;
import com.darkness.commons.dto.user.UserDto;
import com.darkness.userService.domain.User;
import com.darkness.userService.exception.DuplicateUserException;
import com.darkness.userService.exception.UserNotFoundException;
import com.darkness.userService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
/**
 * @author darkness
 **/
@RestController
@RequestMapping("/api/user")
public class UserController extends UserExceptionController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    // Create a new user
    @PostMapping("register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        LOGGER.info(String.format("Starting create user %s", userDto.getEmail()));
        Optional<User> userExistingOptional = userService.getUserByEmail(userDto.getEmail());
        validateUser(userExistingOptional, userDto.getEmail());
        User createdUser = userService.createUser(userDto);
        UserDto result = convertToDto(createdUser);
        LOGGER.info(String.format("Done create user %s", userDto.getEmail()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    // Get all users
    @PreAuthorize("hasAnyRole('TEAM_LEAD', 'PROJECT_MANAGER')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by ID
//    @PreAuthorize("hasAnyRole('USER', 'TEAM_LEAD', 'PROJECT_MANAGER')")
//    @GetMapping("/me")
//    public ResponseEntity<UserDto> getUserById() {
//        System.out.printf("hello");
////        Optional<User> existingUsers = userService.getUserByEmail(email);
////        if (existingUsers.isPresent()) {
////            return ResponseEntity.ok(convertToDto(existingUsers.get()));
////        }
//        throw new UserNotFoundException("User not found");
//    }

    // Get user by email
    @PreAuthorize("hasAnyRole('USER', 'TEAM_LEAD', 'PROJECT_MANAGER')")
    @GetMapping("email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update user
    @PreAuthorize("hasRole('USER', 'TEAM_LEAD', 'PROJECT_MANAGER')")
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        return userService.getUserByUserId(userId)
                .map(existing -> {
//                    existing.setName(user.getName());
                    existing.setEmail(user.getEmail());
                    existing.setPassword(user.getPassword());
                    User updatedUser = userService.updateUser(existing);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        Optional<User> existingUsers = userService.getUserByEmail(email);
        if (existingUsers.isPresent()) {
            return ResponseEntity.ok(convertToDto(existingUsers.get()));
        }
        throw new UserNotFoundException("User not found");
    }

    private void validateUser(Optional<User> userExistingOptional,
                    String email) {
        if (userExistingOptional.isPresent()) {
            throw new DuplicateUserException(String.format(
                    "User with email %s already existing in db", email));
        }
    }

    private UserDto convertToDto(User createdUser) {
        return UserDto.builder()
                .pk(createdUser.getPk())
                .userId(createdUser.getUserId())
                .userRole(createdUser.getUserRole().name())
                .createdDate(createdUser.getCreatedDate())
                .updatedDate(createdUser.getUpdatedDate())
//                .name(createdUser.getName())
                .email(createdUser.getEmail())
                .build();

    }
}