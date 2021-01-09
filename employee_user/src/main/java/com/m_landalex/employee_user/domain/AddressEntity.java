package com.m_landalex.employee_user.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

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

	private String street;
	private int houseNumber;
	private String city;
	private String postCode;
	@Version
	private int version;
	
	@OneToMany(mappedBy = "addressData")
	private List<EmployeeEntity> addressList;
	
}
