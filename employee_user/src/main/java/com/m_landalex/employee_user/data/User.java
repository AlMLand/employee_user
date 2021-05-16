package com.m_landalex.employee_user.data;

import static java.util.Comparator.comparing;

import java.util.Collection;
import java.util.Comparator;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
public class User extends AbstractObject implements Comparable<User>{

	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	@Size(min = 5, max = 30, message = "{javax.validation.constraints.Size.message}")
	private String username;
	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	@Size(min = 5, max = 100, message = "{javax.validation.constraints.Size.message}")
	private String password;
	@NotEmpty(message = "{javax.validation.constraints.NotEmpty.message}")
	private Collection<@Valid Role> userRoles;
	
	private static final Comparator<User> COMPARATOR_USER = comparing(User::getUsername);
	
	@Override
	public int compareTo(User o) {
		return COMPARATOR_USER.compare(this, o);
	}

}
