package com.m_landalex.employee_user.view.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m_landalex.employee_user.data.Address;
import com.m_landalex.employee_user.service.AddressService;

@RestController
@RequestMapping(value = "/addresses")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@PostMapping(value = "/")
	public Address createAddress(@RequestBody Address address) {
		return addressService.save(address);
	}

	@GetMapping(value = "/")
	public List<Address> fetchAllAddresses() {
		return addressService.fetchAll();
	}

	@DeleteMapping(value = "/")
	public void deleteAllAddresses() {
		addressService.deleteAll();
	}

	@PutMapping(value = "/{id}")
	public Address updateAddressById(@PathVariable Long id, @RequestBody Address address) {
		return addressService.save(address);
	}

	@GetMapping(value = "/{id}")
	public Address fetchAddressById(@PathVariable Long id) {
		return addressService.fetchById(id);
	}

	@DeleteMapping(value = "/{id}")
	public void deleteAddressById(@PathVariable Long id) {
		addressService.deleteById(id);
	}

}
