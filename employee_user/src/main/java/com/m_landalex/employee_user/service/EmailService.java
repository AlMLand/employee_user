package com.m_landalex.employee_user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public Collection<Email> fetchAll() {
		Optional<Collection<Email>> optional = Optional.of(mapper.toObjectList(repository.findAll()));
		if(optional.isPresent()) {
			return optional.stream().flatMap(collection -> collection.stream()).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
	
	@Transactional(readOnly = true)
	public Email fetchById(Long id) {
		Optional<Email> optional = Optional.of(mapper.toObject(repository.findById(id).get()));
		if(optional.isPresent()) {
			return optional.get();
		}
		return new Email();
	}

	public void deleteById(Long id) {
		repository.delete(repository.findById(id).get());
	}
	
	@Transactional(propagation = Propagation.NEVER)
	public long countAll() {
		return repository.count();
	}
	
}
