package com.m_landalex.employee_user.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.mapper.UserMapper;
import com.m_landalex.employee_user.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class UserService {

	@Autowired private UserMapper mapper;
	@Autowired private JmsTemplate jmsTemplate;
	@Autowired private UserRepository repository;
	@Autowired private RabbitTemplate rabbitTemplate;
	@Value("${rabbitmq.queueName}") private String queueName;

	public User save(User user) throws AsyncXAResourcesException {
		if (user == null) {
			log.error("Error by save from user object");
			rabbitTemplate.convertAndSend(queueName, "error by save from user object");
			throw new AsyncXAResourcesException("User object is null -> method save");
		}
		User newUser = mapper.toObject(repository.save(mapper.toEntity(user)));
		rabbitTemplate.convertAndSend(queueName, "succesful by save from email object");
		jmsTemplate.convertAndSend("users", "-->User with username " + user.getUsername() + " is saved ");
		return newUser;
	}

	@Transactional(readOnly = true)
	public List<User> fetchAll() {
		return mapper.toObjectList(repository.findAll());
	}

	@Transactional(readOnly = true)
	public User fetchById(Long id) {
		return mapper.toObject(repository.findById(id).get());
	}

	@Transactional(propagation = Propagation.NEVER)
	public Long countAllUsers() {
		return repository.count();
	}

}
