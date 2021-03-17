package com.m_landalex.employee_user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.client.RestTemplate;

import com.m_landalex.employee_user.data.Email;

@TestMethodOrder(OrderAnnotation.class)
public class RestClientEmailTest {

	private RestTemplate restTemplate;
	private static final String URL_CREATE_EMAIL = "http://localhost:8080/emails/";
	private static final String URL_GET_ALL_EMAILS = "http://localhost:8080/emails/";
	private static final String URL_PUT_EMAIL_BY_ID = "http://localhost:8080/emails/{id}";
	private static final String URL_GET_EMAIL_BY_ID = "http://localhost:8080/emails/{id}";
	private static final String URL_DELETE_EMAIL_BY_ID = "http://localhost:8080/emails/{id}";
	
	@BeforeEach
	public void setUp() {
		restTemplate = new RestTemplate();
	}
	
	@AfterEach
	public void tearDown() {
	}
	
	@Test
	@Order(1)
	public void testFetchAllEmails() {
		Email[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMAILS, Email[].class);
		assertEquals(2, returnedArray.length);
	}
	
	@Test
	@Order(2)
	public void testFetchEmailById() {
		Email returnedEmail = restTemplate.getForObject(URL_GET_EMAIL_BY_ID, Email.class, 1);
		assertEquals("mcgregor@googlemail.com", returnedEmail.getEmail());
	}
	
	@Test
	@Order(3)
	public void testUpdateEmailById() {
		Email returnedEmail = restTemplate.getForObject(URL_GET_EMAIL_BY_ID, Email.class, 1);
		returnedEmail.setEmail("test@gmail.com");
		restTemplate.put(URL_PUT_EMAIL_BY_ID, returnedEmail, 1);
		returnedEmail = restTemplate.getForObject(URL_GET_EMAIL_BY_ID, Email.class, 1);
		assertEquals("test@gmail.com", returnedEmail.getEmail());
	}
	
	@Test
	@Order(4)
	public void testCreateEmail() {
		Email newEmail = Email.builder().email("testCreateEmail@gmail.com").build();
		restTemplate.postForObject(URL_CREATE_EMAIL, newEmail, Email.class);
		Email returnedEmail = restTemplate.getForObject(URL_GET_EMAIL_BY_ID, Email.class, 3);
		assertEquals("testCreateEmail@gmail.com", returnedEmail.getEmail());
	}
	
	@Test
	@Order(5)
	public void testDeleteStandingAloneEmailById() {
		restTemplate.delete(URL_DELETE_EMAIL_BY_ID, 3);
		Email[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMAILS, Email[].class);
		assertEquals(2, returnedArray.length);
	}
	
}
