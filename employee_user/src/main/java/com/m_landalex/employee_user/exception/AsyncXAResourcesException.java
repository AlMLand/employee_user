package com.m_landalex.employee_user.exception;

public class AsyncXAResourcesException extends Exception {

	private static final long serialVersionUID = 1L;

	public AsyncXAResourcesException(String message) {
		super(message);
	}
	
	public AsyncXAResourcesException(Exception exception) {
		super(exception);
	}

}
