package com.m_landalex.employee_user.view;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.service.EmployeeService;

@SpringBootApplication(scanBasePackages = "com.m_landalex.employee_user")
@EnableJpaRepositories(basePackages = "com.m_landalex.employee_user.persistence")
@EntityScan("com.m_landalex.employee_user.domain")
public class DemoRunFile implements CommandLineRunner {

	@Autowired
	private EmployeeService employeeService;
	
	@Override
	public void run(String... args) throws Exception {
		List<Employee> returnedList = employeeService.fetchAll();
		show(returnedList);
	}

	private void show(List<Employee> returnedList) {
		returnedList.forEach(employee -> System.out.println(employee));
	}

	public static void main(String[] args) throws IOException {
		
		ConfigurableApplicationContext context = SpringApplication.run(DemoRunFile.class, args);
		System.in.read();
		context.close();
		
	}

}
