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

import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.view.DemoRunFile;

@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = { DemoRunFile.class } )
@DisplayName( "Integration EmailService.class test" )
@DirtiesContext( classMode = ClassMode.BEFORE_EACH_TEST_METHOD )
public class EmailServiceTest {

	@Autowired
	private EmailService emailService;
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "should return list with size 2" )
	@Test
	public void saveTest( ) {
		Email newEmail = Email.builder().email( "test@mail.com" ).build();
		emailService.save( newEmail );
		List<Email> returnedList = emailService.fetchAll();
		
		assertNotNull(returnedList);
		assertEquals(2, returnedList.size());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "should return list with updated address" )
	@Test
	public void updateTest( ) {
		Email toUpdateEmail = Email.builder().email( "testToUpdate@mail.com" ).build();
		toUpdateEmail.setId(1L);
		emailService.update( toUpdateEmail );
		List<Email> returnedList = emailService.fetchAll();
		
		assertNotNull( returnedList );
		assertEquals( 1, returnedList.size() );
		assertEquals( "testToUpdate@mail.com", returnedList.get(0).getEmail() );
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "should return list with all addresses = 1" )
	@Test
	public void fetchAllTest( ) {
		List<Email> returendList = emailService.fetchAll();
		
		assertNotNull( returendList );
		assertEquals( 1, returendList.size() );
		assertEquals( "test@mail.com", returendList.get(0).getEmail() );
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "should return address by id" )
	@Test
	public void fetchByIdTest( ) {
		Email returnedEmail = emailService.fetchById( 1L );
		
		assertNotNull( returnedEmail );
		assertEquals( "test@mail.com", returnedEmail.getEmail() );
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "should save the address and delete" )
	@Test
	public void deleteByIdTest( ) {
		Email toDeleteEmail = Email.builder().email( "testToDelete@mail.com" ).build();
		emailService.save( toDeleteEmail );
		
		List<Email> returnedList = emailService.fetchAll();
		assertNotNull( returnedList );
		assertEquals( Integer.valueOf(2), returnedList.size() );
		
		emailService.deleteById(2L);
		
		returnedList = emailService.fetchAll();
		assertNotNull( returnedList );
		assertEquals( Integer.valueOf(1), returnedList.size() );
	}
	
}
