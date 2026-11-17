package org.serviceA.service;

import org.serviceA.dto.user.UserRegistrationRequestDto;
import org.serviceA.exception.RegistrationException;

public interface UserService {
    void register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
