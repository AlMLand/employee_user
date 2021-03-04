package com.m_landalex.employee_user.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.employee_user.domain.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

	List<EmployeeEntity> findByFirstName(String firstName);

	List<EmployeeEntity> findByLastName(String lastName);

}
