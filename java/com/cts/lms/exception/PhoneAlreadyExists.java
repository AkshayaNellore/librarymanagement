package com.cts.lms.exception;

public class PhoneAlreadyExists extends RuntimeException {
    public PhoneAlreadyExists(String msg) {
        super(msg);
    }
}