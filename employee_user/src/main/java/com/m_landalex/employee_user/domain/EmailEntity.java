package com.m_landalex.employee_user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "email")
public class EmailEntity extends AbstractEntity {

	@Column(name = "EMAIL")
	private String email;

	@OneToOne(mappedBy = "email")
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private EmployeeEntity employee;

}
