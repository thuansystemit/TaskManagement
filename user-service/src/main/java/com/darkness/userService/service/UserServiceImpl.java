package com.darkness.userService.service;
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

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User createUser(User user) {
        if (Objects.isNull(user.getUserRole())) {
            user.setUserRole(UserRoleEnum.USER);
        }
        String userId = UUID.randomUUID().toString();
        user.setUserId("UserId-" + userId);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
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
