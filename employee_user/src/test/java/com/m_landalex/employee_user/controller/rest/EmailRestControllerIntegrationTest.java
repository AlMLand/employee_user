package com.m_landalex.employee_user.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.service.EmailService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmailRestController.class)
public class EmailRestControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private EmailService service;
	private Email email;
	
	
	@BeforeEach
	public void setUp() {
		email = Email.builder().email("TEST@googlemail.com").build();
	}
	
	@DisplayName("when valid input, then return HTTP 200, verifying deserialization from JSON HTTP Request")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test1() throws Exception {
		mockMvc.perform(post("/rest/emails/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(email)))
				.andExpect(status().isOk());
		verify(service, timeout(1)).save(any(Email.class));
	}
	
	@DisplayName("when email value is blank, then return HTTP 4xx client error, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test2() throws JsonProcessingException, Exception {
		var email2 = Email.builder().email("").build();
		mockMvc.perform(post("/rest/emails/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(email2)))
		.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when email value not contain->@, then return HTTP 4xx client error, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test3() throws JsonProcessingException, Exception {
		var email2 = Email.builder().email("xxxxxxxxx").build();
		mockMvc.perform(post("/rest/emails/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(email2)))
		.andExpect(status().is4xxClientError());
		verifyNoInteractions(service);
	}

	@DisplayName("when valid input, then return email, verifying deserialization from HTTP Request and serialization to HTTP Response")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test4() throws JsonProcessingException, Exception {
		when(service.save(any(Email.class))).thenReturn(email);
		MvcResult result = mockMvc.perform(post("/rest/emails/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(email)))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(email);
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).save(any(Email.class));
	}
	
	@DisplayName("when HTTP status 200, then map to business model")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test5() throws JsonProcessingException, Exception {
		when(service.save(any(Email.class))).thenReturn(email);
		mockMvc.perform(post("/rest/emails/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(email)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.email", Matchers.is(email.getEmail())));
		verify(service, timeout(1)).save(any(Email.class));
	}
	
	@DisplayName("when valid input, then return email, verifying object variables")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test6() throws JsonProcessingException, Exception {
		when(service.save(any(Email.class))).thenReturn(email);
		mockMvc.perform(post("/rest/emails/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(email)))
		.andExpect(status().isOk());
		
		ArgumentCaptor<Email> captor = ArgumentCaptor.forClass(Email.class);
		verify(service, timeout(1)).save(captor.capture());
		var captorEmail = captor.getValue();
		
		assertNotNull(captorEmail);
		assertEquals(email.getEmail(), captorEmail.getEmail());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void create_Test7() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/emails/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(email)))
		.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, should return list with 1 emailObject what is equal to email, "
			+ "verifying serialization to HTTP Response")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test1() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(email));
		MvcResult result = mockMvc.perform(get("/rest/emails/"))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(List.of(email));
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponseBody);
		assertNotEquals("", actuallyResponseBody);
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
	}
	
	@DisplayName("when status HTTP 200, then should return list with 1 emailObject what is equal to email")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test2() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(email));
		mockMvc.perform(get("/rest/emails/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].email", Matchers.is(email.getEmail())));
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void list_Test3() throws Exception {
		mockMvc.perform(get("/rest/emails/"))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, then expectedResponseBody and actuallyResponseBody are equal")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void findById_Test1() throws Exception {
		when(service.fetchById(anyLong())).thenReturn(email);
		MvcResult result = mockMvc.perform(get("/rest/emails/{id}", Long.valueOf(1)))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(email);
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
		when(service.fetchById(anyLong())).thenReturn(email);
		mockMvc.perform(get("/rest/emails/{id}", Long.valueOf(1)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", Matchers.is(email.getEmail())));
		verify(service, timeout(1)).fetchById(anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void findById_Test3() throws Exception {
		mockMvc.perform(get("/rest/emails/{id}", Long.valueOf(1)))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}

	@DisplayName("when status HTTP 200, then list return size 0")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void deleteStandingAloneById_Test1() throws Exception {
		var emails = new ArrayList<>();
		emails.add(email);
		assertEquals(1, emails.size());
		
		doAnswer(new Answer<Email>() {

			@Override
			public Email answer(InvocationOnMock invocation) throws Throwable {
				emails.remove(0);
				return null;
			}
		}).when(service).deleteById(anyLong());
		
		mockMvc.perform(delete("/rest/emails/{id}", Long.valueOf(1)).with(csrf()))
				.andExpect(status().isOk());
		verify(service, timeout(1)).deleteById(anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void deleteStandingAloneById_Test2() throws Exception {
		mockMvc.perform(delete("/rest/emails/{id}", Long.valueOf(1)).with(csrf()))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
}
