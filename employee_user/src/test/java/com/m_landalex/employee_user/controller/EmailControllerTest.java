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
import com.m_landalex.employee_user.service.EmailService;
import com.m_landalex.employee_user.view.controller.EmailController;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

	@Mock
	private EmailService mockedEmailService;
	private EmailController emailController;
	private ExtendedModelMap extendedModelMap;
	private List<Email> listEmails;
	
	@BeforeEach
	public void setUp() {
		emailController = new EmailController();
		extendedModelMap = new ExtendedModelMap();
		listEmails = new ArrayList<>();
		Email email = Email.builder().email("Test_1@test.com").build();
		email.setId(1L);
		listEmails.add(email);
	}
	
	@Test
	public void createEmailTest() {
		Email newEmail = Email.builder().email("Test_2@test.com").build();
		Mockito.when(mockedEmailService.save(newEmail)).thenAnswer(new Answer<Email>() {

			@Override
			public Email answer(InvocationOnMock invocation) throws Throwable {
				listEmails.add(newEmail);
				return newEmail;
			}
		});
		ReflectionTestUtils.setField(emailController, "emailService", mockedEmailService);
		extendedModelMap.addAttribute("createEmail", emailController.createEmail(newEmail));
		Email returnedEmail = (Email) extendedModelMap.get("createEmail");
		
		assertNotNull(returnedEmail);
		assertEquals(2, listEmails.size());
		assertEquals("Test_2@test.com", returnedEmail.getEmail());
	}
	
	@Test
	public void createEmailShouldThrowRuntimeExceptionTest() {
		Email email = null;
		Mockito.when(mockedEmailService.save(email)).thenThrow(RuntimeException.class);
		ReflectionTestUtils.setField(emailController, "emailService", mockedEmailService);
		
		assertThrows(RuntimeException.class, () -> {
			emailController.createEmail(email);
		});
	}
	
	@Test
	public void fetchAllEmailsTest() {
		Mockito.when(mockedEmailService.fetchAll()).thenReturn(listEmails);
		ReflectionTestUtils.setField(emailController, "emailService", mockedEmailService);
		extendedModelMap.addAttribute("fetchAllEmails", emailController.fetchAllEmails());
		@SuppressWarnings("unchecked")
		List<Email> returnedList = (List<Email>) extendedModelMap.get("fetchAllEmails");
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@Test
	public void updateEmailById() {
		Email newEmail = Email.builder().email("Test_2@test.com").build();
		Mockito.when(mockedEmailService.update(newEmail)).thenAnswer(new Answer<Email>() {

			@Override
			public Email answer(InvocationOnMock invocation) throws Throwable {
				Email returnedEmail = listEmails.get(0);
				returnedEmail.setEmail(newEmail.getEmail());
				listEmails.remove(0);
				listEmails.add(returnedEmail);
				return newEmail;
			}
		});
		ReflectionTestUtils.setField(emailController, "emailService", mockedEmailService);
		extendedModelMap.addAttribute("updateEmailById", emailController.updateEmailById(1L, newEmail));
		Email updatedEmail = (Email) extendedModelMap.get("updateEmailById");
		
		assertNotNull(updatedEmail);
		assertEquals(1, listEmails.size());
		assertEquals("Test_2@test.com", updatedEmail.getEmail());
	}
	
	@Test
	public void fetchEmailByIdTest() {
		Mockito.when(mockedEmailService.fetchById(Mockito.anyLong())).thenReturn(listEmails.get(0));
		ReflectionTestUtils.setField(emailController, "emailService", mockedEmailService);
		
		extendedModelMap.addAttribute("fetchEmailById", emailController.fetchEmailById(1L));
		Email returnedEmail = (Email) extendedModelMap.get("fetchEmailById");
		
		assertNotNull(returnedEmail);
		assertEquals("Test_1@test.com", returnedEmail.getEmail());
	}
	
	@Test
	public void deleteStandingAloneEmailByIdTest() {
		Mockito.doAnswer(new Answer<Email>() {

			@Override
			public Email answer(InvocationOnMock invocation) throws Throwable {
				listEmails.remove(0);
				return null;
			}
		}).when(mockedEmailService).deleteById(Mockito.anyLong());
		ReflectionTestUtils.setField(emailController, "emailService", mockedEmailService);
		
		emailController.deleteStandingAloneEmailById(1L);
		
		assertNotNull(listEmails);
		assertEquals(0, listEmails.size());
	}
	
}
