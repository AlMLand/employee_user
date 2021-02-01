package com.m_landalex.employee_user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.mapper.UserMapper;
import com.m_landalex.employee_user.persistence.UserRepository;

@Transactional
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public User save(User user) throws AsyncXAResourcesException {
		if(user == null) {
			throw new AsyncXAResourcesException("Symulation going wrong");
		}
		jmsTemplate.convertAndSend("users", "User saved:" + user);
		userRepository.save(userMapper.toEntity(user));
		return user;
	}
	
	@Transactional(readOnly = true)
	public User fetchById(Long id) {
		return userMapper.toObject(userRepository.findById(id).orElse(null));
	}
	
}
