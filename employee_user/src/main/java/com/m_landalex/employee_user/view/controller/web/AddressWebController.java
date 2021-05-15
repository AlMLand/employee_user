package com.m_landalex.employee_user.view.controller.web;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.exception.AsyncXAResourcesException;
import com.m_landalex.employee_user.service.AddressService;

@Controller
@RequestMapping(value = "/addresses")
public class AddressWebController {

	@Autowired
	private AddressService service;
	
	@PutMapping(value = "/")
	public String save(@Valid @ModelAttribute Address address, BindingResult result, Model model) throws AsyncXAResourcesException {
		if(result.hasErrors()) {
			model.addAttribute("address", address);
			return "";
		}
		Address newAddress = service.save(address);
		model.addAttribute("address", newAddress);
		return "";
	}
	
	@GetMapping(value = "/updatings/{id}")
	public String updateById(@PathVariable Long id, Model model) {
		model.addAttribute("address", service.fetchById(id));
		return "";
	}
	
	@GetMapping(value = "/showings")
	public String showAll(Model model) {
		model.addAttribute("addresses", service.fetchAll());
		return "listaddresses";
	}
	
	@GetMapping(value = "/showings/city")
	public String showByCity(@RequestParam(name = "city") String city, Model model) {
		model.addAttribute("addresses", List.of(service.fetchByCity(StringUtils.capitalize(city.toLowerCase()).trim())));
		return "listaddresses";
	}
	
}
