package com.donaboyev.app.entities.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JwtResponse {
    private Long id;
    private String email;
    private String role;
    private String token;
    private String type = "Bearer";

    public JwtResponse(Long id, String email, String role, String token) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.token = token;
    }

}
