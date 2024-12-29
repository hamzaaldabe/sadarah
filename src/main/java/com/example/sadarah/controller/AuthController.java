package com.example.sadarah.controller;

import com.example.sadarah.Request.LoginRequest;
import com.example.sadarah.model.User;
import com.example.sadarah.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/confirm")
    public ResponseEntity<User> confirmEmail(@RequestParam String email) {
        User user = userService.confirmEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        try {
//            User user = userService.authenticateUser(request.getEmail(), request.getPassword());
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            String message = e.getMessage();
//            return switch (message) {
//                case "User not found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//                case "Email not confirmed" -> ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email not confirmed");
//                case "Incorrect password" -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
//                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
//            };
//        }
//    }
//}
}
