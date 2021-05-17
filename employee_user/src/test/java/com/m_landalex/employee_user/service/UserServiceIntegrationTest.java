package com.m_landalex.employee_user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.view.DemoRunFile;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoRunFile.class)
@DisplayName("Integration UserService.class test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;

	@SqlGroup({
			@Sql(value = "classpath:db/test-data.sql", 
					config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
					executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
			@Sql(value = "classpath:db/clean-up.sql", 
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD) })
	@DisplayName("Should return all users")
	@Test
	public void fetchAll_Test() {
		List<User> returnedList = userService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}

	@SqlGroup({
			@Sql(value = "classpath:db/test-data.sql", 
					config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
					executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
			@Sql(value = "classpath:db/clean-up.sql", 
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD) })
	@DisplayName("Should return one user by Id")
	@Test
	public void fetchById_Test() {
		User returnedUser = userService.fetchById(1L);
		assertNotNull(returnedUser);
		assertEquals("test_username", returnedUser.getUsername());
	}

	@SqlGroup({
			@Sql(value = "classpath:db/test-data.sql", 
					config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
					executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
			@Sql(value = "classpath:db/clean-up.sql", 
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD) })
	@DisplayName("Should save new user and return two users")
	@Test
	public void save_Test() throws AsyncXAResourcesException {
		User newUser = User.builder().username("test_save_new_user_username").password("test_save_new_user_password")
				.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build();
		userService.save(newUser);
		List<User> returnedList = userService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(2, returnedList.size());
	}

	@SqlGroup({
			@Sql(value = "classpath:db/test-data.sql", 
					config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
					executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
			@Sql(value = "classpath:db/clean-up.sql", 
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD) })
	@DisplayName("Should return quantity all users")
	@Test
	public void countAll_Test() {
		Long returnedCount = userService.countAll();
		assertNotNull(returnedCount);
		assertEquals(1, returnedCount);
	}
	
	@SqlGroup({
		@Sql(value = "classpath:db/test-data.sql", 
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
				executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(value = "classpath:db/clean-up.sql", 
			config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD) })
	@DisplayName("Should return user with username 'test_username'")
	@Test
	public void fetchUserByUsername_Test() {
		User returnedUser = userService.fetchUserByUsername("test_username");
		
		assertNotNull(returnedUser);
		assertEquals("test_username", returnedUser.getUsername());
		
	}

}
