package com.m_landalex.employee_user.domain;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.User;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
	private Email email;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_employee")
	private Address addressData;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User userData;

}