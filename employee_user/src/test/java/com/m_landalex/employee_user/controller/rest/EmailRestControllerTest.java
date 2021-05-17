package com.m_landalex.employee_user.controller.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmailService;

@ExtendWith(MockitoExtension.class)
public class EmailRestControllerTest {

	@InjectMocks
	private EmailRestController controller;
	@Mock
	private EmailService service;
	private List<Email> emails;
	
	@BeforeEach
	public void setUp() {
		Email email = Email.builder().email("Test_1@test.com").build();
		email.setId(1L);
		emails = new ArrayList<>();
		emails.add(email);
	}
	
	@DisplayName("should return list with size 2 and email with email='Test_2@test.com'")
	@Test
	public void create_Test() throws AsyncXAResourcesException {
		Email newEmail = Email.builder().email("Test_2@test.com").build();
		when(service.save(newEmail)).thenAnswer(new Answer<Email>() {

			@Override
			public Email answer(InvocationOnMock invocation) throws Throwable {
				emails.add(newEmail);
				return newEmail;
			}
		});
		Email returnedEmail = controller.create(newEmail);
		
		assertNotNull(returnedEmail);
		assertEquals(2, emails.size());
		assertEquals("Test_2@test.com", returnedEmail.getEmail());
	}
	
	@DisplayName("should throw AsyncXAResourcesException.class")
	@Test
	public void create_ShouldThrowRuntimeExceptionTest() throws AsyncXAResourcesException {
		Email email = null;
		when(service.save(email)).thenThrow(AsyncXAResourcesException.class);
		
		assertThrows(AsyncXAResourcesException.class, () -> {controller.create(email);});
	}
	
	@DisplayName("should return list with size 1")
	@Test
	public void list_Test() {
		when(service.fetchAll()).thenReturn(emails);
		List<Email> returnedList = controller.list();
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@DisplayName("should return email with email='Test_1@test.com'")
	@Test
	public void findById_Test() {
		when(service.fetchById(anyLong())).thenReturn(emails.get(0));
		Email returnedEmail = controller.findById(anyLong());
		
		assertNotNull(returnedEmail);
		assertEquals("Test_1@test.com", returnedEmail.getEmail());
	}
	
	@DisplayName("should return list with size 0")
	@Test
	public void deleteStandingAloneById_Test() {
		doAnswer(new Answer<Email>() {

			@Override
			public Email answer(InvocationOnMock invocation) throws Throwable {
				emails.remove(0);
				return null;
			}
		}).when(service).deleteById(anyLong());
		
		controller.deleteStandingAloneById(anyLong());
		
		assertNotNull(emails);
		assertEquals(0, emails.size());
	}
	
}
