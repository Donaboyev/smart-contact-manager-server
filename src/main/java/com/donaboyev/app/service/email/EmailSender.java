package com.donaboyev.app.service.email;

public interface EmailSender {
    Boolean send(String to, String email);
}
