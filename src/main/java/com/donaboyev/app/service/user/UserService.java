package com.donaboyev.app.service.user;

import com.donaboyev.app.entities.payload.request.ChangePasswordRequest;
import com.donaboyev.app.entities.payload.response.MessageResponse;
import com.donaboyev.app.dao.ConfirmationTokenRepository;
import com.donaboyev.app.dao.ContactRepository;
import com.donaboyev.app.dao.UserRepository;
import com.donaboyev.app.entities.UserRole;
import com.donaboyev.app.entities.Contact;
import com.donaboyev.app.entities.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.stream.Collectors;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email %s not found" + email));
        return UserDetailsImpl.build(user);
    }

    public MessageResponse deleteUser(Long userId) {
        try {
            confirmationTokenRepository.deleteUserToken(userRepository.findById(userId).get());
            userRepository.deleteById(userId);
            return new MessageResponse("Successfully deleted user");
        } catch (Exception e) {
            return new MessageResponse("Couldn't delete: " + e.getMessage());
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void enableUser(String email) {
        userRepository.enableUser(email);
    }

    public ResponseEntity<?> createContact(Contact contact, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.getUserByEmail(email);
            contact.setUser(user);
            contact.setConfirmed(true);
            contact = contactRepository.save(contact);
            contact = contactRepository.save(contact);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Couldn't create contact"));
        }
        return ResponseEntity.ok(contact);
    }

    public ResponseEntity<?> updateContact(Contact newContact, Long contactId) {
        try {
            contactRepository.updateContact(
                    contactId,
                    newContact.getFirstName(),
                    newContact.getSecondName(),
                    newContact.getWork(),
                    newContact.getEmail(),
                    newContact.getPhone(),
                    newContact.getDescription(),
                    LocalDateTime.now()
            );
            return ResponseEntity.ok(new MessageResponse("Successfully updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Contact doesn't exists with this id: " + e.getMessage()));
        }
    }

    public MessageResponse deleteContact(Long contactId) {
        try {
            contactRepository.deleteById(contactId);
        } catch (Exception e) {
            return new MessageResponse("Couldn't delete contact");
        }
        return new MessageResponse("Deleted successfully");
    }

    public MessageResponse deleteAllContacts() {
        try {
            contactRepository.deleteAll();
        } catch (Exception e) {
            return new MessageResponse("Couldn't delete all contacts");
        }
        return new MessageResponse("All contacts deleted successfully");
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getUserRole() == UserRole.ROLE_USER)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    public MessageResponse updateUser(Long userId, Boolean lock) {
        try {
            User user = userRepository.findById(userId).get();
            user.setLocked(lock);
            userRepository.save(user);
            return new MessageResponse("User successfully updated");
        } catch (Exception e) {
            return new MessageResponse("Couldn't updated");
        }
    }

    public ResponseEntity<?> shareContact(Long contactId, String email) {
        try {
            Contact contact = contactRepository.findById(contactId).get();
            Contact newContact = new Contact();
            newContact.setFirstName(contact.getFirstName());
            newContact.setSecondName(contact.getSecondName());
            newContact.setWork(contact.getWork());
            newContact.setEmail(contact.getEmail());
            newContact.setPhone(contact.getPhone());
            newContact.setUser(userRepository.getUserByEmail(email));
            newContact.setDescription(contact.getDescription());
            newContact.setConfirmed(false);
            contactRepository.save(newContact);
            return ResponseEntity.ok(newContact);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Couldn't share the contact"));
        }
    }

    public MessageResponse changePassword(ChangePasswordRequest changePasswordRequest, Principal principal) {
        String email = principal.getName();
        User user = userRepository.getUserByEmail(email);
        try {
            if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                userRepository.save(user);
                return new MessageResponse("Password successfully updated");
            }
            return new MessageResponse("Old password doesn't match");
        } catch (Exception e) {
            return new MessageResponse("Couldn't update password! " + e.getMessage());
        }

    }

}
