package com.m_landalex.employee_user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.employee_user.domain.EmailEntity;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

}
