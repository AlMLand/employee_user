package com.m_landalex.employee_user.data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Employee extends AbstractObject {

	private String firstName;
	private String lastName;
	private int age;
	private BigDecimal salary;
	private Email email;
	private Address addressData;
	private User userData;
	
}
