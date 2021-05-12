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

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.UserService;
import com.m_landalex.employee_user.view.controller.rest.UserController;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@Mock
	private UserService mockedUserService;
	private ExtendedModelMap extendedModelMap;
	private UserController userController;
	private List<User> listUsers;

	@BeforeEach
	public void setUp() {
		extendedModelMap = new ExtendedModelMap();
		userController = new UserController();
		ReflectionTestUtils.setField(userController, "service", mockedUserService);
		listUsers = new ArrayList<>();
		User user = User.builder().username("Test_1").password("Test_1").userRoles(
				List.of(Role.builder().role("DEVELOPMENT").build())
				).build();
		user.setId(1L);
		listUsers.add(user);
	}
	
	@Test
	public void create_Test() throws AsyncXAResourcesException {
		User newUser = User.builder().username("Test_2").password("Test_2").userRoles(
				List.of(Role.builder().role("ADMINISTRATION").build())
				).build();
		Mockito.when(mockedUserService.save(newUser)).thenAnswer(new Answer<User>() {

			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				listUsers.add(newUser);
				return newUser;
			}
		});
		extendedModelMap.addAttribute("create", userController.create(newUser));
		User returnedUser = (User) extendedModelMap.get("create");
		
		assertNotNull(returnedUser);
		assertEquals(2, listUsers.size());
		assertEquals("Test_2", listUsers.get(1).getUsername());
	}
	
	@Test
	public void create_ShouldThrowRuntimeExceptionTest() throws AsyncXAResourcesException {
		User newUser = null;
		Mockito.when(mockedUserService.save(newUser)).thenThrow(RuntimeException.class);
		
		assertThrows(RuntimeException.class, ()->{
			userController.create(newUser);
		});
	}
	
	@Test
	public void list_Test() {
		Mockito.when(mockedUserService.fetchAll()).thenReturn(listUsers);
		
		extendedModelMap.addAttribute("list", userController.list());
		@SuppressWarnings("unchecked")
		List<User> returnList = (List<User>) extendedModelMap.get("list");
		
		assertNotNull(returnList);
		assertEquals(1, returnList.size());
	}
	
	@Test
	public void findById_Test() {
		Mockito.when(mockedUserService.fetchById(Mockito.anyLong())).thenReturn(listUsers.get(0));
		
		extendedModelMap.addAttribute("findById", userController.findById(1L));
		User returnedUser = (User) extendedModelMap.get("findById");
		
		assertNotNull(returnedUser);
		assertEquals("Test_1", returnedUser.getUsername());
	}

}
