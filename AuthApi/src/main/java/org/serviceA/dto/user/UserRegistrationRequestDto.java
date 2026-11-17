package org.serviceA.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import org.hibernate.validator.constraints.Length;

@Data
public class UserRegistrationRequestDto {
    @Email
    @NotBlank(message = "Please write your email")
    @Length(min = 10, max = 50)
    private String email;
    @NotBlank(message = "Please write your password")
    @Length(min = 10, max = 50)
    private String password;
}
