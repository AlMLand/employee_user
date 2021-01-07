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
public class Email extends AbstractObject {

	private String email;

	@Override
	public String toString() {
		return  "[" + super.toString() + ", email=" + email + "]";
	}
	
}
