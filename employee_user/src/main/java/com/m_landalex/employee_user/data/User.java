package com.m_landalex.employee_user.data;

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
public class User extends AbstractObject {

	private String username;
	private String password;
	private String userRole;

	@Override
	public String toString() {
		return "[" + super.toString() + ", username=" + username + ", password=" + password + ", userRole=" + userRole + "]";
	}

}
