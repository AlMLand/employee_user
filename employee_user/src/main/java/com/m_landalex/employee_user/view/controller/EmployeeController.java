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

import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmployeeService;

@RestController
@RequestMapping(value = "/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping(value = "/")
	public Employee createEmployee(@RequestBody Employee employee) throws AsyncXAResourcesException {
		return employeeService.save(employee);
	}

	@GetMapping(value = "/")
	public List<Employee> fetchAllEmployees() {
		return employeeService.fetchAll();
	}

	@DeleteMapping(value = "/")
	public void deleteAllEmployees() {
		employeeService.deleteAll();
	}

	@GetMapping(value = "/{id}")
	public Employee fetchEmployeeById(@PathVariable Long id) {
		return employeeService.fetchById(id);
	}

	@PutMapping(value = "/{id}")
	public void updateEmployeeById(@PathVariable Long id, @RequestBody Employee employee)
			throws AsyncXAResourcesException {
		employeeService.save(employee);
	}

	@DeleteMapping(value = "/{id}")
	public void deleteEmployeeById(@PathVariable Long id) {
		employeeService.deleteById(id);
	}

}
