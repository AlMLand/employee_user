package com.m_landalex.employee_user.data;

import static java.util.Comparator.comparing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Employee extends AbstractObject implements Comparable<Employee> {
	
	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	@Size(min = 2, max = 100, message = "{javax.validation.constraints.Size.message}")
	private String firstName;
	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	@Size(min = 2, max = 100, message = "{javax.validation.constraints.Size.message}")
	private String lastName;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	@Past( message = "{javax.validation.constraints.Past.message}" )
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}")
	private int age;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}")
	private BigDecimal salary;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}")
	private Email email;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}")
	private Address addressData;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}")
	private User userData;
	
	private static final Comparator<Employee> COMPARATOR_EMPLOYEE = comparing(Employee::getLastName)
			.thenComparing(Employee::getSalary);

	@Override
	public int compareTo(Employee o) {
		return COMPARATOR_EMPLOYEE.compare(this, o);
	}

}
