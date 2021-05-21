package com.m_landalex.employee_user.controller.web;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

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

import com.m_landalex.employee_user.data.User;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserWebController {

	@Autowired
	private UserService service;
	
	@PostMapping(value = "/")
	@Transactional(rollbackFor = ResponseStatusException.class)
	public String save(@Valid @ModelAttribute User user, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "usercreate";
		}
		try {
			service.save(user);
		} catch (AsyncXAResourcesException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object ist not correct", e);
		}
		model.addAttribute("users", service.fetchAll());
		return "listusers";
	}
	
	@GetMapping(value = "/updatings/{id}")
	public String updateById(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.fetchById(id));
		return "usercreate";
	}
	
	@GetMapping(value = "/showings")
	public String showAll(Model model) {
		model.addAttribute("users", service.fetchAll());
		return "listusers";
	}	
	
	@GetMapping(value = "/showings/id")
	public String showById(@RequestParam(name = "id") String id, Model model) {
		try {
			model.addAttribute("users", List.of(service.fetchById(Long.valueOf(id))));
		} catch (NoSuchElementException e) {
			return "listusers";
		}
		return "listusers";
	}
	
}
