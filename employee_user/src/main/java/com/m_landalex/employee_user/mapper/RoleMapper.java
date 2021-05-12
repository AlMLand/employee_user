package com.m_landalex.employee_user.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.domain.RoleEntity;

@Component
public class RoleMapper extends AbstractMapper<RoleEntity, Role> {

	@Autowired
	public RoleMapper() {
		super(RoleEntity.class, Role.class);
	}

}
