package com.example.sadarah.service;

import com.example.sadarah.model.User;
import com.example.sadarah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of("USER"));
        user.setConfirmed(false);
        userRepository.save(user);
        return user;
    }

    public User confirmEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setConfirmed(true);
        return userRepository.save(user);
    }

    public User authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!userOptional.get().isConfirmed()) {
                throw new RuntimeException("Email not confirmed");
            }
            if (!passwordEncoder.matches(password, userOptional.get().getPassword())) {
                throw new RuntimeException("Incorrect password");
            }
            return user;
        }
        throw new RuntimeException("User not found");
    }
}
