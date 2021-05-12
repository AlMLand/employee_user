package com.m_landalex.employee_user.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
@Table(name = "address")
public class AddressEntity extends AbstractEntity {

	@Column(name = "STREET")
	private String street;
	@Column(name = "HOUSENUMBER")
	private int houseNumber;
	@Column(name = "CITY")
	private String city;
	@Column(name = "POSTCODE")
	private String postCode;

	@OneToMany(mappedBy = "addressData")
	private List<EmployeeEntity> addressList;

}
