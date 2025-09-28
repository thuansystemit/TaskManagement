package com.darkness.userService.service;
import com.darkness.commons.dto.user.IdentificationDto;
import com.darkness.commons.dto.user.UserDto;
import com.darkness.commons.security.utils.PasswordUtils;
import com.darkness.userService.domain.GenderEnum;
import com.darkness.userService.domain.Identification;
import com.darkness.userService.domain.User;
import com.darkness.userService.domain.UserRoleEnum;
import com.darkness.userService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author darkness
 **/
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
    public User createUser(UserDto userDto) {
        User user = new User();
        if (Objects.isNull(userDto.getUserRole())) {
            user.setUserRole(UserRoleEnum.USER);
        }
        String userId = UUID.randomUUID().toString();
        user.setUserId("UserId-" + userId);
        user.setEmail(userDto.getEmail());
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());
        user.setUserRole(UserRoleEnum.valueOf(userDto.getUserRole()));
        String password = userDto.getPassword();
        user.setPassword(passwordUtils.encodePassword(password));
        IdentificationDto identificationDto = userDto.getIdentificationDto();
        Identification identification = new Identification();
        identification.setFirstName(identificationDto.getFirstName());
        identification.setLastName(identificationDto.getLastName());
        identification.setIdNumber(identificationDto.getIdNumber());
        identification.setGender(GenderEnum.valueOf(identificationDto.getGender()));
        identification.setMiddleName(identificationDto.getMiddleName());
        user.setIdentification(identification);
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

    @Override
    public User updateUser(User user) {
        return null;
    }
}
