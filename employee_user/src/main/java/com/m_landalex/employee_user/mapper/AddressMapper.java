package com.m_landalex.employee_user.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.domain.AddressEntity;

@Component("addressMapper")
public class AddressMapper extends AbstractMapper<AddressEntity, Address> {

	@Autowired
	public AddressMapper() {
		super(AddressEntity.class, Address.class);
	}

}
