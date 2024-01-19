package com.example.demo.exception;

public class MyAccountNotFoundException extends RuntimeException {
    public MyAccountNotFoundException(String message) {
        super(message);
    }
}
