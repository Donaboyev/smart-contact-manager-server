package com.donaboyev.app.entities.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ChangePasswordRequest {
    @NotBlank
    private final String oldPassword;
    @NotBlank
    private final String newPassword;
}
