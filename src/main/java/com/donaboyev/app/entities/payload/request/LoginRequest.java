package com.donaboyev.app.entities.payload.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LoginRequest {
    @Email(message = "Email is not valid")
    private final String email;
    @Size(min = 5,message = "Password should not be less than 5 characters")
    private final String password;
}
