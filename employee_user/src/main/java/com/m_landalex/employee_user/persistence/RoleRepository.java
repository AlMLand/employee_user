package com.m_landalex.employee_user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.employee_user.domain.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

}
