package com.m_landalex.employee_user.data;

import java.util.List;

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
public class Address extends AbstractObject {

	private String street;
	private int houseNumber;
	private String city;
	private int postCode;
	private List<Address> addressList;

	public Address(String street, int houseNumber, String city, int postCode) {
		super();
		this.street = street;
		this.houseNumber = houseNumber;
		this.city = city;
		this.postCode = postCode;
		this.addressList.add(this);
	}
	
}