package com.m_landalex.employee_user;

import javax.jms.ConnectionFactory;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Configuration
public class JmsConfig {

	/*
	 * so that the JmsTemplate (when receiving) is not blocked
	 */
	@Bean
	public JmsListenerContainerFactory<DefaultMessageListenerContainer> jmsListenerContainerFactory(
			ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer defaultJmsListenerContainerFactoryConfigurer) {
		DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
		defaultJmsListenerContainerFactoryConfigurer.configure(containerFactory, connectionFactory);
		return containerFactory;
	}
	
}
