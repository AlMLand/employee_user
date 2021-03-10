package com.m_landalex.employee_user;

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
import com.m_landalex.employee_user.view.controller.UserController;

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
		listUsers = new ArrayList<>();
		User user = User.builder().username("Test_1").password("Test_1").userRole(Role.DEVELOPMENT).build();
		user.setId(1L);
		listUsers.add(user);
	}
	
	@Test
	public void createUserTest() throws AsyncXAResourcesException {
		User newUser = User.builder().username("Test_2").password("Test_2").userRole(Role.ADMINISTRATION).build();
		Mockito.when(mockedUserService.save(newUser)).thenAnswer(new Answer<User>() {

			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				listUsers.add(newUser);
				return newUser;
			}
		});
		ReflectionTestUtils.setField(userController, "userService", mockedUserService);
		extendedModelMap.addAttribute("createUser", userController.createUser(newUser));
		User returnedUser = (User) extendedModelMap.get("createUser");
		
		assertNotNull(returnedUser);
		assertEquals(2, listUsers.size());
		assertEquals("Test_2", listUsers.get(1).getUsername());
	}
	
	@Test
	public void createUserShouldThrowRuntimeExceptionTest() throws AsyncXAResourcesException {
		User newUser = null;
		Mockito.when(mockedUserService.save(newUser)).thenThrow(RuntimeException.class);
		ReflectionTestUtils.setField(userController, "userService", mockedUserService);
		
		assertThrows(RuntimeException.class, ()->{
			userController.createUser(newUser);
		});
	}
	
	@Test
	public void fetchAllUsersTest() {
		Mockito.when(mockedUserService.fetchAll()).thenReturn(listUsers);
		ReflectionTestUtils.setField(userController, "userService", mockedUserService);
		
		extendedModelMap.addAttribute("fetchAllEmployees", userController.fetchAllUsers());
		@SuppressWarnings("unchecked")
		List<User> returnList = (List<User>) extendedModelMap.get("fetchAllEmployees");
		
		assertNotNull(returnList);
		assertEquals(1, returnList.size());
	}
	
	@Test
	public void updateUserByIdTest() throws AsyncXAResourcesException {
		User user = User.builder().username("Test_2").password("Test_2").userRole(Role.ADMINISTRATION).build();
		Mockito.when(mockedUserService.update(user)).thenAnswer(new Answer<User>() {

			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				User returnedUser = listUsers.get(0);
				returnedUser.setUsername(user.getUsername());
				returnedUser.setPassword(user.getPassword());
				returnedUser.setUserRole(user.getUserRole());
				listUsers.remove(0);
				listUsers.add(returnedUser);
				return returnedUser;
			}
		});
		ReflectionTestUtils.setField(userController, "userService", mockedUserService);
		
		extendedModelMap.addAttribute("updateUser", userController.updateUserById(1L, user));
		User updatedUser = (User) extendedModelMap.get("updateUser");
		
		assertNotNull(updatedUser);
		assertEquals(1, listUsers.size());
		assertEquals("Test_2", updatedUser.getUsername());
	}
	
	@Test
	public void fetchUserByIdTest() {
		Mockito.when(mockedUserService.fetchById(Mockito.anyLong())).thenReturn(listUsers.get(0));
		ReflectionTestUtils.setField(userController, "userService", mockedUserService);
		
		extendedModelMap.addAttribute("fetchUserById", userController.fetchUserById(1L));
		User returnedUser = (User) extendedModelMap.get("fetchUserById");
		
		assertNotNull(returnedUser);
		assertEquals("Test_1", returnedUser.getUsername());
	}

}
