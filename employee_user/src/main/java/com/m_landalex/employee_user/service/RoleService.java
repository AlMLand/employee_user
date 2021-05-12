package com.m_landalex.employee_user.service;

import java.util.List;

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
			rabbitTemplate.convertAndSend(queueName, "error by save from role object");
			throw new AsyncXAResourcesException("Role object is null -> method save");
		}
		Role newRole = mapper.toObject(repository.save(mapper.toEntity(role)));
		rabbitTemplate.convertAndSend(queueName, "succesful by save from role object");
		return newRole;
	}
	
	@Transactional(readOnly = true)
	public List<Role> fetchAll() {
		return mapper.toObjectList(repository.findAll());
	}
	
	@Transactional(propagation = Propagation.NEVER)
	public long countAllRoles() {
		return repository.count();
	}
	
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public Role fetchById(Long id) {
		return mapper.toObject(repository.findById(id).get());
	}
	
}
