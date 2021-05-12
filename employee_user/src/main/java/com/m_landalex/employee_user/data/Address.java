package com.m_landalex.employee_user.data;

import static java.util.Comparator.*;

import java.util.Comparator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class Address extends AbstractObject implements Comparable<Address> {

	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	@Size(min = 2, max = 100, message = "{javax.validation.constraints.Size.message}")
	private String street;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}")
	private int houseNumber;
	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	@Size(min = 2, max = 100, message = "{javax.validation.constraints.Size.message}")
	private String city;
	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	@Size(min = 2, max = 50, message = "{javax.validation.constraints.Size.message}")
	private String postCode;

	private static final Comparator<Address> COMPARATOR_ADDRESS = comparing(Address::getCity)
			.thenComparing(Address::getPostCode).thenComparing(Address::getStreet);
	
	@Override
	public int compareTo(Address o) {
		return COMPARATOR_ADDRESS.compare(this, o);
	}

}
