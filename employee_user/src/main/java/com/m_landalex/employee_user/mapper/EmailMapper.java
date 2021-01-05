package com.m_landalex.employee_user.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.domain.EmailEntity;

@Component("emailMapper")
public class EmailMapper extends AbstractMapper<EmailEntity, Email> {

	@Autowired
	public EmailMapper() {
		super(EmailEntity.class, Email.class);
	}

}
