package com.m_landalex.employee_user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.m_landalex.employee_user.view.controller.EmployeeController;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

	@Mock
	private EmployeeService mockedEmployeeService;
	private EmployeeController employeeController;
	private List<Employee> listEmployees;
	
	@BeforeEach
	public void setUp() {
		listEmployees  = new ArrayList<>();
		employeeController = new EmployeeController();
		Employee employee = Employee.builder().firstName("Test_firstName_1").lastName("Test_lastName_1").age(100)
				.salary(new BigDecimal(5000.00d)).email(Email.builder().email("test_1@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_1").houseNumber(10).city("Test_city_1").postCode("12345")
						.build())
				.userData(User.builder().username("A_A_A").password("B_B_B").userRole(Role.DEVELOPMENT).build())
				.build();
		employee.setId(1L);
		listEmployees.add(employee);
	}
	
	@Test
	public void createEmployeeTest() throws AsyncXAResourcesException {
		Employee newEmployee = Employee.builder().firstName("Test_firstName_2").lastName("Test_lastName_2").age(100)
				.salary(new BigDecimal(5000.00d)).email(Email.builder().email("test_2@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_2").houseNumber(10).city("Test_city_2").postCode("12345")
						.build())
				.userData(User.builder().username("C_C_C").password("D_D_D").userRole(Role.DEVELOPMENT).build())
				.build();
		
		Mockito.when(mockedEmployeeService.save(newEmployee)).thenAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				listEmployees.add(newEmployee);
				return newEmployee;
			}
		});
		ReflectionTestUtils.setField(employeeController, "employeeService", mockedEmployeeService);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("createEmployee", employeeController.createEmployee(newEmployee));
		Employee returnedNewEmployee = (Employee) extendedModelMap.get("createEmployee");
		
		assertNotNull(listEmployees);
		assertEquals(2, listEmployees.size());
		assertEquals("Test_firstName_2", returnedNewEmployee.getFirstName());
	}
	
	@Test
	public void createEmployeeShouldThrowRuntimeExceptiontest() throws AsyncXAResourcesException {
		Employee newEmployee = null;
		Mockito.when(mockedEmployeeService.save(newEmployee)).thenThrow(RuntimeException.class);
		ReflectionTestUtils.setField(employeeController, "employeeService", mockedEmployeeService);
		assertThrows(RuntimeException.class, ()->{
			employeeController.createEmployee(newEmployee);
		});
	}
	
	@Test
	public void fetchAllEmployeesTest() {
		Mockito.when(mockedEmployeeService.fetchAll()).thenReturn(listEmployees);
		ReflectionTestUtils.setField(employeeController, "employeeService", mockedEmployeeService);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("fetchAllEmployees", employeeController.fetchAllEmployees());
		@SuppressWarnings("unchecked")
		List<Employee> returnedList = (List<Employee>) extendedModelMap.get("fetchAllEmployees");
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@Test
	public void deleteAllEmployeesTest() {
		Mockito.doAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				listEmployees.clear();
				return null;
			}
		}).when(mockedEmployeeService).deleteAll();
		ReflectionTestUtils.setField(employeeController, "employeeService", mockedEmployeeService);
		
		employeeController.deleteAllEmployees();
		
		assertNotNull(listEmployees);
		assertEquals(0, listEmployees.size());
	}
	
	@Test
	public void fetchEmployeeByIdTest() {
		Mockito.when(mockedEmployeeService.fetchById(Mockito.anyLong())).thenReturn(listEmployees.get(0));
		ReflectionTestUtils.setField(employeeController, "employeeService", mockedEmployeeService);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("fetchEmployeeById", employeeController.fetchEmployeeById(Mockito.anyLong()));
		Employee returnedEmployee = (Employee) extendedModelMap.get("fetchEmployeeById");
		
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
	public void updateEmployeeByIdTest() throws AsyncXAResourcesException {
		Employee employee = Employee.builder().firstName("Test_firstName_2").lastName("Test_lastName_2").age(10)
				.salary(new BigDecimal(1000.00d)).email(Email.builder().email("test_2@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_2").houseNumber(12).city("Test_city_2").postCode("56789")
						.build())
				.userData(User.builder().username("C_C_C").password("D_D_D").userRole(Role.ADMINISTRATION).build())
				.build();
		
		Mockito.when(mockedEmployeeService.update(Mockito.any(Employee.class))).thenAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				Employee returnedEmployee = listEmployees.get(0);
				returnedEmployee.setFirstName(employee.getFirstName());
				returnedEmployee.setLastName(employee.getLastName());
				returnedEmployee.setAge(employee.getAge());
				returnedEmployee.setAddressData(employee.getAddressData());
				returnedEmployee.setSalary(employee.getSalary());
				returnedEmployee.setEmail(employee.getEmail());
				returnedEmployee.setUserData(employee.getUserData());
				listEmployees.remove(0);
				listEmployees.add(returnedEmployee);
				return returnedEmployee;
			}
		});
		ReflectionTestUtils.setField(employeeController, "employeeService", mockedEmployeeService);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("epdateEmployeeById", employeeController.updateEmployeeById(1L, employee));
		Employee updatedEmployee = (Employee) extendedModelMap.get("epdateEmployeeById");
		
		assertNotNull(updatedEmployee);
		assertEquals(1, listEmployees.size());
		assertEquals("Test_firstName_2", updatedEmployee.getFirstName());
	}
	
	@Test
	public void deleteEmployeeByIdTest() {
		Mockito.doAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				listEmployees.remove(0);
				return null;
			}
		}).when(mockedEmployeeService).deleteById(Mockito.anyLong());
		ReflectionTestUtils.setField(employeeController, "employeeService", mockedEmployeeService);
		
		employeeController.deleteEmployeeById(999L);
		
		assertNotNull(listEmployees);
		assertEquals(0, listEmployees.size());
	}
	
}
