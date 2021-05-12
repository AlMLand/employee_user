package com.m_landalex.employee_user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.employee_user.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByUsername(String username);
	
}
