package com.example.sadarah.controller;

import com.example.sadarah.Exception.DuplicateEmailException;
import com.example.sadarah.Exception.UnconfirmedEmailException;
import com.example.sadarah.Request.LoginRequest;
import com.example.sadarah.Request.SignupRequest;
import com.example.sadarah.model.User;
import com.example.sadarah.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            String verificationCode = String.valueOf((int) (Math.random() * 900000) + 100000);

            User user = User.builder()
                    .username(signupRequest.getUsername())
                    .password(passwordEncoder.encode(signupRequest.getPassword()))
                    .email(signupRequest.getEmail())
                    .address(signupRequest.getAddress())
                    .isConfirmed(false)
                    .verificationCode(verificationCode)
                    .roles(Set.of("USER"))
                    .phone(signupRequest.getPhone())
                    .build();

            User registeredUser = userService.registerUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);

        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/confirm")
    public ResponseEntity<User> confirmEmail(@RequestParam String email, @RequestParam String verificationCode) {
        try {
            User user = userService.confirmEmail(email, verificationCode);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.authenticateUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(user);
        } catch (UnconfirmedEmailException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getUser());
        } catch (RuntimeException e) {
            String message = e.getMessage();
            return switch (message) {
                case "User not found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                case "Incorrect password" -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            };
        }
    }

    @PostMapping("/resend-code")
    public ResponseEntity<?> resendCode(@RequestParam String email) {
        try {
            userService.resendVerificationCode(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

