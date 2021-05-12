package com.m_landalex.employee_user.view.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.UserService;

@RestController
@RequestMapping(value = "/rest/users")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping(value = "/")
	public User create(@Valid @RequestBody User user) throws AsyncXAResourcesException {
		return service.save(user);
	}

	@GetMapping(value = "/")
	public List<User> list() {
		return service.fetchAll();
	}

	@GetMapping(value = "/{id}")
	public User findById(@PathVariable Long id) {
		return service.fetchById(id);
	}

}
