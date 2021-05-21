package com.m_landalex.employee_user.controller.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.UserService;

@RestController
@RequestMapping(value = "/rest/users")
public class UserRestController {

	@Autowired
	private UserService service;

	@PostMapping(value = "/")
	@Transactional(rollbackFor = ResponseStatusException.class)
	public User create(@Valid @RequestBody User user) {
		try {
			return service.save(user);
		} catch (AsyncXAResourcesException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object ist not correct", e);
		}
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
