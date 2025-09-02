package com.darkness.userService.service;
import com.darkness.commons.security.utils.PasswordUtils;
import com.darkness.userService.domain.User;
import com.darkness.userService.domain.UserRoleEnum;
import com.darkness.userService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;

    public UserServiceImpl(final UserRepository userRepository,
                           final PasswordUtils passwordUtils) {
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
    }
    @Override
    public User createUser(User user) {
        if (Objects.isNull(user.getUserRole())) {
            user.setUserRole(UserRoleEnum.USER);
        }
        String userId = UUID.randomUUID().toString();
        user.setUserId("UserId-" + userId);
        String password = user.getPassword();
        user.setPassword(passwordUtils.encodePassword(password));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
