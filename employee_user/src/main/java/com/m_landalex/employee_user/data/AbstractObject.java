package com.m_landalex.employee_user.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractObject {

	private Long id;

	@Override
	public String toString() {
		return "id=" + id;
	}

}
