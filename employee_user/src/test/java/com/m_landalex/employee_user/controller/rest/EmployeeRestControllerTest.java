package com.m_landalex.employee_user.controller.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.web.server.ResponseStatusException;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeRestControllerTest {

	@InjectMocks 
	private EmployeeRestController controller;
	@Mock 
	private EmployeeService service;
	private List<Employee> employees;
	
	@BeforeEach
	public void setUp() {
		Employee employee = Employee.builder().firstName("Test_firstName_1").lastName("Test_lastName_1").age(100)
				.salary(new BigDecimal(5000.00d)).email(Email.builder().email("test_1@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_1").houseNumber(10).city("Test_city_1")
						.postCode("12345").build())
				.userData(User.builder().username("A_A_A").password("B_B_B")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		employee.setId(1L);
		employees = new ArrayList<>();
		employees.add(employee);
	}
	
	@DisplayName("should return list with size 2 and emloyee with firstname='Test_firstName_2'")
	@Test
	public void create_Test() throws AsyncXAResourcesException {
		Employee employee2 = Employee.builder().firstName("Test_firstName_2").lastName("Test_lastName_2").age(100)
				.salary(new BigDecimal(5000.00d)).email(Email.builder().email("test_2@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_2").houseNumber(10).city("Test_city_2")
						.postCode("12345").build())
				.userData(User.builder().username("C_C_C").password("D_D_D")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		
		when(service.save(employee2)).thenAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				employees.add(employee2);
				return employee2;
			}
		});
		Employee returnedEmployee = controller.create(employee2);
		assertNotNull(employees);
		assertEquals(2, employees.size());
		assertEquals("Test_firstName_2", returnedEmployee.getFirstName());
	}
	
	@DisplayName("when AsyncXAResourcesException.class, then throw ResponseStatusException.class")
	@Test
	public void create_Test2() throws AsyncXAResourcesException {
		Employee employee2 = null;
		when(service.save(employee2)).thenThrow(AsyncXAResourcesException.class);
		assertThrows(ResponseStatusException.class, ()->{controller.create(employee2);});
	}
	
	@DisplayName("should return list with size 1")
	@Test
	public void list_Test() {
		when(service.fetchAll()).thenReturn(employees);
		List<Employee> returnedList = controller.list();
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@DisplayName("should return list with size 0")
	@Test
	public void deleteAllTest() {
		doAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				employees.clear();
				return null;
			}
		}).when(service).deleteAll();
		controller.deleteAll();
		
		assertNotNull(employees);
		assertEquals(0, employees.size());
	}
	
	@DisplayName("should return employee with id=1, firstname='Test_firstName_1', ...")
	@Test
	public void findByIdTest() {
		when(service.fetchById(anyLong())).thenReturn(employees.get(0));
		
		Employee returnedEmployee = controller.findById(Long.valueOf(1));
		
		assertNotNull(returnedEmployee);
		assertEquals(Long.valueOf(1), returnedEmployee.getId());
		assertEquals("Test_firstName_1", returnedEmployee.getFirstName());
		assertEquals("Test_lastName_1", returnedEmployee.getLastName());
		assertEquals(Integer.valueOf(100), returnedEmployee.getAge());
		assertEquals(BigDecimal.valueOf(5000), returnedEmployee.getSalary());
		assertEquals("test_1@googlemail.com", returnedEmployee.getEmail().getEmail());
		assertEquals("Test_city_1", returnedEmployee.getAddressData().getCity());
		assertEquals("A_A_A", returnedEmployee.getUserData().getUsername());
	}
	
	@DisplayName("should return list with size 0")
	@Test
	public void deleteByIdTest() {
		doAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				employees.remove(0);
				return null;
			}
		}).when(service).deleteById(anyLong());
		controller.deleteById(anyLong());
		
		assertNotNull(employees);
		assertEquals(0, employees.size());
	}
	
}
