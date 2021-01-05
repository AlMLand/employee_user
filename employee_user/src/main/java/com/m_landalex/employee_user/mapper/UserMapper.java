package com.m_landalex.employee_user.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.domain.UserEntity;

@Component("userMapper")
public class UserMapper extends AbstractMapper<UserEntity, User> {

	@Autowired
	public UserMapper() {
		super(UserEntity.class, User.class);
	}

}
