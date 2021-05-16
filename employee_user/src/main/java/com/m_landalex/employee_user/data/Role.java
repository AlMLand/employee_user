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
public class Role extends AbstractObject implements Comparable<Role> {

	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	private String role;

	private static final Comparator<Role> COMPARATOR_ROLE = comparing(Role::getRole);

	@Override
	public int compareTo(Role o) {
		return COMPARATOR_ROLE.compare(this, o);
	}

	@Override
	public String toString() {
		return role;
	}
	
}
