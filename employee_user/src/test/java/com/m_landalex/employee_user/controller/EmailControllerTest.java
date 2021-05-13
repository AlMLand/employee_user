package com.m_landalex.employee_user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmailService;
import com.m_landalex.employee_user.view.controller.rest.EmailRestController;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

	@Mock
	private EmailService mockedEmailService;
	private EmailRestController emailController;
	private ExtendedModelMap extendedModelMap;
	private List<Email> listEmails;
	
	@BeforeEach
	public void setUp() {
		emailController = new EmailRestController();
		extendedModelMap = new ExtendedModelMap();
		ReflectionTestUtils.setField(emailController, "service", mockedEmailService);
		listEmails = new ArrayList<>();
		Email email = Email.builder().email("Test_1@test.com").build();
		email.setId(1L);
		listEmails.add(email);
	}
	
	@Test
	public void create_Test() throws AsyncXAResourcesException {
		Email newEmail = Email.builder().email("Test_2@test.com").build();
		Mockito.when(mockedEmailService.save(newEmail)).thenAnswer(new Answer<Email>() {

			@Override
			public Email answer(InvocationOnMock invocation) throws Throwable {
				listEmails.add(newEmail);
				return newEmail;
			}
		});
		extendedModelMap.addAttribute("create", emailController.create(newEmail));
		Email returnedEmail = (Email) extendedModelMap.get("create");
		
		assertNotNull(returnedEmail);
		assertEquals(2, listEmails.size());
		assertEquals("Test_2@test.com", returnedEmail.getEmail());
	}
	
	@Test
	public void create_ShouldThrowRuntimeExceptionTest() throws AsyncXAResourcesException {
		Email email = null;
		Mockito.when(mockedEmailService.save(email)).thenThrow(RuntimeException.class);
		
		assertThrows(RuntimeException.class, () -> {
			emailController.create(email);
		});
	}
	
	@Test
	public void list_Test() {
		Mockito.when(mockedEmailService.fetchAll()).thenReturn(listEmails);
		extendedModelMap.addAttribute("list", emailController.list());
		@SuppressWarnings("unchecked")
		List<Email> returnedList = (List<Email>) extendedModelMap.get("list");
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@Test
	public void findById_Test() {
		Mockito.when(mockedEmailService.fetchById(Mockito.anyLong())).thenReturn(listEmails.get(0));
		
		extendedModelMap.addAttribute("findById", emailController.findById(1L));
		Email returnedEmail = (Email) extendedModelMap.get("findById");
		
		assertNotNull(returnedEmail);
		assertEquals("Test_1@test.com", returnedEmail.getEmail());
	}
	
	@Test
	public void deleteStandingAloneById_Test() {
		Mockito.doAnswer(new Answer<Email>() {

			@Override
			public Email answer(InvocationOnMock invocation) throws Throwable {
				listEmails.remove(0);
				return null;
			}
		}).when(mockedEmailService).deleteById(Mockito.anyLong());
		
		emailController.deleteStandingAloneById(1L);
		
		assertNotNull(listEmails);
		assertEquals(0, listEmails.size());
	}
	
}
