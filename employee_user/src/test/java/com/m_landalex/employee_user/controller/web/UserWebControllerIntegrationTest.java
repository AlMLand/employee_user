package com.m_landalex.employee_user.controller.web;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.hasProperty;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserWebController.class)
public class UserWebControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserService service;
	private User user;
	
	@BeforeEach
	public void setUp() {
		user = User.builder().username("TEST_USERNAME").password("TEST_PASSWORD")
				.userRoles(List.of(Role.builder().role("OFFICE").build())).build();
	}
	
	@DisplayName("when user variables: username too long, password too long, userRoles has not valid role -> "
			+ ", then return 3 errors count, verifying validation")
	@Test
	public void save_Test1() throws Exception {
		var user2 = User.builder().username("a".repeat(100)).password("a".repeat(200))
				.userRoles(List.of(Role.builder().role("").build())).build();
		mockMvc.perform(post("/users/")
				.param("username", user2.getUsername())
				.param("password", user2.getPassword())
				.param("userRoles.role", user2.getUserRoles().stream().findFirst().get().getRole())
				.with(user("TESTER"))
				.with(csrf()))
		.andExpect(status().isOk())
		.andExpect(view().name("usercreate"))
		.andExpect(model().errorCount(3))
		.andExpect(model().attributeExists("user"))
		.andExpect(model().attributeErrorCount("user", 3))
		.andExpect(model().attributeHasFieldErrors("user", "username", "password", "userRoles"))
		.andExpect(model().attribute("user", hasProperty("username", is(user2.getUsername()))))
		.andExpect(model().attribute("user", hasProperty("password", is(user2.getPassword()))));
		verifyNoInteractions(service);
	}
	
	@DisplayName("when HTTP 200, then caprorUser==user, view=='listusers'")
	@Test
	public void save_Test2() throws Exception {
		when(service.save(any(User.class))).thenReturn(user);
		when(service.fetchAll()).thenReturn(List.of(user));
		mockMvc.perform(post("/users/")
				.param("username", user.getUsername())
				.param("password", user.getPassword())
				.param("userRoles", user.getUserRoles().toString())
				.with(user("TESTER"))
				.with(csrf()))
		.andExpect(status().isOk())
		.andExpect(view().name("listusers"))
		.andExpect(model().hasNoErrors())
		.andExpect(model().attributeExists("users"))
		.andExpect(model().attribute("users", hasItem(
				allOf(
						hasProperty("username", is(user.getUsername())),
						hasProperty("password", is(user.getPassword())),
						hasProperty("userRoles", is(user.getUserRoles()))
						)
				)));
		
		verify(service, timeout(1)).fetchAll();
		
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(service, timeout(1)).save(captor.capture());
		var captorUser = captor.getValue();
		
		assertNotNull(captorUser);
		assertEquals(user.getUsername(), captorUser.getUsername());
		assertEquals(user.getPassword(), captorUser.getPassword());
		assertEquals(user.getUserRoles().stream().findFirst().get().getRole(), 
				captorUser.getUserRoles().stream().findFirst().get().getRole()
				.substring(1, captorUser.getUserRoles().stream().findFirst().get().getRole().length() - 1));
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401, verifying security")
	@Test
	public void save_Test3() throws Exception {
		mockMvc.perform(post("/users/")
				.param("username", user.getUsername())
				.param("password", user.getPassword())
				.param("userRoles", user.getUserRoles().toString())
				.with(csrf()))
		.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
}
