package com.m_landalex.employee_user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.employee_user.data.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
