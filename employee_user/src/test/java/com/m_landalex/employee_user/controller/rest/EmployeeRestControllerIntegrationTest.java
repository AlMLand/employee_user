package com.m_landalex.employee_user.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.service.EmployeeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeRestController.class)
public class EmployeeRestControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private EmployeeService service;
	private Employee employee;
	
	@BeforeEach
	public void setUp() {
		employee = Employee.builder().firstName("Test_firstName_1").lastName("Test_lastName_1")
				.birthDate(LocalDate.of(1995, 01, 01)).age(100).salary(new BigDecimal(5000.00d))
				.email(Email.builder().email("test_1@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_1").houseNumber(10).city("Test_city_1")
						.postCode("12345").build())
				.userData(User.builder().username("A_A_A").password("B_B_B")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		employee.setId(1L);
	}
	
	@DisplayName("when valid input the return HTTP 200, verifying json deserealization from HTTP Request")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test1() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee)))
		.andExpect(status().isOk());
		verify(service, timeout(1)).save(any(Employee.class));
	}
	
	@DisplayName("when firstname too long, then return HTTP 400, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test2() throws JsonProcessingException, Exception {
		var employee2 = Employee.builder().firstName("a".repeat(200)).lastName("TEST_LASTNAME")
				.birthDate(LocalDate.of(1985, 01, 01)).age(100).salary(new BigDecimal(5000.00d))
				.email(Email.builder().email("test_2@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_2").houseNumber(10).city("Test_city_2")
						.postCode("12345").build())
				.userData(User.builder().username("tester2").password("12345")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		when(service.save(any(Employee.class))).thenReturn(employee2);
		
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee2)))
		.andExpect(status().isBadRequest());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when lastname too long, then return HTTP 400, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test3() throws JsonProcessingException, Exception {
		var employee2 = Employee.builder().firstName("TEST_FIRSTNAME").lastName("a".repeat(200))
				.birthDate(LocalDate.of(1985, 01, 01)).age(100).salary(new BigDecimal(5000.00d))
				.email(Email.builder().email("test_2@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_2").houseNumber(10).city("Test_city_2")
						.postCode("12345").build())
				.userData(User.builder().username("tester2").password("12345")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		when(service.save(any(Employee.class))).thenReturn(employee2);
		
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee2)))
		.andExpect(status().isBadRequest());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when birthdate is in future, then return HTTP 400, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test4() throws JsonProcessingException, Exception {
		var employee2 = Employee.builder().firstName("TEST_FIRSTNAME").lastName("TEST_LASTNAME")
				.birthDate(LocalDate.of(2085, 01, 01)).age(100).salary(new BigDecimal(5000.00d))
				.email(Email.builder().email("test_1@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_1").houseNumber(10).city("Test_city_1")
						.postCode("12345").build())
				.userData(User.builder().username("tester2").password("12345")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		when(service.save(any(Employee.class))).thenReturn(employee2);
		
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee2)))
		.andExpect(status().isBadRequest());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when age less than 18, then return HTTP 400, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test5() throws JsonProcessingException, Exception {
		var employee2 = Employee.builder().firstName("TEST_FIRSTNAME").lastName("TEST_LASTNAME")
				.birthDate(LocalDate.of(1985, 01, 01)).age(17).salary(new BigDecimal(5000.00d))
				.email(Email.builder().email("test_2@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_2").houseNumber(10).city("Test_city_2")
						.postCode("12345").build())
				.userData(User.builder().username("tester2").password("12345")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		when(service.save(any(Employee.class))).thenReturn(employee2);
		
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee2)))
		.andExpect(status().isBadRequest());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when salary less than 500, then return HTTP 400, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test6() throws JsonProcessingException, Exception {
		var employee2 = Employee.builder().firstName("TEST_FIRSTNAME").lastName("TEST_LASTNAME")
				.birthDate(LocalDate.of(1985, 01, 01)).age(22).salary(new BigDecimal(50.00d))
				.email(Email.builder().email("test_2@googlemail.com").build())
				.addressData(Address.builder().street("Test_street_2").houseNumber(10).city("Test_city_2")
						.postCode("12345").build())
				.userData(User.builder().username("tester2").password("12345")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		when(service.save(any(Employee.class))).thenReturn(employee2);
		
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee2)))
		.andExpect(status().isBadRequest());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when email=null, then return HTTP 400, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test7() throws JsonProcessingException, Exception {
		var employee2 = Employee.builder().firstName("TEST_FIRSTNAME").lastName("TEST_LASTNAME")
				.birthDate(LocalDate.of(1985, 01, 01)).age(22).salary(new BigDecimal(5000.00d)).email(null)
				.addressData(Address.builder().street("Test_street_2").houseNumber(10).city("Test_city_2")
						.postCode("12345").build())
				.userData(User.builder().username("tester2").password("12345")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		when(service.save(any(Employee.class))).thenReturn(employee2);
		
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee2)))
		.andExpect(status().isBadRequest());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when address=null, then return HTTP 400, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test8() throws JsonProcessingException, Exception {
		var employee2 = Employee.builder().firstName("TEST_FIRSTNAME").lastName("TEST_LASTNAME")
				.birthDate(LocalDate.of(1985, 01, 01)).age(22).salary(new BigDecimal(5000.00d))
				.email(Email.builder().email("test_2@googlemail.com").build()).addressData(null)
				.userData(User.builder().username("tester2").password("12345")
						.userRoles(List.of(Role.builder().role("DEVELOPMENT").build())).build())
				.build();
		when(service.save(any(Employee.class))).thenReturn(employee2);
		
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee2)))
		.andExpect(status().isBadRequest());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when user=null, then return HTTP 400, verifying validation")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test9() throws JsonProcessingException, Exception {
		var employee2 = Employee.builder().firstName("TEST_FIRSTNAME").lastName("TEST_LASTNAME")
				.birthDate(LocalDate.of(1985, 01, 01)).age(22).salary(new BigDecimal(5000.00d))
				.email(Email.builder().email("test_2@googlemail.com").build()).addressData(Address.builder()
						.street("Test_street_2").houseNumber(10).city("Test_city_2").postCode("12345").build())
				.userData(null).build();
		when(service.save(any(Employee.class))).thenReturn(employee2);
		
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee2)))
		.andExpect(status().isBadRequest());
		verifyNoInteractions(service);
	}
	
	@DisplayName("whne valid input, then map to business model")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test10() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee)))
		.andExpect(status().isOk());
		
		ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
		verify(service, timeout(1)).save(captor.capture());
		Employee captorEmployee = captor.getValue();
		
		assertNotNull(captorEmployee);
		assertEquals(employee.getId(), captorEmployee.getId());
		assertEquals(employee.getVersion(), captorEmployee.getVersion());
		assertEquals(employee.getFirstName(), captorEmployee.getFirstName());
		assertEquals(employee.getLastName(), captorEmployee.getLastName());
		assertEquals(employee.getBirthDate(), captorEmployee.getBirthDate());
		assertEquals(employee.getAge(), captorEmployee.getAge());
		assertEquals(employee.getSalary(), captorEmployee.getSalary());
		assertEquals(employee.getEmail().getEmail(), captorEmployee.getEmail().getEmail());
		assertEquals(employee.getAddressData().getStreet(), captorEmployee.getAddressData().getStreet());
		assertEquals(employee.getAddressData().getHouseNumber(), captorEmployee.getAddressData().getHouseNumber());
		assertEquals(employee.getAddressData().getCity(), captorEmployee.getAddressData().getCity());
		assertEquals(employee.getAddressData().getPostCode(), captorEmployee.getAddressData().getPostCode());
		assertEquals(employee.getUserData().getUsername(), captorEmployee.getUserData().getUsername());
		assertEquals(employee.getUserData().getPassword(), captorEmployee.getUserData().getPassword());
		assertEquals(employee.getUserData().getUserRoles().size(), captorEmployee.getUserData().getUserRoles().size());
		assertEquals(employee.getUserData().getUserRoles().stream().findFirst().get().getRole(), 
				captorEmployee.getUserData().getUserRoles().stream().findFirst().get().getRole());
		verify(service, timeout(1)).save(any(Employee.class));
	}
	
	@DisplayName("when valid input, then return employee, verifying deserialization from HTTP Request and serialization to HTTP Response")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void create_Test11() throws JsonProcessingException, Exception {
		when(service.save(any(Employee.class))).thenReturn(employee);
		MvcResult result = mockMvc.perform(post("/rest/employees/").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(employee)))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponse = mapper.writeValueAsString(employee);
		var actuallyResponse = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponse);
		assertThat(expectedResponse).isEqualToIgnoringWhitespace(actuallyResponse);
		verify(service, timeout(1)).save(any(Employee.class));
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void create_Test12() throws Exception {
		mockMvc.perform(post("/rest/employees/").with(csrf()))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, should return 1 employeeObject what is equal to employee, verifying serialization to HTTP Response")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test1() throws JsonProcessingException, Exception {
		when(service.fetchAll()).thenReturn(List.of(employee));
		MvcResult result = mockMvc.perform(get("/rest/employees/"))
				.andExpect(status().isOk())
				.andReturn();
				
		
		var expectedResponse = mapper.writeValueAsString(List.of(employee));
		var actuallyResponse = result.getResponse().getContentAsString();
		
		assertNotNull(actuallyResponse);
		assertNotEquals("", actuallyResponse);
		assertThat(expectedResponse).isEqualToIgnoringWhitespace(actuallyResponse);
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when status HTTP 200, then should return return 1 employeeObject what is equal to employee")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void list_Test2() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(employee));
		mockMvc.perform(get("/rest/employees/"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$[0].lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$[0].birthDate", is(employee.getBirthDate().toString())))
				.andExpect(jsonPath("$[0].age", is(employee.getAge())))
				.andExpect(jsonPath("$[0].salary", is(Integer.valueOf(employee.getSalary().toString()))))
				.andExpect(jsonPath("$[0].email.email", is(employee.getEmail().getEmail())))
				.andExpect(jsonPath("$[0].addressData.street", is(employee.getAddressData().getStreet())))
				.andExpect(jsonPath("$[0].addressData.houseNumber", is(employee.getAddressData().getHouseNumber())))
				.andExpect(jsonPath("$[0].addressData.city", is(employee.getAddressData().getCity())))
				.andExpect(jsonPath("$[0].addressData.postCode", is(employee.getAddressData().getPostCode())))
				.andExpect(jsonPath("$[0].userData.username", is(employee.getUserData().getUsername())))
				.andExpect(jsonPath("$[0].userData.password", is(employee.getUserData().getPassword())))
				.andExpect(jsonPath("$[0].userData.userRoles[0].role", 
						is(employee.getUserData().getUserRoles().stream().findFirst().get().getRole())));
		
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void list_Test3() throws Exception {
		mockMvc.perform(get("/rest/employees/"))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, then should return list with size 0")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void deleteAll_Test() throws Exception {
		var employees = new ArrayList<>();
		employees.add(employee);
		assertEquals(1, employees.size());
		
		doAnswer(invocation -> {employees.clear(); return null;}).when(service).deleteAll();
		
		mockMvc.perform(delete("/rest/employees/").with(csrf()))
				.andExpect(status().isOk());
		
		assertNotNull(employees);
		assertEquals(0, employees.size());
		verify(service, timeout(1)).deleteAll();
	}
	
	@DisplayName("when status HTTP 200, then return employeeObject equal is to employee")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void findById_Test1() throws Exception {
		when(service.fetchById(anyLong())).thenReturn(employee);
		mockMvc.perform(get("/rest/employees/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.birthDate", is(employee.getBirthDate().toString())))
				.andExpect(jsonPath("$.age", is(employee.getAge())))
				.andExpect(jsonPath("$.salary", is(Integer.valueOf(employee.getSalary().toString()))))
				.andExpect(jsonPath("$.email.email", is(employee.getEmail().getEmail())))
				.andExpect(jsonPath("$.addressData.street", is(employee.getAddressData().getStreet())))
				.andExpect(jsonPath("$.addressData.houseNumber", is(employee.getAddressData().getHouseNumber())))
				.andExpect(jsonPath("$.addressData.city", is(employee.getAddressData().getCity())))
				.andExpect(jsonPath("$.addressData.postCode", is(employee.getAddressData().getPostCode())))
				.andExpect(jsonPath("$.userData.username", is(employee.getUserData().getUsername())))
				.andExpect(jsonPath("$.userData.password", is(employee.getUserData().getPassword())))
				.andExpect(jsonPath("$.userData.userRoles[0].role", 
						is(employee.getUserData().getUserRoles().stream().findFirst().get().getRole())));
		
		verify(service, timeout(1)).fetchById(anyLong());
	}
	
	@DisplayName("when status HTTP 200, then expectedResponseBody and actuallyResponseBody are equal")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void findById_Test2() throws Exception {
		when(service.fetchById(anyLong())).thenReturn(employee);
		MvcResult result = mockMvc.perform(get("/rest/employees/{id}", 1L))
				.andExpect(status().isOk())
				.andReturn();
		
		var expectedResponseBody = mapper.writeValueAsString(employee);
		var actuallyResponseBody = result.getResponse().getContentAsString();
		
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuallyResponseBody);
		verify(service, timeout(1)).fetchById(anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void findById_Test() throws Exception {
		mockMvc.perform(get("/rest/employees/{id}", 1L))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, then list return size 0")
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	@Test
	public void deleteById_Test1() throws Exception {
		var employees = new ArrayList<>();
		employees.add(employee);
		assertEquals(1, employees.size());
		
		doAnswer(invocation -> employees.remove(0)).when(service).deleteById(anyLong());
		
		mockMvc.perform(delete("/rest/employees/{id}", 1L).with(csrf()))
				.andExpect(status().isOk());
		
		assertNotNull(employees);
		assertEquals(0, employees.size());
		verify(service, timeout(1)).deleteById(anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void deleteById_Test2() throws Exception {
		mockMvc.perform(delete("/rest/employees/{id}", 1L).with(csrf()))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
}
