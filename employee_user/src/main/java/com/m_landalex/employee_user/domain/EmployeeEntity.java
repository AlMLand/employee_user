package com.m_landalex.employee_user.domain;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employee")
public class EmployeeEntity extends AbstractEntity {

	private String firstName;
	private String lastName;
	private int age;
	private BigDecimal salary;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "email_id", referencedColumnName = "id")
	private EmailEntity email;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_employee")
	private AddressEntity addressData;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserEntity userData;

}
