package com.m_landalex.employee_user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.employee_user.domain.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

	AddressEntity findByCity(String city);
	
}
