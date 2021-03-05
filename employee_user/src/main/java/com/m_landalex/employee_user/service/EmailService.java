package com.m_landalex.employee_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.domain.EmailEntity;
import com.m_landalex.employee_user.mapper.EmailMapper;
import com.m_landalex.employee_user.persistence.EmailRepository;

@Transactional
@Service
public class EmailService {

	@Autowired 	private EmailRepository emailRepository;
	@Autowired 	private EmailMapper emailMapper;

	public Email save(Email email) {
		emailRepository.save(emailMapper.toEntity(email));
		return email;
	}
	
	public Email update(Email email) {
		EmailEntity returnedEmailEntity = emailRepository.findById(email.getId()).get();
		returnedEmailEntity.setEmail(emailMapper.toEntity(email).getEmail());
		emailRepository.save(returnedEmailEntity);
		return email;
	}

	@Transactional(readOnly = true)
	public List<Email> fetchAll() {
		return emailMapper.toObjectList(emailRepository.findAll());
	}
	
	@Transactional(readOnly = true)
	public Email fetchById(Long id) {
		return emailMapper.toObject(emailRepository.findById(id).get());
	}

	public void deleteById(Long id) {
		emailRepository.delete(emailRepository.findById(id).get());
	}
	
}
