package com.jsp.ecommerce.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.ecommerce.dto.Product;
import com.jsp.ecommerce.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;

	@GetMapping("/add-product")
	public String loadAddProduct(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null)
			return "AddProduct";
		else {
			map.put("fail", "Session Expired, Login Again");
			return "Home";
		}
	}

	@GetMapping("/home")
	public String loadHome(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null)
			return "AdminHome";
		else {
			map.put("fail", "Session Expired, Login Again");
			return "Home";
		}
	}

	@PostMapping("/add-product")
	public String addProduct(Product product, @RequestParam MultipartFile pic, HttpSession session, ModelMap map)
			throws IOException {
		if (session.getAttribute("admin") != null) {
			return adminService.addProduct(product, pic, map);
		} else {
			map.put("fail", "Session Expired, Login Again");
			return "Home";
		}
	}

	@GetMapping("/fetch-products")
	public String fetchProducts(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			return adminService.fetchProducts(map);
		} else {
			map.put("fail", "Session Expired, Login Again");
			return "Home";
		}
	}
}
