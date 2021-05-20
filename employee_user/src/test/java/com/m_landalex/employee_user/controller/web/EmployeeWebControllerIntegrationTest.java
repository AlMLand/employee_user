package com.m_landalex.employee_user.controller.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.service.EmployeeService;

@ExtendWith(SpringExtension.class)

@WebMvcTest(controllers = EmployeeWebController.class)
public class EmployeeWebControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private EmployeeService service;
	private Employee employee;

	@BeforeEach
	public void setUp() {
		employee = Employee.builder().firstName("TEST_FIRSTNAME").lastName("TEST_LASTNAME")
				.birthDate(LocalDate.of(1990, 01, 01)).age(35).salary(new BigDecimal(5000.00d))
				.email(Email.builder().email("TEST_EMAIL@googlemail.com").build())
				.addressData(Address.builder().street("TEST_STREET").houseNumber(10).city("TEST_HOUSENUMBER")
						.postCode("12345").build())
				.userData(User.builder().username("TEST_USERNAME").password("TEST_PASSWORD")
						.userRoles(List.of(Role.builder().role("OFFICE").build())).build())
				.build();
	}

	@DisplayName("when all 13 employee variables are not valid, then return 13 errors count, verifying validation")
	@Test
	public void save_Test1() throws Exception {
		var employee2 = Employee.builder().firstName("a".repeat(200)).lastName("a".repeat(200))
				.birthDate(LocalDate.of(3000, 01, 01)).age(11).salary(new BigDecimal(50.00d))
				.email(Email.builder().email("TEST_EMAIL$googlemail.com").build())
				.addressData(Address.builder().street("a").houseNumber(0).city("a".repeat(200)).postCode("123").build())
				.userData(User.builder().username("a".repeat(50)).password("1234")
						.userRoles(List.of(Role.builder().role("").build())).build())
				.build();
		
		mockMvc.perform(post("/employees/")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", employee2.getFirstName())
				.param("lastName", employee2.getLastName())
				.param("birthDate", employee2.getBirthDate().toString())
				.param("age", Integer.valueOf(employee2.getAge()).toString())
				.param("salary", employee2.getSalary().toString())
				.param("email.email", employee2.getEmail().getEmail())
				.param("addressData.street", employee2.getAddressData().getStreet())
				.param("addressData.houseNumber", Integer.valueOf(employee2.getAddressData().getHouseNumber()).toString())
				.param("addressData.city", employee2.getAddressData().getCity())
				.param("addressData.postCode", employee2.getAddressData().getPostCode())
				.param("userData.username", employee2.getUserData().getUsername())
				.param("userData.password", employee2.getUserData().getPassword())
				.param("userData.userRoles.role", employee2.getUserData().getUserRoles().stream().findFirst().get().getRole())
				.with(csrf())
				.with(user("TESTER")))
		.andExpect(status().isOk())
		.andExpect(view().name("formationorupdate"))
		.andExpect(model().errorCount(13))
		.andExpect(model().attributeHasFieldErrors("employee", "firstName", "lastName", "birthDate", "age", "salary", "email.email",
				"addressData.street", "addressData.houseNumber", "addressData.city", "userData.username", "userData.password",
				"userData.userRoles"))
		.andExpect(model().attribute("id", is(nullValue())))
		.andExpect(model().attribute("version", is(nullValue())));
		verifyNoInteractions(service);
	}
	
	@DisplayName("when HTTP status 302, then save employee, render view and HTTP 302 redirected")
	@Test
	public void save_Test2() throws JsonProcessingException, Exception {
		when(service.save(any(Employee.class))).thenReturn(employee);
		mockMvc.perform(post("/employees/")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", employee.getFirstName())
				.param("lastName", employee.getLastName())
				.param("birthDate", employee.getBirthDate().toString())
				.param("age", Integer.valueOf(employee.getAge()).toString())
				.param("salary", employee.getSalary().toString())
				.param("email.email", employee.getEmail().getEmail())
				.param("addressData.street", employee.getAddressData().getStreet())
				.param("addressData.houseNumber", Integer.valueOf(employee.getAddressData().getHouseNumber()).toString())
				.param("addressData.city", employee.getAddressData().getCity())
				.param("addressData.postCode", employee.getAddressData().getPostCode())
				.param("userData.username", employee.getUserData().getUsername())
				.param("userData.password", employee.getUserData().getPassword())
				.param("userData.userRoles", employee.getUserData().getUserRoles().toString())
				.with(csrf())
				.with(user("TESTER")))
		.andExpect(status().isFound())
		.andExpect(view().name("redirect:/employees/showings/" + employee.getId()))
		.andExpect(redirectedUrl("/employees/showings/" + employee.getId()));
		
		ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
		verify(service, timeout(1)).save(captor.capture());
		var captorEmployee = captor.getValue();
		
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
		assertEquals(employee.getUserData().getUserRoles().stream().findFirst().get().getRole(), 
				captorEmployee.getUserData().getUserRoles().stream().findFirst().get().getRole()
				.substring(1, captorEmployee.getUserData().getUserRoles().stream().findFirst().get().getRole().length() - 1));
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void save_Test() throws Exception {
		mockMvc.perform(post("/employees/")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", employee.getFirstName())
				.param("lastName", employee.getLastName())
				.param("birthDate", employee.getBirthDate().toString())
				.param("age", Integer.valueOf(employee.getAge()).toString())
				.param("salary", employee.getSalary().toString())
				.param("email.email", employee.getEmail().getEmail())
				.param("addressData.street", employee.getAddressData().getStreet())
				.param("addressData.houseNumber", Integer.valueOf(employee.getAddressData().getHouseNumber()).toString())
				.param("addressData.city", employee.getAddressData().getCity())
				.param("addressData.postCode", employee.getAddressData().getPostCode())
				.param("userData.username", employee.getUserData().getUsername())
				.param("userData.password", employee.getUserData().getPassword())
				.param("userData.userRoles", employee.getUserData().getUserRoles().toString())
				.with(csrf()))
		.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when status HTTP 200, then return blank model 'employee' without attributes")
	@Test
	public void formationNew_Test1() throws Exception {
		mockMvc.perform(get("/employees/formations/")
				.with(user("TESTER")))
		.andExpect(status().isOk())
		.andExpect(view().name("formationorupdate"))
		.andExpect(model().attributeExists("employee"))
		.andExpect(model().hasNoErrors())
		.andExpect(model().attributeDoesNotExist("firstName", "lastName", "birthDate", "age", "salary", "email.email",
				"addressData.street", "addressData.houseNumber", "addressData.city", "userData.username", "userData.password",
				"userData.userRoles"));
		verifyNoInteractions(service);
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void formations_Test2() throws Exception {
		mockMvc.perform(get("/employees/formations/"))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when employee HTTP status 200, then add model and render view")
	@Test
	public void updateById_Test1() throws Exception {
		when(service.fetchById(Long.valueOf(1))).thenReturn(employee);
		mockMvc.perform(get("/employees/updatings/{id}", Long.valueOf(1))
				.with(user("TESTER")))
		.andExpect(status().isOk())
		.andExpect(view().name("formationorupdate"))
		.andExpect(model().attributeExists("employee"))
		.andExpect(model().hasNoErrors())
		.andExpect(model().attribute("employee", hasProperty("firstName", is(employee.getFirstName()))))
		.andExpect(model().attribute("employee", hasProperty("lastName", is(employee.getLastName()))))
		.andExpect(model().attribute("employee", hasProperty("birthDate", is(employee.getBirthDate()))))
		.andExpect(model().attribute("employee", hasProperty("age", is(employee.getAge()))))
		.andExpect(model().attribute("employee", hasProperty("salary", is(employee.getSalary()))))
		.andExpect(model().attribute("employee", hasProperty("email", is(employee.getEmail()))))
		.andExpect(model().attribute("employee", hasProperty("addressData", is(employee.getAddressData()))))
		.andExpect(model().attribute("employee", hasProperty("userData", is(employee.getUserData()))));
		verify(service, timeout(1)).fetchById(anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void updateById_Test2() throws Exception {
		mockMvc.perform(get("/employees/updatings/{id}", Long.valueOf(1)))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when employee HTTP status 200, then add model and render view")
	@Test
	public void showById_Test1() throws JsonProcessingException, Exception {
		when(service.fetchById(anyLong())).thenReturn(employee);
		mockMvc.perform(get("/employees/showings/{id}", Long.valueOf(1))
				.with(user("TESTER")))
				.andExpect(status().isOk())
				.andExpect(view().name("detailsemployee"))
				.andExpect(model().attributeExists("employee"))
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute("employee", hasProperty("firstName", is(employee.getFirstName()))))
				.andExpect(model().attribute("employee", hasProperty("lastName", is(employee.getLastName()))))
				.andExpect(model().attribute("employee", hasProperty("birthDate", is(employee.getBirthDate()))))
				.andExpect(model().attribute("employee", hasProperty("age", is(employee.getAge()))))
				.andExpect(model().attribute("employee", hasProperty("salary", is(employee.getSalary()))))
				.andExpect(model().attribute("employee", hasProperty("email", is(employee.getEmail()))))
				.andExpect(model().attribute("employee", hasProperty("addressData", is(employee.getAddressData()))))
				.andExpect(model().attribute("employee", hasProperty("userData", is(employee.getUserData()))));
				verify(service, timeout(1)).fetchById(anyLong());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void showById_Test2() throws Exception {
		mockMvc.perform(get("/employees/showings/{id}", Long.valueOf(1)))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when HTTP status 200, then add to model 1 Employee==employee and render view 'listemployees'")
	@Test
	public void showAll_Test1() throws Exception {
		when(service.fetchAll()).thenReturn(List.of(employee));
		mockMvc.perform(get("/employees/showings/")
				.with(user("TESTER")))
		.andExpect(status().isOk())
		.andExpect(view().name("listemployees"))
		.andExpect(model().hasNoErrors())
		.andExpect(model().attributeExists("employees"))
		.andExpect(model().attribute("employees", hasSize(1)))
		.andExpect(model().attribute("employees", hasItem(
				allOf(
						hasProperty("firstName", is(employee.getFirstName())),
						hasProperty("lastName", is(employee.getLastName())),
						hasProperty("birthDate", is(employee.getBirthDate())),
						hasProperty("age", is(employee.getAge())),
						hasProperty("salary", is(employee.getSalary())),
						hasProperty("email", is(employee.getEmail())),
						hasProperty("addressData", is(employee.getAddressData())),
						hasProperty("userData", is(employee.getUserData()))
						)
				)));
		verify(service, timeout(1)).fetchAll();
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401 - unauthorized, verifying security")
	@Test
	public void showAll_Test2() throws Exception {
		mockMvc.perform(get("/employees/showings/"))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
	@DisplayName("when HTTP 302, then render view, redirect and list employees size==0")
	@Test
	public void deleteById_Test1() throws Exception {
		var employees = new ArrayList<Employee>();
		employees.add(employee);
		assertEquals(1, employees.size());
		
		doAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				employees.remove(0);
				return null;
			}
		}).when(service).deleteById(anyLong());
		
		mockMvc.perform(get("/employees/remove/{id}", Long.valueOf(1))
				.with(user("TESTER")))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/employees/showings"))
		.andExpect(view().name("redirect:/employees/showings"));
		
		assertNotNull(employees);
		assertEquals(0, employees.size());
		verify(service, timeout(1)).deleteById(anyLong());
	}

	@DisplayName("when not authorized user, then return status HTTP 401, verifying security")
	@Test
	public void deleteById_Test2() throws Exception {
		mockMvc.perform(get("/employees/remove/{id}", Long.valueOf(1)))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}

	@DisplayName("when HTTP status 200, then add to model 1 Employee==employee and render view 'listemployees'")
	@Test
	public void showByLastname_Test1() throws Exception {
		when(service.fetchByLastName(anyString())).thenReturn(List.of(employee));
		
		mockMvc.perform(get("/employees/showings/lastname")
				.param("lastname", "a".repeat(10))
				.with(user("TESTER")))
		.andExpect(status().isOk())
		.andExpect(view().name("listemployees"))
		.andExpect(model().hasNoErrors())
		.andExpect(model().attributeExists("employees"))
		.andExpect(model().attribute("employees", hasSize(1)))
		.andExpect(model().attribute("employees", hasItem(
				allOf(
						hasProperty("firstName", is(employee.getFirstName())),
						hasProperty("lastName", is(employee.getLastName())),
						hasProperty("birthDate", is(employee.getBirthDate())),
						hasProperty("age", is(employee.getAge())),
						hasProperty("salary", is(employee.getSalary())),
						hasProperty("email", is(employee.getEmail())),
						hasProperty("addressData", is(employee.getAddressData())),
						hasProperty("userData", is(employee.getUserData()))
						)
				)));
		verify(service, timeout(1)).fetchByLastName(anyString());
	}
	
	@DisplayName("when not authorized user, then return status HTTP 401, verifying security")
	@Test
	public void showByLastname_Test2() throws Exception {
		mockMvc.perform(get("/employees/showings/lastname")
				.param("lastname", "a".repeat(10)))
				.andExpect(status().isUnauthorized());
		verifyNoInteractions(service);
	}
	
}
