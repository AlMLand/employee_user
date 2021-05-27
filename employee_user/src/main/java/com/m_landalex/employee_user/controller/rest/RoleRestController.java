package com.m_landalex.employee_user.controller.rest;

import java.util.Collection;

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

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.RoleService;

@RestController
@RequestMapping(value = "/rest/roles")
public class RoleRestController {

	@Autowired
	private RoleService service;
	
	@PostMapping(value = "/")
	@Transactional(rollbackFor = ResponseStatusException.class)
	public Role create(@Valid @RequestBody Role role) {
		try {
			return service.save(role);
		} catch (AsyncXAResourcesException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object ist not correct", e);
		}
	}
	
	@GetMapping(value = "/")
	public Collection<Role> list(){
		return service.fetchAll();
	}
	
	@GetMapping(value = "/{id}")
	public Role findById(@PathVariable Long id) {
		return service.fetchById(id);
	}
	
}
