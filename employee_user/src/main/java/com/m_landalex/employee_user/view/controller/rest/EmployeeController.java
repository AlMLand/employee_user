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

import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmployeeService;

@RestController
@RequestMapping(value = "/rest/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService service;

	@PostMapping(value = "/")
	public Employee create(@Valid @RequestBody Employee employee) throws AsyncXAResourcesException {
		return service.save(employee);
	}

	@GetMapping(value = "/")
	public List<Employee> list() {
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
