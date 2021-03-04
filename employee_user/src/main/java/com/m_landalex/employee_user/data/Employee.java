package com.m_landalex.employee_user.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Employee extends AbstractObject implements Serializable {

	private String firstName;
	private String lastName;
	private int age;
	private BigDecimal salary;
	private Email email;
	private Address addressData;
	private User userData;

	@Override
	public String toString() {
		return "Employee [" + super.toString() + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age
				+ ", salary=" + salary + "]" + ", email=" + email + ", addressData=" + addressData + ", userData="
				+ userData + "]";
	}

}
