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

import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmployeeService;

@RestController
@RequestMapping(value = "/rest/employees")
public class EmployeeRestController {

	@Autowired
	private EmployeeService service;

	@PostMapping(value = "/")
	@Transactional(rollbackFor = ResponseStatusException.class)
	public Employee create(@Valid @RequestBody Employee employee) {
		try {
			return service.save(employee);
		} catch (AsyncXAResourcesException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object ist not correct", e);
		}
	}

	@GetMapping(value = "/")
	public Collection<Employee> list() {
		return service.fetchAll();
	}

	@DeleteMapping(value = "/")
	public void deleteAll() {
		service.deleteAll();
	}

	@GetMapping(value = "/{id}")
	public Employee findById(@PathVariable Long id) {
		return service.fetchById(id);
	}

	@DeleteMapping(value = "/{id}")
	public void deleteById(@PathVariable Long id) {
		service.deleteById(id);
	}

}
