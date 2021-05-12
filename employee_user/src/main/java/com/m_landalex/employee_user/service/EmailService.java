package com.m_landalex.employee_user.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.mapper.EmailMapper;
import com.m_landalex.employee_user.persistence.EmailRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class EmailService {

	@Autowired private EmailMapper mapper;
	@Autowired private EmailRepository repository;
	@Autowired private RabbitTemplate rabbitTemplate;
	@Value("${rabbitmq.queueName}") private String queueName;

	public Email save(Email email) throws AsyncXAResourcesException {
		if(email == null) {
			log.error("Error by save from email object");
			rabbitTemplate.convertAndSend(queueName, "error");
			throw new AsyncXAResourcesException("Email object is null -> method save");
		}
		Email newEmail = mapper.toObject(repository.save(mapper.toEntity(email)));
		rabbitTemplate.convertAndSend(queueName, "succesful");
		return newEmail;
	}
	
	@Transactional(readOnly = true)
	public List<Email> fetchAll() {
		return mapper.toObjectList(repository.findAll());
	}
	
	@Transactional(readOnly = true)
	public Email fetchById(Long id) {
		return mapper.toObject(repository.findById(id).get());
	}

	public void deleteById(Long id) {
		repository.delete(repository.findById(id).get());
	}
	
	@Transactional(propagation = Propagation.NEVER)
	public long countAllEmails() {
		return repository.count();
	}
	
}
