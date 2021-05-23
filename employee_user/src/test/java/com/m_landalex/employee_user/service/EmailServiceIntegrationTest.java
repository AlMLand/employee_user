package com.m_landalex.employee_user.service;

import static org.junit.jupiter.api.Assertions.*;

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

import com.m_landalex.employee_user.DemoRunFile;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { DemoRunFile.class })
@DisplayName("Integration EmailService.class test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmailServiceIntegrationTest {

	@Autowired
	private EmailService emailService;

	@SqlGroup({
			@Sql(value = "classpath:db/test-data.sql", 
					config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
					executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
			@Sql(value = "classpath:db/clean-up.sql", 
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD) })
	@DisplayName("should return list with size 2")
	@Test
	public void save_Test() throws AsyncXAResourcesException {
		var newEmail = Email.builder().email("test@mail.com").build();
		emailService.save(newEmail);
		var returnedList = emailService.fetchAll();

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
	@DisplayName("should return list with all addresses = 1")
	@Test
	public void fetchAll_Test() {
		var returendList = emailService.fetchAll();

		assertNotNull(returendList);
		assertEquals(1, returendList.size());
		assertEquals("test@mail.com", returendList.get(0).getEmail());
	}

	@SqlGroup({
			@Sql(value = "classpath:db/test-data.sql", 
					config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
					executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
			@Sql(value = "classpath:db/clean-up.sql", 
				config = @SqlConfig(encoding = "utf-8", separator = ";", 
				commentPrefix = "--"), executionPhase = ExecutionPhase.AFTER_TEST_METHOD) })
	@DisplayName("should return address by id")
	@Test
	public void fetchById_Test() {
		var returnedEmail = emailService.fetchById(1L);

		assertNotNull(returnedEmail);
		assertEquals("test@mail.com", returnedEmail.getEmail());
	}

	@SqlGroup({
			@Sql(value = "classpath:db/test-data.sql", 
					config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
					executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
			@Sql(value = "classpath:db/clean-up.sql", 
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD) })
	@DisplayName("should save the address and delete")
	@Test
	public void deleteById_Test() throws AsyncXAResourcesException {
		var toDeleteEmail = Email.builder().email("testToDelete@mail.com").build();
		emailService.save(toDeleteEmail);

		var returnedList = emailService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(Integer.valueOf(2), returnedList.size());

		emailService.deleteById(2L);

		returnedList = emailService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(Integer.valueOf(1), returnedList.size());
	}

	@SqlGroup({
			@Sql(value = "classpath:db/test-data.sql", 
					config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
					executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
			@Sql(value = "classpath:db/clean-up.sql", 
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"), 
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD) })
	@DisplayName("should return returnedCount=1")
	@Test
	public void countAll_Test() {
		var returnedCount = emailService.countAll();
		assertEquals(1, returnedCount);
	}

}
