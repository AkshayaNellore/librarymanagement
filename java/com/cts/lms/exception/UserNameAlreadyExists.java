package com.cts.lms.exception;

public class UserNameAlreadyExists extends RuntimeException{
	public UserNameAlreadyExists(String msg) {
		super(msg);
	}
}
