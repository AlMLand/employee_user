package com.m_landalex.employee_user.controller.web;

import java.util.List;
import java.util.NoSuchElementException;

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
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.AddressService;

@Controller
@RequestMapping(value = "/addresses")
public class AddressWebController {

	@Autowired
	private AddressService service;
	
	@PostMapping(value = "/")
	@Transactional(rollbackFor = ResponseStatusException.class)
	public String save(@Valid @ModelAttribute Address address, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "addresscreate";
		}
		try {
			service.save(address);
		} catch (AsyncXAResourcesException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object ist not correct", e);
		}
		model.addAttribute("addresses", service.fetchAll());
		return "listaddresses";
	}
	
	@GetMapping(value = "/updatings/{id}")
	public String updateById(@PathVariable Long id, Model model) {
		model.addAttribute("address", service.fetchById(id));
		return "addresscreate";
	}
	
	@GetMapping(value = "/showings")
	public String showAll(Model model) {
		model.addAttribute("addresses", service.fetchAll());
		return "listaddresses";
	}
	
	@GetMapping(value = "/showings/city")
	public String showByCity(@RequestParam(name = "city") String city, Model model) {
		try {
			model.addAttribute("addresses",
					List.of(service.fetchByCity(StringUtils.capitalize(city.toLowerCase()).trim())));
		} catch (NoSuchElementException | NullPointerException e) {
			return "listaddresses";
		}
		return "listaddresses";
	}
	
}
