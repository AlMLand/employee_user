package com.m_landalex.employee_user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.client.RestTemplate;

import com.m_landalex.employee_user.data.Address;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class RestClientAddressTest {

	private RestTemplate restTemplate;
	private static final String URL_CREATE_ADDRESS = "http://localhost:8080/addresses/";
	private static final String URL_GET_ALL_ADDRESS = "http://localhost:8080/addresses/";
	private static final String URL_GET_ADDRESS_BY_ID = "http://localhost:8080/addresses/{id}";
	private static final String URL_PUT_ADDRESS_BY_ID = "http://localhost:8080/addresses/{id}";
	private static final String URL_DELETE_STANDING_ALONE_ADDRESS_BY_ID = "http://localhost:8080/addresses/{id}";
	
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
	@Order(3)
	public void testCreateAddress() {
		Address newAddress = Address.builder().street("TESTstreet").houseNumber(10).city("TESTcity").postCode("TEST000").build();
		restTemplate.postForObject(URL_CREATE_ADDRESS, newAddress, Address.class);
		Address returnedAddress = restTemplate.getForObject(URL_GET_ADDRESS_BY_ID, Address.class, 3);
		assertEquals("TESTstreet", returnedAddress.getStreet());
	}
	
	@Test
	@Order(2)
	public void testFetchAddressById() {
		Address returnedAddress = restTemplate.getForObject(URL_GET_ADDRESS_BY_ID, Address.class, 1);
		assertEquals("Dublin", returnedAddress.getCity());
	}
	
	@Test
	@Order(1)
	public void testFetchAllAddresses() {
		Address[] returnedArray = restTemplate.getForObject(URL_GET_ALL_ADDRESS, Address[].class);
		assertEquals(2, returnedArray.length);
	}
	
	@Test
	@Order(4)
	public void testDeleteStandingAloneAddressById() {
		restTemplate.delete(URL_DELETE_STANDING_ALONE_ADDRESS_BY_ID, 3);
		Address[] returnedArray = restTemplate.getForObject(URL_GET_ALL_ADDRESS, Address[].class);
		assertEquals(2, returnedArray.length);
	}
	
	@Test
	@Order(5)
	public void testUpdateAddressById() {
		Address returnedAddress = restTemplate.getForObject(URL_GET_ADDRESS_BY_ID, Address.class, 1);
		returnedAddress.setCity("UpdatedCity");
		restTemplate.put(URL_PUT_ADDRESS_BY_ID, returnedAddress, 1);
		returnedAddress = restTemplate.getForObject(URL_GET_ADDRESS_BY_ID, Address.class, 1);
		assertEquals("UpdatedCity", returnedAddress.getCity());
	}
	
}
