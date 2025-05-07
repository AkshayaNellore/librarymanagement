package com.cts.lms.exception;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String msg) {
        super(msg);
    }
}