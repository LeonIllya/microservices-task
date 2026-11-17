package org.serviceA.service.impl;

import lombok.RequiredArgsConstructor;

import org.serviceA.dto.user.UserRegistrationRequestDto;
import org.serviceA.exception.RegistrationException;
import org.serviceA.mapper.UserMapper;
import org.serviceA.model.User;
import org.serviceA.repository.UserRepository;
import org.serviceA.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(UserRegistrationRequestDto requestDto) throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "This email is already registered: " + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
