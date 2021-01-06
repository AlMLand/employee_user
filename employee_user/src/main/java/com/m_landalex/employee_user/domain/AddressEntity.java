package com.m_landalex.employee_user.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.m_landalex.employee_user.data.Address;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "address")
public class AddressEntity extends AbstractEntity {

	private String street;
	private int houseNumber;
	private String city;
	private int postCode;
	
	@OneToMany(mappedBy = "addressData")
	private List<Address> addressList;
	
}
