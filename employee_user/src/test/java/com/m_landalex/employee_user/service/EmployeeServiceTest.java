package com.m_landalex.employee_user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
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

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.view.DemoRunFile;

@ActiveProfiles( "test" )
@ExtendWith( SpringExtension.class )
@SpringBootTest( classes = { DemoRunFile.class } )
@DisplayName( "Integration EmployeeService.class Test" )
@DirtiesContext( classMode = ClassMode.BEFORE_EACH_TEST_METHOD )
public class EmployeeServiceTest {

	@Autowired
	private EmployeeService employeeService;
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName("should return all(1) employees")
	@Test
	public void fetchAllTest() {
		List<Employee> returnedList = employeeService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName("should save one employee and return 2 employees")
	@Test
	public void saveTest() throws AsyncXAResourcesException {
		Employee newEmployee = Employee.builder().firstName("test_new_firstname").lastName("test_new_lastname").age(99)
				.salary(new BigDecimal(999.99d)).email(Email.builder().email("test@mail.com").build())
				.addressData(Address.builder().street("test_new_street").houseNumber(99).city("test_new_city")
						.postCode("99999").build())
				.userData(User.builder().username("test_new_username").password("test_new_password")
						.userRole(Role.DEVELOPMENT).build())
				.build();
		employeeService.save(newEmployee);
		
		List<Employee> returnedList = employeeService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(2, returnedList.size());
		assertEquals("test_new_firstname", returnedList.get(1).getFirstName());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "schould return quantity all employees" )
	@Test
	public void countAllEmployeesTest() {
		long returnedCount = employeeService.countAllEmployees();
		assertNotNull(returnedCount);
		assertEquals(1, returnedCount);
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "schould return updated employee" )
	@Test
	public void updateTest() {
		Employee toUpdateEmployee = Employee.builder().firstName("test_toUpdate_firstname").lastName("test_toUpdate_lastname").age(88)
				.salary(new BigDecimal(888.88d)).email(Email.builder().email("toUpdateTest@mail.com").build())
				.addressData(Address.builder().street("test_toUpdate_street").houseNumber(88).city("test_ntoUpdate_city")
						.postCode("88888").build())
				.userData(User.builder().username("test_toUpdate_username").password("test_toUpdate_password")
						.userRole(Role.DEVELOPMENT).build())
				.build();
		toUpdateEmployee.setId(1L);
		employeeService.update(toUpdateEmployee);
		
		List<Employee> returnedList = employeeService.fetchAll();
		Employee updatedEmployee = returnedList.get(0);
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
		assertEquals("test_toUpdate_firstname", updatedEmployee.getFirstName());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "schould return employee by id 1L" )
	@Test
	public void fetchByIdTest() {
		Employee returnedEmployee = employeeService.fetchById(1L);
		assertNotNull(returnedEmployee);
		assertEquals("test_firstname", returnedEmployee.getFirstName());
		assertEquals("test@mail.com", returnedEmployee.getEmail().getEmail());
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "schould return employee by firstname" )
	@Test
	public void fetchByFirstNameTest( ) {
		List<Employee> returnedList = employeeService.fetchByFirstName( "test_firstname" );
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
		
		Employee returnedEmployee = returnedList.get(0);
		assertNotNull(returnedEmployee);
		assertEquals("test_firstname", returnedEmployee.getFirstName());
		assertEquals("test@mail.com", returnedEmployee.getEmail().getEmail());
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "schould return employee by lastname" )
	@Test
	public void fetchByLastNameTest( ) {
		List<Employee> returnedList = employeeService.fetchByLastName( "test_lastname" );
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
		
		Employee returnedEmployee = returnedList.get(0);
		assertNotNull(returnedEmployee);
		assertEquals("test_lastname", returnedEmployee.getLastName());
		assertEquals("test@mail.com", returnedEmployee.getEmail().getEmail());
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "schould return a list with size 0" )
	@Test
	public void deleteByIdTest( ) {
		employeeService.deleteById(1L);
		
		List<Employee> returnedList = employeeService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(0, returnedList.size());
	}

	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@DisplayName( "schould return a empty list with size 0" )
	@Test
	public void deleteAllTest( ) {
		employeeService.deleteAll();
		
		List<Employee> returnedList = employeeService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(0, returnedList.size());
	}
	
}
