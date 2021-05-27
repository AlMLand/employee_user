package com.m_landalex.employee_user.service;

import java.util.Collection;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.mapper.AddressMapper;
import com.m_landalex.employee_user.persistence.AddressRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class AddressService {

	@Autowired private AddressMapper mapper;
	@Autowired private AddressRepository repository;
	@Autowired private RabbitTemplate rabbitTemplate;
	@Value("${rabbitmq.queueName}") private String queueName;

	public Address save(Address address) throws AsyncXAResourcesException {
		if(address == null) {
			log.error("Error by save from address object");
			rabbitTemplate.convertAndSend(queueName, "error");
			throw new AsyncXAResourcesException("Address object is null -> method save");
		}
		Address newAddress = mapper.toObject(repository.save(mapper.toEntity(address)));
		rabbitTemplate.convertAndSend(queueName, "succesful");
		return newAddress;
	}

	@Transactional(readOnly = true)
	public Collection<Address> fetchAll() {
		return mapper.toObjectList(repository.findAll());
	}

	@Transactional(readOnly = true)
	public Address fetchById(Long id) {
		return mapper.toObject(repository.findById(id).get());
	}

	/*
	 * only addresses that are not related to any are employee
	 */
	public void deleteById(Long id) {
		repository.delete(repository.findById(id).get());
	}
	
	@Transactional(propagation = Propagation.NEVER)
	public long countAll() {
		return repository.count();
	}
	
	@Transactional(readOnly = true)
	public Address fetchByCity(String city) {
		return mapper.toObject(repository.findByCity(city));
	}

}
