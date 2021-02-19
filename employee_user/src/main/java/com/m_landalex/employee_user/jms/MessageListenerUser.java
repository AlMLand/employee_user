package com.m_landalex.employee_user.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListenerUser implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(MessageListenerUser.class);

	@JmsListener(destination = "users", containerFactory = "jmsListenerContainerFactory")
	public void onMessage(Message message) throws JMSException {
		TextMessage textMessage = (TextMessage) message;
		logger.info("--> Received message: {}", textMessage.getText());
	}

}
