package com.m_landalex.employee_user.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig {

	@Order(1)
	public static class WebSecurity extends WebSecurityConfigurerAdapter{
		
		@Autowired
		private UserDetailsService service;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
			.antMatchers("/employees/**", "/users/**", "/addresses/**").authenticated()
			.antMatchers("/**").permitAll()
			.and()
			.formLogin()
			.and()
			.logout()
			.logoutSuccessUrl("/")
			.and()
			.csrf().disable();
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(providerWeb());
		}
		
		@Bean
		public PasswordEncoder encoderWeb() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public DaoAuthenticationProvider providerWeb() {
			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
			provider.setPasswordEncoder(encoderWeb());
			provider.setUserDetailsService(service);
			return provider;
		}
		
	}
	
	@Order(2)
	public static class RestSecurity extends WebSecurityConfigurerAdapter{
		
		@Autowired
		private UserDetailsService service;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
			.antMatchers("/rest/employees/**", "/rest/users/**", "/rest/addresses/**").authenticated()
			.antMatchers("/**").permitAll()
			.and()
			.formLogin()
			.and()
			.logout()
			.logoutSuccessUrl("/")
			.and()
			.csrf().disable();
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(providerRest());
		}
		
		@Bean
		public PasswordEncoder encoderRest() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public DaoAuthenticationProvider providerRest() {
			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
			provider.setPasswordEncoder(encoderRest());
			provider.setUserDetailsService(service);
			return provider;
		}
		
	}
	
}
