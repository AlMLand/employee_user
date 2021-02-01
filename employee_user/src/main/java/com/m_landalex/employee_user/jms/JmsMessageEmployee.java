package com.m_landalex.employee_user.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageEmployee {
	
	private static final Logger logger = LoggerFactory.getLogger(JmsMessageEmployee.class);

	@JmsListener(destination = "employees")
	public void onMessage(String message) {
		logger.info("--> Received message: {}", message);
	}
	
}