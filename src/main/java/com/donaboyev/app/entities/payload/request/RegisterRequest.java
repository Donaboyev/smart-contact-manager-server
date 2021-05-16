package com.donaboyev.app.entities.payload.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegisterRequest {
    @NotBlank(message = "First name cannot be empty")
    private final String firstName;
    private final String lastName;
    @Email(message = "Email is not valid")
    private final String email;
    @Size(min = 5, message = "Password should not be less than 5 characters")
    private final String password;
}
