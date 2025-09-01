package com.darkness.taskManagement.service;

import com.darkness.taskManagement.domain.User;
import com.darkness.taskManagement.domain.UserRoleEnum;
import com.darkness.taskManagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
