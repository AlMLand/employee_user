package com.m_landalex.employee_user.view.controller.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmailService;

@RestController
@RequestMapping(value = "/rest/emails")
public class EmailController {

	@Autowired
	private EmailService service;
	
	@PostMapping(value = "/")
	public Email create(@Valid @RequestBody Email email) throws AsyncXAResourcesException {
		service.save(email);
		return email;
	}
	
	@GetMapping(value = "/")
	public List<Email> list(){
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
