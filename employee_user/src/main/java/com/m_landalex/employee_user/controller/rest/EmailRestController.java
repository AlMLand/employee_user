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

import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmailService;

@RestController
@RequestMapping(value = "/rest/emails")
public class EmailRestController {

	@Autowired
	private EmailService service;
	
	@PostMapping(value = "/")
	@Transactional(rollbackFor = ResponseStatusException.class)
	public Email create(@Valid @RequestBody Email email) {
		try {
			service.save(email);
		} catch (AsyncXAResourcesException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object ist not correct", e);
		}
		return email;
	}
	
	@GetMapping(value = "/")
	public Collection<Email> list(){
		return service.fetchAll();
	}
	
	@GetMapping(value = "/{id}")
	public Email findById(@PathVariable Long id) {
		return service.fetchById(id);
	}
	
	@DeleteMapping(value = "/{id}")
	public void deleteStandingAloneById(@PathVariable Long id) {
		service.deleteById(id);
	}
	
}
