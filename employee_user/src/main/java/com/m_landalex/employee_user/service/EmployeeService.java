package com.m_landalex.employee_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.mapper.EmployeeMapper;
import com.m_landalex.employee_user.persistence.EmployeeRepository;

@Transactional
@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public Employee save(Employee employee) throws AsyncXAResourcesException {
		if (employee == null) {
			throw new AsyncXAResourcesException("Symulation going wrong");
		}
		jmsTemplate.convertAndSend("employees", "Employee saved:" + employee);
		employeeRepository.save(employeeMapper.toEntity(employee));
		return employee;
	}
	
	@Transactional(propagation = Propagation.NEVER)
	public long countAllEmployees() {
		return employeeRepository.count();
	}
	
	@Transactional(readOnly = true)
	public List<Employee> fetchAll(){
		return employeeMapper.toObjectList(employeeRepository.findAll());
	}
	
	@Transactional(readOnly = true)
	public Employee fetchById(Long id) {
		return employeeMapper.toObject(employeeRepository.findById(id).orElse(null));
	}
	
	@Transactional(readOnly = true)
	public List<Employee> fetchByFirstName(String firstName){
		return employeeMapper.toObjectList(employeeRepository.findByFirstName(firstName));
	}
	
	@Transactional(readOnly = true)
	public List<Employee> fetchByLastName(String lastName){
		return employeeMapper.toObjectList(employeeRepository.findByLastName(lastName));
	}
	
}
