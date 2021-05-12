package com.m_landalex.employee_user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.m_landalex.employee_user.data.AbstractObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "roles")
public class RoleEntity extends AbstractObject {

	@Column(name = "ROLE")
	private String role;
	
}
