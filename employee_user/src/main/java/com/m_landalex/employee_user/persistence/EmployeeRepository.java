package com.m_landalex.employee_user.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.employee_user.data.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findByFirstName(String firstName);
	List<Employee> findByLastName(String lastName);
	
}
