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

import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.UserService;

@RestController
@RequestMapping(value = "users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(value = "/")
	public User createUser(@RequestBody User user) throws AsyncXAResourcesException {
		return userService.save(user);
	}

	@GetMapping(value = "/")
	public List<User> fetchAllUsers() {
		return userService.fetchAll();
	}

	@DeleteMapping(value = "/")
	public void deleteAllUsers() {
		userService.deleteAll();
	}

	@PutMapping(value = "/{id}")
	public User updateUserById(@PathVariable Long id, @RequestBody User user) throws AsyncXAResourcesException {
		return userService.save(user);
	}

	@GetMapping(value = "/{id}")
	public User fetchUserById(@PathVariable Long id) {
		return userService.fetchById(id);
	}

	@DeleteMapping(value = "/{id}")
	public void deleteUserById(@PathVariable Long id) {
		userService.deleteById(id);
	}

}
