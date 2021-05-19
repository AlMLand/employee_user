package com.m_landalex.employee_user.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

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
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserRestController.class)
public class UserRestControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private UserService service;
	private User user;
	
	@BeforeEach
	public void setUp() {
		user = User.builder().username("UserTEST").password("77777")
				.userRoles(List.of(Role.builder().role("OFFICE").build())).build();
		user.setId(Long.valueOf(1));
	}
	
	@DisplayName("when status HTTP 200, verifying json deserealization from HTTP Request")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test1() throws Exception {
		mockMvc.perform(post("/rest/users/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user)))
				.andExpect(status().isOk());
		verify(service, timeout(1)).save(any(User.class));
	}
	
	@DisplayName("when username too long, then status HTTP client error 4xx, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test2() throws Exception {
		var user2 = User.builder().username("a".repeat(200)).password("77777")
				.userRoles(List.of(Role.builder().role("OFFICE").build())).build();
		
		mockMvc.perform(post("/rest/user/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user2)))
				.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when password too long, then statuc HTTP client error 4xx, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test3() throws Exception {
		var user2 = User.builder().username("UserTEST2").password("1".repeat(200))
				.userRoles(List.of(Role.builder().role("OFFICE").build())).build();
		
		mockMvc.perform(post("/rest/user/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user2)))
				.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when userRoles is empty, then status HTTP client error 4xx, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test4() throws Exception {
		var user2 = User.builder().username("UserTEST2").password("11111")
				.userRoles(null).build();
		
		mockMvc.perform(post("/rest/user/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user2)))
				.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when userRoles->role is blank, then status HTTP client error 4xx, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test5() throws Exception {
		var user2 = User.builder().username("UserTEST2").password("11111")
				.userRoles(List.of(Role.builder().role("").build())).build();
		
		mockMvc.perform(post("/rest/user/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user2)))
				.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when HTTP 200, then map to business model")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test6() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/users/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user)))
				.andExpect(status().isOk());
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(service, timeout(1)).save(captor.capture());
		User captorUser = captor.getValue();
		
		assertNotNull(captorUser);
		assertEquals(user.getId(), captorUser.getId());
		assertEquals(user.getVersion(), captorUser.getVersion());
		assertEquals(user.getUsername(), captorUser.getUsername());
		assertEquals(user.getPassword(), captorUser.getPassword());
		assertEquals(user.getUserRoles().stream().findFirst().get().getRole(), 
				captorUser.getUserRoles().stream().findFirst().get().getRole());
		verify(service, timeout(1)).save(any(User.class));
		
	}
	
	@DisplayName("when valid input, then return user, verifying deserialization from HTTP Request and serialization to HTTP Response")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test7() throws JsonProcessingException, Exception {
		when(service.save(any(User.class))).thenReturn(user);
		MvcResult result = mockMvc.perform(post("/rest/users/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(user);
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).save(any(User.class));
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void create_Test8() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/users/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user)))
		.andExpect(status().is(401));
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, should return 1 userObject what is equal to user, verifying serialization to HTTP Response")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test1() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(user));
		MvcResult result = mockMvc.perform(get("/rest/users/"))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(List.of(user));
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when status HTTP 200, then should return return 1 userObject what is equal to user")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test2() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(user));
		mockMvc.perform(get("/rest/users/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].username", is(user.getUsername())))
				.andExpect(jsonPath("$[0].password", is(user.getPassword())))
				.andExpect(jsonPath("$[0].userRoles[0].role", is(user.getUserRoles().stream().findFirst().get().getRole())));
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void list_Test3() throws Exception {
		mockMvc.perform(get("/rest/users/"))
				.andExpect(status().is(401));
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, then expectedResponseBody and actuallyResponseBody are equal")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void findById_Test1() throws Exception {
		when(service.fetchById(anyLong())).thenReturn(user);
		MvcResult result = mockMvc.perform(get("/rest/users/{id}", 1L))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(user);
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).fetchById(anyLong());
	}
	
	@DisplayName("when status HTTP 200, then return userObject equal is to user")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void findById_Test2() throws Exception {
		when(service.fetchById(anyLong())).thenReturn(user);
		mockMvc.perform(get("/rest/users/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(user.getUsername())))
				.andExpect(jsonPath("$.password", is(user.getPassword())))
				.andExpect(jsonPath("$.userRoles[0].role", is(user.getUserRoles().stream().findFirst().get().getRole())));
		verify(service, timeout(1)).fetchById(anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void findById_Test3() throws Exception {
		mockMvc.perform(get("/rest/users/{id}", 1L))
				.andExpect(status().is(401));
		verifyNoInteractions(service);
	}
	
}
