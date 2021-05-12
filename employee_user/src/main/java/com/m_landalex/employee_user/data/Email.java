package com.m_landalex.employee_user.data;

import static java.util.Comparator.*;

import java.util.Comparator;

import javax.validation.constraints.NotBlank;

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
public class Email extends AbstractObject implements Comparable<Email> {

	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	@javax.validation.constraints.Email(message = "{javax.validation.constraints.Email.message}")
	private String email;

	private static final Comparator<Email> COMPARATOR_EMAIL = comparing(Email::getEmail);
	
	@Override
	public int compareTo(Email o) {
		return COMPARATOR_EMAIL.compare(this, o);
	}

}
