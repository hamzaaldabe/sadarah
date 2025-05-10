package com.example.sadarah.Exception;

import com.example.sadarah.model.User;

public class UnconfirmedEmailException extends RuntimeException {
    private final User user;

    public UnconfirmedEmailException(String message, User user) {
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
