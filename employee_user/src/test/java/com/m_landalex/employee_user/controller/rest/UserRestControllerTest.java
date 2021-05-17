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

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.UserService;
import com.m_landalex.employee_user.view.controller.rest.UserRestController;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {

	@InjectMocks
	private UserRestController controller;
	@Mock
	private UserService service;
	private List<User> users;

	@BeforeEach
	public void setUp() {
		User user = User.builder().username("Test_1").password("Test_1")
				.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build();
		user.setId(1L);
		users = new ArrayList<>();
		users.add(user);
	}
	
	@DisplayName("should return list with size 2 and return user with username='Test_2'")
	@Test
	public void create_Test() throws AsyncXAResourcesException {
		User newUser = User.builder().username("Test_2").password("Test_2")
				.userRoles(List.of(Role.builder().role("ADMINISTRATION").build())).build();
		when(service.save(newUser)).thenAnswer(new Answer<User>() {

			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				users.add(newUser);
				return newUser;
			}
		});
		User returnedUser = controller.create(newUser);
		
		assertNotNull(returnedUser);
		assertEquals(2, users.size());
		assertEquals("Test_2", users.get(1).getUsername());
	}
	
	@DisplayName("should throw AsyncXAResourcesException.class")
	@Test
	public void create_ShouldThrowRuntimeExceptionTest() throws AsyncXAResourcesException {
		User newUser = null;
		when(service.save(newUser)).thenThrow(AsyncXAResourcesException.class);

		assertThrows(AsyncXAResourcesException.class, () -> {controller.create(newUser);});
	}
	
	@DisplayName("should return list with size 1")
	@Test
	public void list_Test() {
		when(service.fetchAll()).thenReturn(users);
		List<User> returnList = controller.list();
		
		assertNotNull(returnList);
		assertEquals(1, returnList.size());
	}
	
	@DisplayName("should return user with username='Test_1'")
	@Test
	public void findById_Test() {
		when(service.fetchById(anyLong())).thenReturn(users.get(0));
		User returnedUser = controller.findById(anyLong());
		
		assertNotNull(returnedUser);
		assertEquals("Test_1", returnedUser.getUsername());
	}

}
