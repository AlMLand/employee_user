package com.m_landalex.employee_user.service;

import java.util.List;

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
	private UserMapper userMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private UserRepository userRepository;

	public User save(User user) throws AsyncXAResourcesException {
		if (user == null) {
			throw new AsyncXAResourcesException("Symulation going wrong");
		}
		userRepository.save(userMapper.toEntity(user));
		jmsTemplate.convertAndSend("users", "-->User with username " + user.getUsername() + " is saved ");
		return user;
	}
	
	@Transactional(readOnly = true)
	public List<User> fetchAll(){
		return userMapper.toObjectList(userRepository.findAll());
	}
	
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Transactional(readOnly = true)
	public User fetchById(Long id) {
		return userMapper.toObject(userRepository.findById(id).get());
	}
	
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

}
