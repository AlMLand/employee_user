package com.m_landalex.employee_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.mapper.AddressMapper;
import com.m_landalex.employee_user.persistence.AddressRepository;

@Transactional
@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private AddressMapper addressMapper;
	
	public Address save(Address address) {
		addressRepository.save(addressMapper.toEntity(address));
		return address;
	}
	
	@Transactional(readOnly = true)
	public List<Address> fetchAll(){
		return addressMapper.toObjectList(addressRepository.findAll());
	}
	
}
