package com.donaboyev.app.service.auth;

import com.donaboyev.app.entities.payload.response.MessageResponse;
import com.donaboyev.app.entities.payload.request.LoginRequest;
import com.donaboyev.app.entities.payload.response.JwtResponse;
import com.donaboyev.app.service.user.UserDetailsImpl;
import com.donaboyev.app.config.jwt.JwtUtils;
import com.donaboyev.app.dao.UserRepository;
import com.donaboyev.app.entities.User;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> login(LoginRequest request, HttpSession session) {
        boolean isValidEmail = userRepository.existsByEmail(request.getEmail());
        if (!isValidEmail) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is not valid"));
        }
        if (!userRepository.findByEmail(request.getEmail()).get().getEnabled()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email not verified yet"));
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()).get(0);
            session.setAttribute("email", request.getEmail());
            return ResponseEntity.ok(
                    new JwtResponse(
                            userDetails.getId(),
                            userDetails.getUsername(),
                            role,
                            jwt

                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Your login credentials may be invalid"));
        }

    }

    public MessageResponse changePassword(String newPassword, HttpSession session) {
        String email = (String) session.getAttribute("email");
        User user = userRepository.getUserByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new MessageResponse("Password changed successfully");
    }
}
