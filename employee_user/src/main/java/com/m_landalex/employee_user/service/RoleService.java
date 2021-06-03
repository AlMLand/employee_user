package com.m_landalex.employee_user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.mapper.RoleMapper;
import com.m_landalex.employee_user.persistence.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class RoleService {

	@Autowired private RoleRepository repository;
	@Autowired private RoleMapper mapper;
	@Autowired private RabbitTemplate rabbitTemplate;
	@Value("${rabbitmq.queueName}")	private String queueName;
	
	public Role save(Role role) throws AsyncXAResourcesException {
		if(role == null) {
			log.error("Error by save from role object");
			rabbitTemplate.convertAndSend(queueName, "error");
			throw new AsyncXAResourcesException("Role object is null -> method save");
		}
		Role newRole = mapper.toObject(repository.save(mapper.toEntity(role)));
		rabbitTemplate.convertAndSend(queueName, "succesful");
		return newRole;
	}
	
	@Transactional(readOnly = true)
	public Collection<Role> fetchAll() {
		Optional<Collection<Role>> optional = Optional.of(mapper.toObjectList(repository.findAll()));
		if(optional.isPresent()) {
			return optional.stream()
					.flatMap(collection -> collection.stream()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		}
		return new ArrayList<>();
	}
	
	@Transactional(propagation = Propagation.NEVER)
	public long countAll() {
		return repository.count();
	}
	
	@Transactional(readOnly = true)
	public Role fetchById(Long id) {
		Optional<Role> optional = Optional.of(mapper.toObject(repository.findById(id).get()));
		if(optional.isPresent()) {
			return optional.get();
		}
		return new Role();
	}
	
}
