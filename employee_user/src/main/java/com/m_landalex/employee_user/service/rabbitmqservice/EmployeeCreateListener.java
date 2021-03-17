package com.m_landalex.employee_user.service.rabbitmqservice;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeCreateListener {

	@RabbitListener(queues = "queueNumberOne")
	public void queue_1Listener(String statusMessage) {
		if(statusMessage.equals("succesful")) {
			log.info("ObJeCt Is SaVeD [ {} ]", statusMessage);
		}else if (statusMessage.equals("error")) {
			log.info("ObJeCt Is NoT SaVeD [ {} ]", statusMessage);
		}else {
			log.info("not option");
		}
	}
	
}
