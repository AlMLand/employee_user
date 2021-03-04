package com.m_landalex.employee_user.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Address extends AbstractObject {

	private String street;
	private int houseNumber;
	private String city;
	private String postCode;

	@Override
	public String toString() {
		return "[" + super.toString() + ", street=" + street + ", houseNumber=" + houseNumber + ", city=" + city
				+ ", postCode=" + postCode + "]";
	}

}
