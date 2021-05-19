package com.m_landalex.employee_user.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.service.RoleService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RoleRestController.class)
public class RoleRestControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private RoleService service;
	private Role role;
	
	@BeforeEach
	public void setUp() {
		role = Role.builder().role("OFFICE").build();
	}
	
	@DisplayName("when status HTTP 200, verifying JSON deserialization from HTTP Request")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test1() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/roles/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(role)))
		.andExpect(status().isOk());
		verify(service, timeout(1)).save(any(Role.class));
	}
	
	@DisplayName("when role value is blank, then status HTTP client error 4xx, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test2() throws JsonProcessingException, Exception {
		var role2 = Role.builder().role("").build();
		mockMvc.perform(post("/rest/roles/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(role2)))
		.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when valid input, then return role, verifying deserialization from HTTP Request and serialization to HTTP Respons")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test3() throws JsonProcessingException, Exception {
		when(service.save(any(Role.class))).thenReturn(role);
		MvcResult result = mockMvc.perform(post("/rest/roles/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(role)))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(role);
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).save(any(Role.class));
	}
	
	@DisplayName("when HTTP 200, then map to business model")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test4() throws JsonProcessingException, Exception {
		when(service.save(any(Role.class))).thenReturn(role);
		mockMvc.perform(post("/rest/roles/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(role)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.role", Matchers.is(role.getRole())));
		verify(service, timeout(1)).save(any(Role.class));
	}
	
	@DisplayName("when valid input, then return role, verifying object variables")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test5() throws JsonProcessingException, Exception {
		when(service.save(any(Role.class))).thenReturn(role);
		mockMvc.perform(post("/rest/roles/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(role)))
		.andExpect(status().isOk());
		
		ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
		verify(service, timeout(1)).save(captor.capture());
		var captorRole = captor.getValue();
		
		assertNotNull(captorRole);
		assertEquals(role.getRole(), captorRole.getRole());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void create_Test6() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/roles/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(role)))
		.andExpect(status().is(401));
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, then expectedResponseBody and actuallyResponseBody are equal")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test1() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(role));
		MvcResult result = mockMvc.perform(get("/rest/roles/"))
				.andExpect(status().isOk())
				.andReturn();
		var expectedResponseBody = mapper.writeValueAsString(List.of(role));
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when status HTTP 200, then return list with emailObject equal is to email")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test2() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(role));
		mockMvc.perform(get("/rest/roles/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].role", Matchers.is(role.getRole())));
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void list_Test3() throws Exception {
		mockMvc.perform(get("/rest/roles/"))
				.andExpect(status().is(401));
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, then expectedResponseBody and actuallyResponseBody are equal")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void findById_Test1() throws Exception {
		when(service.fetchById(anyLong())).thenReturn(role);
		MvcResult result = mockMvc.perform(get("/rest/roles/{id}", Long.valueOf(1)))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(role);
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).fetchById(anyLong());
	}
	
	@DisplayName("when status HTTP 200, then return emailObject equal is to email")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void findById_Test2() throws Exception {
		when(service.fetchById(anyLong())).thenReturn(role);
		mockMvc.perform(get("/rest/roles/{id}", Long.valueOf(1)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.role", Matchers.is(role.getRole())));
		verify(service, timeout(1)).fetchById(anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void findById_Test3() throws Exception {
		mockMvc.perform(get("/rest/roles/{id}", Long.valueOf(1)))
				.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
}
