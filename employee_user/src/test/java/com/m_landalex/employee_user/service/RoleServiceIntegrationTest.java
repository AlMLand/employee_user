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
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { DemoRunFile.class })
@DisplayName("Integration RoleService.class Test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class RoleServiceIntegrationTest {

	@Autowired
	private RoleService service;
	
	@SqlGroup({ 
		@Sql(value = "classpath:db/test-data.sql",
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
				executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(value = "classpath:db/clean-up.sql",
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	})
	@DisplayName("should return list with size 2")
	@Test
	public void save_Test() throws AsyncXAResourcesException {
		service.save(Role.builder().role(null).build());
		var returnedList = service.fetchAll();
		
		assertNotNull(returnedList);
		assertEquals(2, returnedList.size());
	}
	
	@SqlGroup({ 
		@Sql(value = "classpath:db/test-data.sql",
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
				executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(value = "classpath:db/clean-up.sql",
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	})
	@DisplayName("should return list with size 1")
	@Test
	public void fetchAll_Test() {
		var returnedLis = service.fetchAll();
		
		assertNotNull(returnedLis);
		assertEquals(1, returnedLis.size());
	}
	
	@SqlGroup({ 
		@Sql(value = "classpath:db/test-data.sql",
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
				executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(value = "classpath:db/clean-up.sql",
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	})
	@DisplayName("should return count 1")
	@Test
	public void countAll_Test() {
		assertEquals(1, service.countAll());
	}

	@SqlGroup({ 
		@Sql(value = "classpath:db/test-data.sql",
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
				executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(value = "classpath:db/clean-up.sql",
				config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
				executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	})
	@DisplayName("should return Role object with role 'DEVELOPMENT'")
	@Test
	public void fetchById_Test() {
		var returnedRole = service.fetchById(1L);
		
		assertNotNull(returnedRole);
		assertEquals(Long.valueOf(1), returnedRole.getId());
		assertEquals("DEVELOPMENT", returnedRole.getRole());
	}
	
}
