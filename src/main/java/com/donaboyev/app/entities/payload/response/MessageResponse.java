package com.donaboyev.app.entities.payload.response;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MessageResponse {
    private String message;
}
