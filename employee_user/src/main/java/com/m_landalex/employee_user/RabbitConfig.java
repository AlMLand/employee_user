package com.m_landalex.employee_user;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Value("${rabbitmq.queueName}") private String queueName;
	@Value("${rabbitmq.exchangeName}") private String exchangeName;

	@Bean
	public Queue queue_1() {
		return new Queue(queueName, true);
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(exchangeName, true, false);
	}

	@Bean
	public Binding binding(DirectExchange directExchange, Queue queue) {
		return BindingBuilder.bind(queue).to(directExchange).with(queueName);
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory("127.0.0.1");
	}

	@Bean
	public SimpleMessageListenerContainer simpleMessageListenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cachingConnectionFactory());
		container.setQueueNames(queueName);
		container.setConcurrentConsumers(5);
		return container;
	}

}
