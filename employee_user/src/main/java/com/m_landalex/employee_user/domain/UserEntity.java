package com.m_landalex.employee_user.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "users")
public class UserEntity extends AbstractEntity {

	@Column(name = "USERNAME")
	private String username;
	@Column(name = "PASSWORD")
	private String password;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles",
				joinColumns = {@JoinColumn(name = "USER_FK")},
				inverseJoinColumns = {@JoinColumn(name = "ROLE_FK")})
	private Collection<RoleEntity> userRoles;
	
	@OneToOne(mappedBy = "userData")
	private EmployeeEntity employee;

}
