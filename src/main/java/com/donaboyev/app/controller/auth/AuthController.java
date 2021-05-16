package com.donaboyev.app.controller.auth;

import com.donaboyev.app.entities.payload.request.LoginRequest;
import com.donaboyev.app.entities.payload.request.RegisterRequest;
import com.donaboyev.app.entities.payload.response.MessageResponse;
import com.donaboyev.app.service.auth.LoginService;
import com.donaboyev.app.service.auth.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "api/auth")
@AllArgsConstructor
public class AuthController {

    private final RegisterService registerService;
    private final LoginService loginService;

    @PostMapping(path = "register")
    public MessageResponse register(@RequestBody RegisterRequest request) {
        return registerService.register(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registerService.confirmToken(token);
    }

    @PostMapping(path = "login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,HttpSession session) {
        return loginService.login(request,session);
    }

    @GetMapping(path = "change-password")
    public MessageResponse changePassword(@RequestParam("newPassword") String newPassword, HttpSession session) {
        return loginService.changePassword(newPassword, session);
    }

}
