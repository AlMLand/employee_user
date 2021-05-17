package com.m_landalex.employee_user;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMBeanExport
@EnableScheduling
public class DemoRunFile {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(DemoRunFile.class, args);

	}

}
