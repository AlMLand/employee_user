package com.m_landalex.employee_user.view;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.m_landalex.employee_user")
@EnableJpaRepositories(basePackages = "com.m_landalex.employee_user.persistence")
@EntityScan("com.m_landalex.employee_user.domain")
public class DemoRunFile {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(DemoRunFile.class, args);

	}

}
