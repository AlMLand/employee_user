package com.m_landalex.employee_user.view.controller;

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

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.RoleService;

@RestController
@RequestMapping(value = "/rest/roles")
public class RoleController {

	@Autowired
	private RoleService service;
	
	@PostMapping(value = "/")
	public Role create(@Valid @RequestBody Role role) throws AsyncXAResourcesException {
		return service.save(role);
	}
	
	@GetMapping(value = "/")
	public List<Role> list(){
		return service.fetchAll();
	}
	
	@GetMapping(value = "/{id}")
	public Role findById(@PathVariable Long id) {
		return service.fetchById(id);
	}
	
	@DeleteMapping(value = "/{id}")
	public void deleteById(@PathVariable Long id) {
		service.deleteById(id);
	}
	
}
