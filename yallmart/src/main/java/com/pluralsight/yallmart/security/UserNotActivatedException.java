package com.pluralsight.yallmart.security;

public class UserNotActivatedException extends RuntimeException {

	private static final long serialVersionUID = -1126699074574529145L;

	public UserNotActivatedException(String message) {
		super(message);
	}
}
