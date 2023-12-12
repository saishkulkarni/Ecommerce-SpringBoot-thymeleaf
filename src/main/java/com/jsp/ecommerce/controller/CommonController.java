package com.jsp.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {
	@GetMapping("/")
	public String loadHome() {
		return "Home";
	}

	@GetMapping("/about-us")
	public String loadAboutUs() {
		return "AboutUs";
	}
}
