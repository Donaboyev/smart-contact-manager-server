package com.donaboyev.app.controller;

import com.donaboyev.app.dao.ContactRepository;
import com.donaboyev.app.dao.UserRepository;
import com.donaboyev.app.entities.Contact;
import com.donaboyev.app.entities.User;
import com.donaboyev.app.entities.payload.request.ChangePasswordRequest;
import com.donaboyev.app.entities.payload.response.MessageResponse;
import com.donaboyev.app.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/user")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public User profile(Principal principal) {
        return userRepository.getUserByEmail(principal.getName());
    }

    @RequestMapping( method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createContact(@RequestBody Contact contact, Principal principal) {
        return userService.createContact(contact, principal);
    }

    @PutMapping("/{contactId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateContact(@PathVariable("contactId") Long contactId, @RequestBody Contact newContact) {
        return userService.updateContact(newContact, contactId);
    }

    @DeleteMapping("/{contactId}")
    @PreAuthorize("hasRole('USER')")
    public MessageResponse delete(@PathVariable("contactId") Long contactId) {
        return userService.deleteContact(contactId);
    }

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasRole('USER')")
    public MessageResponse deleteAll() {
        return userService.deleteAllContacts();
    }

    @PostMapping("/{contactId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> shareContact(@PathVariable("contactId") Long contactId, @RequestParam("email") String email) {
        return userService.shareContact(contactId, email);
    }

    @GetMapping("/{contactId}")
    @PreAuthorize("hasRole('USER')")
    public Contact getContactDetails(@PathVariable("contactId") Long contactId, Principal principal) {
        Contact contact = contactRepository.findById(contactId).get();
        String username = principal.getName();
        User user = userRepository.getUserByEmail(username);
        if (user.getId().equals(contact.getUser().getId())) {
            return contact;
        }
        return null;
    }

    @PostMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public MessageResponse changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) {
        return userService.changePassword(changePasswordRequest, principal);
    }

}
