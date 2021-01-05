package com.m_landalex.employee_user.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.domain.EmployeeEntity;

@Component("employeeMapper")
public class EmployeeMapper extends AbstractMapper<EmployeeEntity, Employee> {

	@Autowired
	public EmployeeMapper() {
		super(EmployeeEntity.class, Employee.class);
	}

}
