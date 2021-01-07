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
		
		employeeService.save(Employee.builder()
				.firstName("Alex")
				.lastName("Morland")
				.age(35)
				.salary(new BigDecimal(3000.00d))
				.email(Email.builder().email("morlandalex@googlemail.com").build())
				.addressData(Address.builder().street("Elsasserstrasse").houseNumber(10).city("Dresden").postCode(01307).build())
				.userData(User.builder().username("A_A_A").password("B_B_B").userRole(Role.DEVELOPMENT).build())
				.build());
		
	}
	
}
