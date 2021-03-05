package com.m_landalex.employee_user.view.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.service.EmailService;

@RestController
@RequestMapping(value = "/emails")
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@PostMapping(value = "/")
	public Email createEmail(@RequestBody Email email) {
		emailService.save(email);
		return email;
	}
	
	@GetMapping(value = "/")
	public List<Email> fetchAllEmails(){
		return emailService.fetchAll();
	}
	
	@PutMapping(value = "/{id}")
	public Email updateEmailById(@PathVariable Long id, @RequestBody Email email ) {
		emailService.update(email);
		return email;
	}
	
	@GetMapping(value = "/{id}")
	public Email fetchEmailById(@PathVariable Long id) {
		return emailService.fetchById(id);
	}
	
	@DeleteMapping(value = "/{id}")
	public void deleteStandingAloneEmailById(@PathVariable Long id) {
		emailService.deleteById(id);
	}
	
}
