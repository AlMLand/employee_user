package com.m_landalex.employee_user.controller.web;

import java.util.ArrayList;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.data.Email;
import com.m_landalex.employee_user.data.Employee;
import com.m_landalex.employee_user.data.Role;
import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.EmployeeService;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeWebController {

	@Autowired 
	private EmployeeService service;

	@PostMapping(value = "/")
	@Transactional(rollbackFor = ResponseStatusException.class)
	public String save(@Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "formationorupdate";
		}
		Employee newEmployee;
		try {
			newEmployee = service.save(employee);
		} catch (AsyncXAResourcesException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object is not correct", e);
		}
		return "redirect:/employees/showings/" + newEmployee.getId();
	}
	
	@GetMapping(value = "/formations")
	public String formationNew(Model model) {
		model.addAttribute("employee", Employee.builder()
				.userData(new User(null, null, new ArrayList<Role>()))
				.addressData(new Address())
				.email(new Email())
				.build());
		return "formationorupdate";
	}
	
	@GetMapping(value = "/updatings/{id}")
	public String updateById(@PathVariable Long id , Model model) {
		model.addAttribute("employee", service.fetchById(id));
		return "formationorupdate";
	}
	
	@GetMapping(value = "/showings/{id}")
	public String showById(@PathVariable Long id, Model model) {
		model.addAttribute("employee", service.fetchById(id));
		return "detailsemployee";
	}
	
	@GetMapping(value = "/showings")
	public String showAll(Model model) {
		model.addAttribute("employees", service.fetchAll());
		return "listemployees";
	}
	
	@GetMapping(value = "/remove/{id}")
	public String deletebyId(@PathVariable Long id) {
		service.deleteById(id);
		return "redirect:/employees/showings";
	}
	
	@GetMapping(value = "/showings/lastname")
	public String showByLastname(@RequestParam(name = "lastname") String lastName, Model model) {
		model.addAttribute("employees", service.fetchByLastName(StringUtils.capitalize(lastName.toLowerCase().trim())));
		return "listemployees";
	}
	
}