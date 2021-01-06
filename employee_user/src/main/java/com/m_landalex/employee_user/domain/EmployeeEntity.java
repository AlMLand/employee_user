package com.m_landalex.employee_user.domain;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employee")
public class EmployeeEntity extends AbstractEntity {

	@Setter
	private String firstName;
	@Setter
	private String lastName;
	@Setter
	private int age;
	@Setter
	private BigDecimal salary;
	
	@Setter
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "email_id", referencedColumnName = "id")
	private EmailEntity email;

	@Setter
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_employee")
	private AddressEntity addressData;
	
	@Setter
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserEntity userData;

}
