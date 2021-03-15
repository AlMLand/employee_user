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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.view.DemoRunFile;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = DemoRunFile.class )
@DisplayName( "Integration UserService.class test" )
@DirtiesContext( classMode = ClassMode.BEFORE_EACH_TEST_METHOD )
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "Should return all users" )
	@Test
	public void fetchAllTest() {
		List<User> returnedList = userService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "Should return one user by Id" )
	@Test
	public void fetchByIdTest() {
		User returnedUser = userService.fetchById(1L);
		assertNotNull(returnedUser);
		assertEquals("test_username", returnedUser.getUsername());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "Should save new user and return two users" )
	@Test
	public void saveTest() throws AsyncXAResourcesException {
		User newUser = User.builder().username("test_save_new_user_username")
				.password("test_save_new_user_password").userRole(Role.DEVELOPMENT).build();
		userService.save(newUser);
		List<User> returnedList = userService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(2, returnedList.size());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "Should return updated user" )
	@Test
	public void updateTest() {
		User newUser = User.builder().username("test_save_updateed_user_username")
				.password("test_save_updated_user_password").userRole(Role.DEVELOPMENT).build();
		newUser.setId(1L);
		userService.update(newUser);
		
		List<User> returnedList = userService.fetchAll();
		User returnedUser = userService.fetchById(1L);
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
		assertNotNull(returnedUser);
		assertEquals("test_save_updateed_user_username", returnedUser.getUsername());
	}
	
}
