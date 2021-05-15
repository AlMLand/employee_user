package com.m_landalex.employee_user.service.dbinitialization;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmployeeService;

@Profile("!test")
@Service
public class DBInitialization {

	@Autowired
	private EmployeeService employeeService;

	@PostConstruct
	public void setupInitialization() throws AsyncXAResourcesException {

		employeeService.save(
				Employee.builder().firstName("Connor").lastName("Mcgregor").birthDate(LocalDate.of(1988, 07, 14))
				.age(32).salary(new BigDecimal(5000.00d))
				.email(
						Email.builder().email("mcgregor@googlemail.com").build()
						)
				.addressData(
						Address.builder().street("Aaaa street").houseNumber(10).city("Dublin").postCode("12345").build()
						)
				.userData(
						User.builder().username("A_A_A").password("B_B_B")
						.userRoles(List.of(Role.builder().role("ADMINISTRATOR").build())).build()
						)
				.build()
				);

		employeeService.save(
				Employee.builder().firstName("Dustin").lastName("Poirier").birthDate(LocalDate.of(1989, 01, 19))
				.age(31).salary(new BigDecimal(4000.00d))
				.email(
						Email.builder().email("poirier@googlemail.com").build()
						)
				.addressData(
						Address.builder().street("Bbbb street").houseNumber(10).city("Atlanta").postCode("67890").build()
						)
				.userData(
						User.builder().username("C_C_C").password("D_D_D")
						.userRoles(List.of(Role.builder().role("ADMINISTRATOR").build())).build()
						)
				.build()
				);

	}

}
