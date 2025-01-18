package com.example.sadarah.service;

import com.example.sadarah.Exception.DuplicateEmailException;
import com.example.sadarah.Exception.UnconfirmedEmailException;
import com.example.sadarah.model.User;
import com.example.sadarah.repository.UserRepository;
import jakarta.mail.MessagingException;
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

    @Autowired
    private EmailService emailService;

    public User registerUser(User user) throws MessagingException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of("USER"));
        user.setConfirmed(false);
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationCode(), user.getUsername());
        return user;
    }

    public User confirmEmail(String email, String confirmationCode) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getVerificationCode().equals(confirmationCode)) {
            user.setConfirmed(true);
            userRepository.save(user);
            return user;
        }
        else throw new RuntimeException("Invalid verification code");
    }

    public User authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setVerificationCode(null);
            if (!user.isConfirmed()) {
                throw new UnconfirmedEmailException("Email not confirmed", user);
            }
            if (!passwordEncoder.matches(password, userOptional.get().getPassword())) {
                throw new RuntimeException("Incorrect password");
            }
            return user;
        }
        throw new RuntimeException("User not found");
    }

    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        emailService.sendVerificationEmail(email, user.getVerificationCode(), user.getUsername());
    }
}
