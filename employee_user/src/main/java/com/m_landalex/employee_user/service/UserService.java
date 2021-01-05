package com.m_landalex.employee_user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.mapper.UserMapper;
import com.m_landalex.employee_user.persistence.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper userMapper;
	
	@Transactional(readOnly = true)
	public User fetchById(Long id) {
		User returnedUser = userMapper.toObject(userRepository.findById(id).orElse(null));
		return returnedUser;
	}
	
}
