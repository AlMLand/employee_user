package com.m_landalex.employee_user.service.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.m_landalex.employee_user.service.AddressService;
import com.m_landalex.employee_user.service.EmailService;
import com.m_landalex.employee_user.service.EmployeeService;
import com.m_landalex.employee_user.service.UserService;

@Component
@ManagedResource(description = "JMX managed ressource", objectName = "jmxDemo:name=App_employee_user")
public class AppGlobalStatistics {

	@Autowired private EmployeeService employeeService;
	@Autowired private AddressService addressService;
	@Autowired private EmailService emailService;
	@Autowired private UserService userService;

	@ManagedOperation(description = "Quantity of employees in the application")
	public long countAllEmployees() {
		return employeeService.countAll();
	}

	@ManagedOperation(description = "Quantity of users in the application")
	public long countAllUsers() {
		return userService.countAll();
	}

	@ManagedOperation(description = "Quantity of addresses in the application")
	public long countAllAddresses() {
		return addressService.countAll();
	}

	@ManagedOperation(description = "Quantity of emails in the application")
	public long countAllEmails() {
		return emailService.countAll();
	}

	@ManagedOperation(description = "Quantity of objects in the application")
	public long countAllObjects() {
		return countAllEmployees() + countAllUsers() + countAllAddresses() + countAllEmails();
	}

}
