package com.m_landalex.employee_user.controller.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.AddressService;

@ExtendWith(MockitoExtension.class)
public class AddressRestControllerTest {

	@InjectMocks
	private AddressRestController controller;
	@Mock
	private AddressService service;
	private List<Address> addresses;

	@BeforeEach
	public void setUp() {
		Address address = Address.builder().street("TEST_street_1").houseNumber(10).city("TEST_city_1")
				.postCode("11111").build();
		address.setId(1L);
		addresses = new ArrayList<>();
		addresses.add(address);
	}

	@DisplayName("should return list with size 2 and address with street='TEST_street_2'")
	@Test
	public void create_Test() throws AsyncXAResourcesException {
		var newAddress = Address.builder().street("TEST_street_2").houseNumber(20).city("TEST_city_2")
				.postCode("22222").build();
		when(service.save(newAddress)).thenAnswer(invocation -> {
			addresses.add(newAddress);
			return newAddress;
		});
		var returnedAddress = controller.create(newAddress);

		assertNotNull(returnedAddress);
		assertEquals(2, addresses.size());
		assertEquals("TEST_street_2", returnedAddress.getStreet());
	}

	@DisplayName("when AsyncXAResourcesException.class, then throw ResponseStatusException.class")
	@Test
	public void create_Test2() throws AsyncXAResourcesException {
		Address newAddress = null;
		when(service.save(newAddress)).thenThrow(AsyncXAResourcesException.class);
		
		assertThrows(ResponseStatusException.class, () -> {controller.create(newAddress);});
	}

	@DisplayName("should return list with size 1")
	@Test
	public void list_Test() {
		when(service.fetchAll()).thenReturn(addresses);
		var returnList = controller.list();
		
		assertNotNull(returnList);
		assertEquals(1, returnList.size());
	}

	@DisplayName("should return address with city='TEST_city_1', postcode='11111', street='TEST_street_1'")
	@Test
	public void findByIdT_est() {
		when(service.fetchById(anyLong())).thenReturn(addresses.get(0));
		var returnedAddress = controller.findById(anyLong());
		
		assertNotNull(returnedAddress);
		assertEquals("TEST_city_1", returnedAddress.getCity());
		assertEquals(Integer.valueOf(10), returnedAddress.getHouseNumber());
		assertEquals("11111", returnedAddress.getPostCode());
		assertEquals("TEST_street_1", returnedAddress.getStreet());
	}
	
	@DisplayName("should return list with size 0")
	@Test
	public void deleteStandingAloneById_Test() {
		doAnswer(invocation -> addresses.remove(0)).when(service).deleteById(anyLong());
		
		controller.deleteStandingAloneById(anyLong());
		
		assertNotNull(addresses);
		assertEquals(0, addresses.size());
	}

}
