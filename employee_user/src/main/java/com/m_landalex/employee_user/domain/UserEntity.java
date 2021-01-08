package com.m_landalex.employee_user.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class UserEntity extends AbstractEntity {

	private String username;
	private String password;
	private String userRole;
	
	@OneToOne(mappedBy = "userData")
	private EmployeeEntity employee;
	
}
