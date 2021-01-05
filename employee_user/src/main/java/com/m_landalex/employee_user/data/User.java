package com.m_landalex.employee_user.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractObject {

	private String username;
	private String password;
	private Role userRole;
	private List<User> userList;

	public User(String username, String password, Role userRole) {
		super();
		this.username = username;
		this.password = password;
		this.userRole = userRole;
		this.userList.add(this);
	}
	
}
