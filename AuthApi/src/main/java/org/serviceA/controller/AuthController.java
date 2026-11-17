package org.serviceA.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.serviceA.dto.user.UserLoginRequestDto;
import org.serviceA.dto.user.UserLoginResponseDto;
import org.serviceA.dto.user.UserRegistrationRequestDto;
import org.serviceA.exception.RegistrationException;
import org.serviceA.security.AuthenticationService;
import org.serviceA.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for authentication users")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login", description = "Authenticates an user and returns JWT token")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Create a new user and save it in DB")
    public void register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        userService.register(requestDto);
    }
}
