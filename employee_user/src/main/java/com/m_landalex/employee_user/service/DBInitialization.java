package com.m_landalex.employee_user.service;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;

@Service
public class DBInitialization {

	@Autowired
	private EmployeeService employeeService;
	
	@PostConstruct
	public void setupInitialization() {
		
		Email email =  Email.builder().email("morlandalex@googlemail.com").build();
		Address address = new Address("Elsasserstrasse", 10, "Dresden", 01307);
		User user = User.builder().username("A_A_A").password("B_B_B").userRole(Role.DEVELOPMENT).build();
		
		Employee employee = Employee.builder()
				.firstName("Alex")
				.lastName("Morland")
				.age(35)
				.salary(new BigDecimal(3000.00d))
				.email(email)
				.addressData(address)
				.userData(user)
				.build();
		employeeService.save(employee);
		
	}
	
}
