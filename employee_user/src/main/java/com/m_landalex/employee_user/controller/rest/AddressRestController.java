package com.m_landalex.employee_user.controller.rest;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.AddressService;

@RestController
@RequestMapping(value = "/rest/addresses")
public class AddressRestController {

	@Autowired
	private AddressService service;

	@PostMapping(value = "/")
	@Transactional(rollbackFor = ResponseStatusException.class)
	public Address create(@Valid @RequestBody Address address) {
		try {
			return service.save(address);
		} catch (AsyncXAResourcesException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object ist not correct", e);
		}
	}

	@GetMapping(value = "/")
	public Collection<Address> list() {
		return service.fetchAll();
	}

	@GetMapping(value = "/{id}")
	public Address findById(@PathVariable Long id) {
		return service.fetchById(id);
	}

	@DeleteMapping(value = "/{id}")
	public void deleteStandingAloneById(@PathVariable Long id) {
		service.deleteById(id);
	}
	
	@GetMapping(value = "/city/{city}")
	public Address findByCity(@PathVariable String city) {
		return service.fetchByCity(city);
	}

}
