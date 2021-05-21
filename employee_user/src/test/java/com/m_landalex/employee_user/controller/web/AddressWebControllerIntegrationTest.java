package com.m_landalex.employee_user.controller.web;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.service.AddressService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AddressWebController.class)
public class AddressWebControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AddressService service;
	private Address address;
	
	@BeforeEach
	public void setUp() {
		address = Address.builder().street("TEST_STREET").houseNumber(10).city("TEST_HOUSENUMBER")
				.postCode("12345").build();
	}
	
	@DisplayName("when address variables: street too long, houseNumber too small, city too long, postCode too short -> \"\r\n"
			+ "			+ \", then return 4 errors count, verifying validation")
	@Test
	public void save_Test1() throws Exception {
		var address2 = Address.builder().street("a".repeat(200)).houseNumber(0).city("a".repeat(200))
				.postCode("123").build();
		mockMvc.perform(post("/addresses/")
				.param("street", address2.getStreet())
				.param("houseNumber", Integer.valueOf(address2.getHouseNumber()).toString())
				.param("city", address2.getCity())
				.param("postCode", address2.getPostCode())
				.with(csrf())
				.with(user("TESTER")))
		.andExpect(status().isOk())
		.andExpect(view().name("addresscreate"))
		.andExpect(model().size(1))
		.andExpect(model().errorCount(4))
		.andExpect(model().attributeExists("address"))
		.andExpect(model().attribute("address", hasProperty("street", is(address2.getStreet()))))
		.andExpect(model().attribute("address", hasProperty("houseNumber", is(address2.getHouseNumber()))))
		.andExpect(model().attribute("address", hasProperty("city", is(address2.getCity()))))
		.andExpect(model().attribute("address", hasProperty("postCode", is(address2.getPostCode()))));
		verifyNoInteractions(service);
	}
	
	@DisplayName("when HTTP 200, then caprorAddress==address, view=='listaddresses'")
	@Test
	public void save_Test2() throws Exception {
		when(service.save(any(Address.class))).thenReturn(address);
		when(service.fetchAll()).thenReturn(List.of(address));
		mockMvc.perform(post("/addresses/")
				.param("street", address.getStreet())
				.param("houseNumber", Integer.valueOf(address.getHouseNumber()).toString())
				.param("city", address.getCity())
				.param("postCode", address.getPostCode())
				.with(csrf())
				.with(user("TESTER")))
		.andExpect(status().isOk())
		.andExpect(view().name("listaddresses"))
		.andExpect(model().hasNoErrors())
		.andExpect(model().size(2))
		.andExpect(model().attributeExists("addresses", "address"))
		.andExpect(model().attribute("addresses", hasItem(
				allOf(
						hasProperty("street", is(address.getStreet())),
						hasProperty("houseNumber", is(address.getHouseNumber())),
						hasProperty("city", is(address.getCity())),
						hasProperty("postCode", is(address.getPostCode()))
						)
				)));
		
		verify(service, timeout(1)).fetchAll();
		
		ArgumentCaptor<Address> captor = ArgumentCaptor.forClass(Address.class);
		verify(service, timeout(1)).save(captor.capture());
		var captorAddress = captor.getValue();
		
		assertNotNull(captorAddress);
		assertEquals(address.getStreet(), captorAddress.getStreet());
		assertEquals(address.getHouseNumber(), captorAddress.getHouseNumber());
		assertEquals(address.getCity(), captorAddress.getCity());
		assertEquals(address.getPostCode(), captorAddress.getPostCode());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401, verifying security")
	@Test
	public void save_Test3() throws Exception {
		mockMvc.perform(post("/addresses/")
				.param("street", address.getStreet())
				.param("houseNumber", Integer.valueOf(address.getHouseNumber()).toString())
				.param("city", address.getCity())
				.param("postCode", address.getPostCode())
				.with(csrf()))
		.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
}
