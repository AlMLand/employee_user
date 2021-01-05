package com.m_landalex.employee_user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "email")
public class EmailEntity extends AbstractEntity {

	@OneToOne(mappedBy = "email")
	@Column(name = "email")
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private String email;
	
}
