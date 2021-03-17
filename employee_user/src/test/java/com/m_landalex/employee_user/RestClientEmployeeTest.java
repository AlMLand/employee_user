package com.m_landalex.employee_user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.client.RestTemplate;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;

@TestMethodOrder(OrderAnnotation.class)
public class RestClientEmployeeTest {

	private RestTemplate restTemplate;
	private static final String URL_CREATE_EMPLOYEE = "http://localhost:8080/employees/";
	private static final String URL_GET_ALL_EMPLOYEES = "http://localhost:8080/employees/";
	private static final String URL_DELETE_ALL_EMPLOYEES = "http://localhost:8080/employees/";
	private static final String URL_GET_EMPLOYEE_BY_ID = "http://localhost:8080/employees/{id}";
	private static final String URL_PUT_EMPLOYEE_BY_ID = "http://localhost:8080/employees/{id}";
	private static final String URL_DELETE_EMPLOYEE_BY_ID = "http://localhost:8080/employees/{id}";

	@BeforeEach
	public void setUp() {
		restTemplate = new RestTemplate();
	}

	@AfterEach
	public void tearDown() {
	}

	@Test
	@Order(5)
	public void testCreateEmployee() {
		Employee employee = Employee.builder().firstName("Test").lastName("McTest").age(100)
				.salary(new BigDecimal(5000.00d)).email(Email.builder().email("test@googlemail.com").build())
				.addressData(Address.builder().street("Test street").houseNumber(10).city("X city").postCode("12345")
						.build())
				.userData(User.builder().username("A_A_A").password("B_B_B").userRole(Role.DEVELOPMENT).build())
				.build();
		restTemplate.postForObject(URL_CREATE_EMPLOYEE, employee, Employee.class);
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		assertEquals(1, returnedArray.length);
	}

	@Test
	@Order(1)
	public void testFetchAllEmployees() {
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		assertEquals(2, returnedArray.length);
	}

	@Test
	@Order(4)
	public void testDeleteAllEmpoeeys() {
		restTemplate.delete(URL_DELETE_ALL_EMPLOYEES);
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		assertEquals(0, returnedArray.length);
	}

	@Test
	@Order(2)
	public void testFetchEmployeeById() {
		Employee returendEmployee = restTemplate.getForObject(URL_GET_EMPLOYEE_BY_ID, Employee.class, 1);
		assertEquals("McGregor", returendEmployee.getLastName());
	}

	@Test
	@Order(6)
	public void testUpdateEmployeeById() {
		Employee returnedEmployee = restTemplate.getForObject(URL_GET_EMPLOYEE_BY_ID, Employee.class, 3);
		returnedEmployee.setLastName("NewTestedLastName");
		restTemplate.put(URL_PUT_EMPLOYEE_BY_ID, returnedEmployee, 3);
		returnedEmployee = restTemplate.getForObject(URL_GET_EMPLOYEE_BY_ID, Employee.class, 3);
		assertEquals("NewTestedLastName", returnedEmployee.getLastName());
	}

	@Test
	@Order(3)
	public void testDeleteEmployeeById() {
		restTemplate.delete(URL_DELETE_EMPLOYEE_BY_ID, 1);
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		assertEquals(1, returnedArray.length);
	}

}
