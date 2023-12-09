package com.jsp.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsp.ecommerce.dto.Customer;
import com.jsp.ecommerce.service.CustomerService;

import jakarta.validation.Valid;

@RequestMapping("/customer")
@Controller
public class CustomerController {

	@Autowired
	Customer customer;
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/signup")
	public String loadSignup(ModelMap map) {
		map.put("customer",customer);
		return "Signup";
	}
	@PostMapping("/signup")
	public String signup(@Valid Customer customer, BindingResult result,ModelMap map) {
		if (result.hasErrors()) {
			return "Signup";
		} else {
			return customerService.signup(customer,map);
		}
	}

}
