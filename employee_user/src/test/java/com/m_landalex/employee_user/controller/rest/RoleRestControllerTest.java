package com.m_landalex.employee_user.controller.rest;

import static org.junit.Assert.assertNotNull;
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
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.RoleService;
import com.m_landalex.employee_user.view.controller.rest.RoleRestController;

@ExtendWith(MockitoExtension.class)
public class RoleRestControllerTest {

	@InjectMocks
	private RoleRestController controller;
	@Mock
	private RoleService service;
	private List<Role> roles;
	private Role role;
	
	@BeforeEach
	public void setUp() {
		role = Role.builder().role("TESTER").build();
		roles = new ArrayList<>();
		roles.add(role);
	}
	
	@DisplayName("should return 2 roles")
	@Test
	public void create_Test() throws AsyncXAResourcesException {
		Role role2 = Role.builder().role("TESTER2").build();
		when(service.save(role2)).thenAnswer(new Answer<Role>() {

			@Override
			public Role answer(InvocationOnMock invocation) throws Throwable {
				roles.add(role2);
				return role2;
			}
		});
		
		Role returnedRole = controller.create(role2);
		
		assertNotNull(returnedRole);
		assertEquals("TESTER2", returnedRole.getRole());
		assertEquals(2, roles.size());
	}
	
	@DisplayName("should throw AsyncXAResourcesException.class")
	@Test
	public void create_Test_Fail() throws AsyncXAResourcesException {
		Role role2 = null;
		when(service.save(role2)).thenThrow(AsyncXAResourcesException.class);
		
		assertThrows(AsyncXAResourcesException.class, () -> {controller.create(role2);});
	}
	
	@DisplayName("should return list with size 1")
	@Test
	public void list_Test() {
		when(service.fetchAll()).thenReturn(roles);
		List<Role> returnedList = controller.list();
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@DisplayName("should return role with variable role='TESTER'")
	@Test
	public void findById_Test() {
		when(service.fetchById(anyLong())).thenReturn(role);
		Role returnedRole = controller.findById(Long.valueOf(1));
		
		assertNotNull(returnedRole);
		assertEquals("TESTER", returnedRole.getRole());
	}
	
}