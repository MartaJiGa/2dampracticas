package com.svalero.bookapi.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {super(message);
    }
}
