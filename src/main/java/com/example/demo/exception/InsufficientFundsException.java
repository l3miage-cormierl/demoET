package com.example.demo.exception;

// InsufficientFundsException.java
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
