package com.m_landalex.employee_user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.employee_user.data.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
