package com.donaboyev.app.controller;

import com.donaboyev.app.entities.User;
import com.donaboyev.app.entities.payload.response.MessageResponse;
import com.donaboyev.app.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> showUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse deleteUser(@PathVariable("userId") Long userId) {
        return userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse updateUser(@PathVariable("userId") Long userId,@RequestParam("lock") Boolean lock) {
        return userService.updateUser(userId,lock);
    }

}
