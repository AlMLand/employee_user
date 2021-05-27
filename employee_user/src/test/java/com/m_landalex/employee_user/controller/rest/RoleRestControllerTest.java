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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.RoleService;

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
		var role2 = Role.builder().role("TESTER2").build();
		when(service.save(role2)).thenAnswer(invokation -> {roles.add(role2); return role2;});
		
		var returnedRole = controller.create(role2);
		
		assertNotNull(returnedRole);
		assertEquals("TESTER2", returnedRole.getRole());
		assertEquals(2, roles.size());
	}
	
	@DisplayName("when AsyncXAResourcesException.class, then throw ResponseStatusException.class")
	@Test
	public void create_Test2() throws AsyncXAResourcesException {
		Role role2 = null;
		when(service.save(role2)).thenThrow(AsyncXAResourcesException.class);
		
		assertThrows(ResponseStatusException.class, () -> {controller.create(role2);});
	}
	
	@DisplayName("should return list with size 1")
	@Test
	public void list_Test() {
		when(service.fetchAll()).thenReturn(roles);
		var returnedList = controller.list();
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@DisplayName("should return role with variable role='TESTER'")
	@Test
	public void findById_Test() {
		when(service.fetchById(anyLong())).thenReturn(role);
		var returnedRole = controller.findById(Long.valueOf(1));
		
		assertNotNull(returnedRole);
		assertEquals("TESTER", returnedRole.getRole());
	}
	
}
