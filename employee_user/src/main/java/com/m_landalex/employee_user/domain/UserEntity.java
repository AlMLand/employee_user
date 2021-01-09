package com.m_landalex.employee_user.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.m_landalex.employee_user.data.Role;

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
	@Enumerated(EnumType.STRING)
	private Role userRole;
	@Version
	private int version;
	
	@OneToOne(mappedBy = "userData")
	private EmployeeEntity employee;
	
}
