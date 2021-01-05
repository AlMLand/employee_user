package com.m_landalex.employee_user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.employee_user.data.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {

}
