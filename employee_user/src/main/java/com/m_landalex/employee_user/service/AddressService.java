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
		assert address != null : "AddressService.class, method save address is null";
		Address newAddress = mapper.toObject(repository.save(mapper.toEntity(address)));
		assert newAddress != null : "AddressService.class, method save newAddress is null";
		rabbitTemplate.convertAndSend(queueName, "succesful");
		return newAddress;
	}

	@Transactional(readOnly = true)
	public Collection<Address> fetchAll() {
		Optional<Collection<Address>> optional = Optional.of(mapper.toObjectList(repository.findAll()));
		assert optional != null : "AddressService.class, method fetchAll optional is null";
		if(optional.isPresent()) {
			return optional.stream().flatMap(collection -> collection.stream()).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Transactional(readOnly = true)
	public Address fetchById(Long id) {
		assert id != null : "AddressService.class, method fetchById id is null";
		Optional<Address> optional = Optional.of(mapper.toObject(repository.findById(id).get()));
		if(optional.isPresent()) {
			return optional.get();
		}
		return new Address();
	}

	/*
	 * only addresses that are not related to any are employee
	 */
	public void deleteById(Long id) {
		assert id != null : "AddressService.class, method deleteById id is null";
		if(id != null) {
			repository.delete(repository.findById(id).get());
		}
	}
	
	@Transactional(propagation = Propagation.NEVER)
	public long countAll() {
		return repository.count();
	}
	
	@Transactional(readOnly = true)
	public Address fetchByCity(String city) {
		assert city != null : "AddressService.class, method fetchByCity city is null";
		Optional<Address> optional = Optional.of(mapper.toObject(repository.findByCity(city)));
		if(optional.isPresent()) {
			if(optional.stream().allMatch(address -> address.getCity().compareToIgnoreCase(city) == 0)) {
				return optional.get();
			}
		}
		return new Address();
	}

}
