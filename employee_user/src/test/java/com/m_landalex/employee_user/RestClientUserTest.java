package com.m_landalex.employee_user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.client.RestTemplate;

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class RestClientUserTest {

	private static final String URL_CREATE_USER = "http://localhost:8080/users/";
	private static final String URL_GET_ALL_USER = "http://localhost:8080/users/";
	private static final String URL_PUT_USER_BY_ID = "http://localhost:8080/users/{id}";
	private static final String URL_GET_USER_BY_ID = "http://localhost:8080/users/{id}";
	
	private RestTemplate restTemplate;
	
	@BeforeEach
	public void setUp() {
		restTemplate = new RestTemplate();
		log.info("Test started...");
	}
	
	@AfterEach
	public void tearDown() {
		log.info("Test ended...");
	}
	
	@Test
	@Order(4)
	public void testCreateUser() {
		restTemplate.postForObject(URL_CREATE_USER, User.builder()
				.username("TestUN").password("TestPW").userRole(Role.DEVELOPMENT).build(), User.class);
		User[] returnedArray = restTemplate.getForObject(URL_GET_ALL_USER, User[].class);
		assertEquals(3, returnedArray.length);
	}
	
	@Test
	@Order(1)
	public void testFetchAllUsers() {
		User[] returnedArray = restTemplate.getForObject(URL_GET_ALL_USER, User[].class);
		assertEquals(2, returnedArray.length);
	}
	
	@Test
	@Order(3)
	public void testUpdateUserById() {
		User returnedUser = restTemplate.getForObject(URL_GET_USER_BY_ID, User.class, 1);
		returnedUser.setPassword("TestPW");
		restTemplate.put(URL_PUT_USER_BY_ID, returnedUser, 1);
		returnedUser = restTemplate.getForObject(URL_GET_USER_BY_ID, User.class, 1);
		assertEquals("TestPW", returnedUser.getPassword());
	}
	
	@Test
	@Order(2)
	public void testFetchUserById() {
		User returneduser = restTemplate.getForObject(URL_GET_USER_BY_ID, User.class, 1);
		assertEquals("A_A_A", returneduser.getUsername());
	}
	
}
