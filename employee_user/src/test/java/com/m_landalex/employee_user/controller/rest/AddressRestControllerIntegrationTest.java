package com.m_landalex.employee_user.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.service.AddressService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AddressRestController.class)
public class AddressRestControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private AddressService service;
	private Address address;

	@BeforeEach
	public void setUp() {
		address = Address.builder().street("TEST_STREET").houseNumber(10).city("TEST_CITY").postCode("12345").build();
	}

	@DisplayName("when status HTTP 200, verifying JSON deserialization from HTTP Request")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test1() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/addresses/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(address)))
		.andExpect(status().isOk());
		verify(service, timeout(1)).save(Mockito.any(Address.class));
	}
	
	@DisplayName("when street value too long is, then status HTTP client error 4xx, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test2() throws JsonProcessingException, Exception {
		var address2 = Address.builder().street("a".repeat(300)).houseNumber(10).city("TEST_CITY").postCode("12345").build();
		mockMvc.perform(post("/rest/addresses/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(address2)))
		.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when houseNumber=0, then status HTTP client error 4xx, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test3() throws JsonProcessingException, Exception {
		var address2 = Address.builder().street("TEST_STREET").houseNumber(0).city("TEST_CITY").postCode("12345").build();
		mockMvc.perform(post("/rest/addresses/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(address2)))
		.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when city value too long is, then status HTTP client error 4xx, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test4() throws JsonProcessingException, Exception {
		var address2 = Address.builder().street("TEST_STREET").houseNumber(10).city("a".repeat(200)).postCode("12345").build();
		mockMvc.perform(post("/rest/addresses/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(address2)))
		.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when postCode value too short is, then status HTTP client error 4xx, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test5() throws JsonProcessingException, Exception {
		var address2 = Address.builder().street("TEST_STREET").houseNumber(10).city("TEST_CITY").postCode("12").build();
		mockMvc.perform(post("/rest/addresses/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(address2)))
		.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when valid input, then return address, verifying deserialization from HTTP Request and serialization to HTTP Response")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test6() throws JsonProcessingException, Exception {
		when(service.save(Mockito.any(Address.class))).thenReturn(address);
		MvcResult result = mockMvc.perform(post("/rest/addresses/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(address)))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(address);
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).save(Mockito.any(Address.class));
	}
	
	@DisplayName("when HTTP 200, then map to business model")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test7() throws JsonProcessingException, Exception {
		when(service.save(Mockito.any(Address.class))).thenReturn(address);
		mockMvc.perform(post("/rest/addresses/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(address)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.street", is(address.getStreet())))
		.andExpect(jsonPath("$.houseNumber", is(address.getHouseNumber())))
		.andExpect(jsonPath("$.city", is(address.getCity())))
		.andExpect(jsonPath("$.postCode", is(address.getPostCode())));
		verify(service, timeout(1)).save(Mockito.any(Address.class));
	}
	
	@DisplayName("when valid input, then return address, verifying object variables")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test8() throws JsonProcessingException, Exception {
		when(service.save(Mockito.any(Address.class))).thenReturn(address);
		mockMvc.perform(post("/rest/addresses/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(address)))
		.andExpect(status().isOk());
		
		ArgumentCaptor<Address> captor = ArgumentCaptor.forClass(Address.class);
		verify(service, timeout(1)).save(captor.capture());
		var captorAddress = captor.getValue();
		
		assertNotNull(captorAddress);
		assertEquals(address.getStreet(), captorAddress.getStreet());
		assertEquals(address.getHouseNumber(), captorAddress.getHouseNumber());
		assertEquals(address.getCity(), captorAddress.getCity());
		assertEquals(address.getPostCode(), captorAddress.getPostCode());
		verify(service, timeout(1)).save(Mockito.any(Address.class));
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void create_Test9() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/addresses/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(address)))
		.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, should return 1 addressObject what is equal to address, verifying serialization to HTTP Response")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test1() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(address));
		MvcResult result = mockMvc.perform(get("/rest/addresses/"))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(List.of(address));
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when status HTTP 200, then should return 1 addressObject what is equal to address")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test2() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(address));
		mockMvc.perform(get("/rest/addresses/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(address.getId())))
				.andExpect(jsonPath("$[0].version", is(address.getVersion())))
				.andExpect(jsonPath("$[0].street", is(address.getStreet())))
				.andExpect(jsonPath("$[0].houseNumber", is(address.getHouseNumber())))
				.andExpect(jsonPath("$[0].city", is(address.getCity())))
				.andExpect(jsonPath("$[0].postCode", is(address.getPostCode())));
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void list_Test3() throws JsonProcessingException, Exception {
		mockMvc.perform(get("/rest/addresses/"))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, then expectedResponseBody and actuallyResponseBody are equal")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void findById_Test1() throws Exception {
		when(service.fetchById(Mockito.anyLong())).thenReturn(address);
		MvcResult result = mockMvc.perform(get("/rest/addresses/{id}", 1L))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(address);
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).fetchById(Mockito.anyLong());
	}
	
	@DisplayName("when status HTTP 200, then return addressObject equal is to address")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void findById_Test2() throws Exception {
		when(service.fetchById(Mockito.anyLong())).thenReturn(address);
		mockMvc.perform(get("/rest/addresses/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.street", is(address.getStreet())))
				.andExpect(jsonPath("$.houseNumber", is(address.getHouseNumber())))
				.andExpect(jsonPath("$.city", is(address.getCity())))
				.andExpect(jsonPath("$.postCode", is(address.getPostCode())));
		verify(service, timeout(1)).fetchById(Mockito.anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void findById_Test3() throws Exception {
		mockMvc.perform(get("/rest/addresses/{id}", 1L))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, then list return size 0")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test 
	public void deleteStandingAloneById_Test1() throws Exception {
		var addresses = new ArrayList<>();
		addresses.add(address);
		assertEquals(1, addresses.size());
		
		doAnswer(invocation -> addresses.remove(0)).when(service).deleteById(Mockito.anyLong());
		
		mockMvc.perform(delete("/rest/addresses/{id}", Mockito.anyLong()).with(csrf()))
				.andExpect(status().isOk());
		verify(service, timeout(1)).deleteById(Mockito.anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test 
	public void deleteStandingAloneById_Test2() throws Exception {
		mockMvc.perform(delete("/rest/addresses/{id}", 1L).with(csrf()))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when by city status HTTP 200, then expectedResponseBody and actuallyResponseBody are equal")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test 
	public void findByCity_Test1() throws Exception {
		when(service.fetchByCity(Mockito.anyString())).thenReturn(address);
		MvcResult result = mockMvc.perform(get("/rest/addresses/city/{city}", "TEST_CITY"))
				.andExpect(status().isOk())
				.andReturn();
		var expectedResponseBody = mapper.writeValueAsString(address);
		var actuallyResponseBody =result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).fetchByCity(Mockito.anyString());
	}
	
	@DisplayName("when by city status HTTP 200, then return addressObject equal is to address")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test 
	public void findByCity_Test2() throws Exception {
		when(service.fetchByCity(Mockito.anyString())).thenReturn(address);
		mockMvc.perform(get("/rest/addresses/city/{city}", "TEST_CITY"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.street", is(address.getStreet())))
				.andExpect(jsonPath("$.houseNumber", is(address.getHouseNumber())))
				.andExpect(jsonPath("$.city", is(address.getCity())))
				.andExpect(jsonPath("$.postCode", is(address.getPostCode())));
		verify(service, timeout(1)).fetchByCity(Mockito.anyString());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test 
	public void findByCity_Test3() throws Exception {
		mockMvc.perform(get("/rest/addresses/city/{city}", "TEST_CITY"))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
}
