package com.m_landalex.employee_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.domain.AddressEntity;
import com.m_landalex.employee_user.mapper.AddressMapper;
import com.m_landalex.employee_user.persistence.AddressRepository;

@Transactional
@Service
public class AddressService {

	@Autowired 	private AddressRepository addressRepository;
	@Autowired 	private AddressMapper addressMapper;

	public Address save(Address address) {
		addressRepository.save(addressMapper.toEntity(address));
		return address;
	}

	@Transactional(readOnly = true)
	public List<Address> fetchAll() {
		return addressMapper.toObjectList(addressRepository.findAll());
	}

	@Transactional(readOnly = true)
	public Address fetchById(Long id) {
		return addressMapper.toObject(addressRepository.findById(id).get());
	}

	/*
	 * only addresses that are not related to any are employee
	 */
	public void deleteById(Long id) {
		addressRepository.delete(addressRepository.findById(id).get());
	}

	public Address update(Address address) {
		AddressEntity returnedAddressEntity = addressRepository.findById(address.getId()).get();
		returnedAddressEntity.setStreet(addressMapper.toEntity(address).getStreet());
		returnedAddressEntity.setHouseNumber(addressMapper.toEntity(address).getHouseNumber());
		returnedAddressEntity.setCity(addressMapper.toEntity(address).getCity());
		returnedAddressEntity.setPostCode(addressMapper.toEntity(address).getPostCode());
		addressRepository.save(returnedAddressEntity);
		return address;
	}

}
