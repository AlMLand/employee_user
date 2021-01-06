package com.m_landalex.employee_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.mapper.EmployeeMapper;
import com.m_landalex.employee_user.persistence.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Transactional
	public Employee save(Employee employee) {
		employeeRepository.save(employeeMapper.toEntity(employee));
		return employee;
	}
	
	@Transactional(readOnly = true)
	public Employee fetchById(Long id) {
		Employee returnedEmployee = employeeMapper.toObject(employeeRepository.findById(id).orElse(null));
		return returnedEmployee;
	}
	
	@Transactional(readOnly = true)
	public List<Employee> fetchByFirstName(String firstName){
		List<Employee> returnedList = employeeMapper.toObjectList(employeeRepository.findByFirstName(firstName));
		return returnedList;
	}
	
	@Transactional(readOnly = true)
	public List<Employee> fetchByLastName(String lastName){
		List<Employee> returnedList = employeeMapper.toObjectList(employeeRepository.findByLastName(lastName));
		return returnedList;
	}
	
}
