package com.jsp.ecommerce.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.jsp.ecommerce.dao.CustomerDao;
import com.jsp.ecommerce.dto.Customer;
import com.jsp.ecommerce.helper.AES;
import com.jsp.ecommerce.helper.EmailLogic;

@Service
public class CustomerService {
	@Autowired
	CustomerDao customerDao;

	@Autowired
	EmailLogic emailLogic;

	public String signup(Customer customer, ModelMap map) {
		// to check Email and Mobile is Unique
		List<Customer> exCustomers = customerDao.findByEmailOrMobile(customer.getEmail(), customer.getMobile());
		if (!exCustomers.isEmpty()) {
			map.put("fail", "Account Already Exists");
			return "Signup";
		} else {
			// Generating otp
			int otp = new Random().nextInt(100000, 999999);
			customer.setOtp(otp);
			// Encrypting password
			customer.setPassword(AES.encrypt(customer.getPassword(), "123"));
			customerDao.save(customer);
			// Send OTP to email
			// emailLogic.sendOtp(customer);
			// Carrying id
			map.put("id", customer.getId());
			return "EnterOtp";
		}
	}

	public String verifyOtp(int id, int otp, ModelMap map) {
		Customer customer = customerDao.findById(id);
		if (customer.getOtp() == otp) {
			customer.setVerified(true);
			customerDao.update(customer);
			map.put("pass", "Account Created Succesfully");
			return "Login.html";
		} else {
			map.put("fail", "Invalid Otp, Try Again");
			map.put("id", id);
			return "EnterOtp";
		}
	}

}
