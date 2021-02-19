package com.m_landalex.employee_user.jms;

import javax.jms.JMSException;
import javax.jms.Message;

public interface MessageListener {

	void onMessage(Message message) throws JMSException;
	
}
