package com.m_landalex.employee_user.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
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

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmployeeService;
import com.m_landalex.employee_user.view.controller.rest.EmployeeRestController;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

	@Mock
	private EmployeeService mockedEmployeeService;
	private EmployeeRestController employeeController;
	private List<Employee> listEmployees;
	
	@BeforeEach
	public void setUp() {
		listEmployees  = new ArrayList<>();
		employeeController = new EmployeeRestController();
		ReflectionTestUtils.setField(employeeController, "service", mockedEmployeeService);
		Employee employee = Employee.builder().firstName("Test_firstName_1").lastName("Test_lastName_1").age(100)
				.salary(new BigDecimal(5000.00d)).email(Email.builder().email("test_1@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_1").houseNumber(10).city("Test_city_1").postCode("12345")
						.build())
				.userData(User.builder().username("A_A_A").password("B_B_B").userRoles(
						List.of(Role.builder().role("DEVELOPMENT").build())
						).build())
				.build();
		employee.setId(1L);
		listEmployees.add(employee);
	}
	
	@Test
	public void create_Test() throws AsyncXAResourcesException {
		Employee newEmployee = Employee.builder().firstName("Test_firstName_2").lastName("Test_lastName_2").age(100)
				.salary(new BigDecimal(5000.00d)).email(Email.builder().email("test_2@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_2").houseNumber(10).city("Test_city_2").postCode("12345")
						.build())
				.userData(User.builder().username("C_C_C").password("D_D_D").userRoles(
						List.of(Role.builder().role("DEVELOPMENT").build())
						).build())
				.build();
		
		Mockito.when(mockedEmployeeService.save(newEmployee)).thenAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				listEmployees.add(newEmployee);
				return newEmployee;
			}
		});
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("createEmployee", employeeController.create(newEmployee));
		Employee returnedNewEmployee = (Employee) extendedModelMap.get("createEmployee");
		
		assertNotNull(listEmployees);
		assertEquals(2, listEmployees.size());
		assertEquals("Test_firstName_2", returnedNewEmployee.getFirstName());
	}
	
	@Test
	public void create_ShouldThrowRuntimeExceptiontest() throws AsyncXAResourcesException {
		Employee newEmployee = null;
		Mockito.when(mockedEmployeeService.save(newEmployee)).thenThrow(RuntimeException.class);
		assertThrows(RuntimeException.class, ()->{
			employeeController.create(newEmployee);
		});
	}
	
	@Test
	public void list_Test() {
		Mockito.when(mockedEmployeeService.fetchAll()).thenReturn(listEmployees);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("list", employeeController.list());
		@SuppressWarnings("unchecked")
		List<Employee> returnedList = (List<Employee>) extendedModelMap.get("list");
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@Test
	public void deleteAllTest() {
		Mockito.doAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				listEmployees.clear();
				return null;
			}
		}).when(mockedEmployeeService).deleteAll();
		
		employeeController.deleteAll();
		
		assertNotNull(listEmployees);
		assertEquals(0, listEmployees.size());
	}
	
	@Test
	public void findByIdTest() {
		Mockito.when(mockedEmployeeService.fetchById(Mockito.anyLong())).thenReturn(listEmployees.get(0));
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("findById", employeeController.findById(Mockito.anyLong()));
		Employee returnedEmployee = (Employee) extendedModelMap.get("findById");
		
		assertNotNull(returnedEmployee);
		assertEquals(Long.valueOf(1L), returnedEmployee.getId());
		assertEquals("Test_firstName_1", returnedEmployee.getFirstName());
		assertEquals("Test_lastName_1", returnedEmployee.getLastName());
		assertEquals(Integer.valueOf(100), returnedEmployee.getAge());
		assertEquals(BigDecimal.valueOf(5000), returnedEmployee.getSalary());
		assertEquals("test_1@googlemail.com", returnedEmployee.getEmail().getEmail());
		assertEquals("Test_city_1", returnedEmployee.getAddressData().getCity());
		assertEquals("A_A_A", returnedEmployee.getUserData().getUsername());
	}
	
	@Test
	public void deleteByIdTest() {
		Mockito.doAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				listEmployees.remove(0);
				return null;
			}
		}).when(mockedEmployeeService).deleteById(Mockito.anyLong());
		
		employeeController.deleteById(999L);
		
		assertNotNull(listEmployees);
		assertEquals(0, listEmployees.size());
	}
	
}
