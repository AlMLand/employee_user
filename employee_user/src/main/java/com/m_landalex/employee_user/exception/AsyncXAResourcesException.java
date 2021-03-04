package com.m_landalex.employee_user.exception;

public class AsyncXAResourcesException extends Exception {

	private static final long serialVersionUID = 1L;

	private String text;

	public AsyncXAResourcesException(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "AsyncXAResourcesException [text=" + text + "]";
	}

}
