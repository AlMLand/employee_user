package com.m_landalex.employee_user.data;

import static java.util.Comparator.*;

import java.util.Comparator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
	@Size(min = 5, max = 30, message = "{javax.validation.constraints.Size.message}")
	private String password;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}")
	private Role userRole;
	
	private static final Comparator<User> COMPARATOR_USER = comparing(User::getUsername)
			.thenComparing(User::getUserRole);
	
	@Override
	public int compareTo(User o) {
		return COMPARATOR_USER.compare(this, o);
	}

}
