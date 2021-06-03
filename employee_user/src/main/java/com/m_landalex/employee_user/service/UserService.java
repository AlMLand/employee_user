package com.m_landalex.employee_user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

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
			rabbitTemplate.convertAndSend(queueName, "error");
			throw new AsyncXAResourcesException("User object is null -> method save");
		}
		User newUser = mapper.toObject(repository.save(mapper.toEntity(user)));
		rabbitTemplate.convertAndSend(queueName, "succesful");
		jmsTemplate.convertAndSend("users", "-->User with username " + user.getUsername() + " is saved ");
		return newUser;
	}

	@Transactional(readOnly = true)
	public Collection<User> fetchAll() {
		Optional<Collection<User>> optional = Optional.of(mapper.toObjectList(repository.findAll()));
		if(optional.isPresent()) {
			return optional.stream().flatMap(collection -> collection.stream()).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Transactional(readOnly = true)
	public User fetchById(Long id) {
		Optional<User> optional = Optional.of(mapper.toObject(repository.findById(id).get()));
		if(optional.isPresent()) {
			return optional.get();
		}
		return new User();
	}

	@Transactional(propagation = Propagation.NEVER)
	public Long countAll() {
		return repository.count();
	}
	
	public User fetchUserByUsername(String username) {
		Optional<User> optional = Optional.of(mapper.toObject(repository.findByUsername(username)));
		if(optional.isPresent()) {
			return optional.get();
		}
		return new User();
	}

}
