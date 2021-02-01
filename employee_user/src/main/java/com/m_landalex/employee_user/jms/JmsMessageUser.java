package com.m_landalex.employee_user.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageUser {

	private static final Logger logger = LoggerFactory.getLogger(JmsMessageUser.class);
	
	@JmsListener(destination = "users")
	public void onMessage(String message) {
		logger.info("--> Received message: {}", message);
	}
	
}
