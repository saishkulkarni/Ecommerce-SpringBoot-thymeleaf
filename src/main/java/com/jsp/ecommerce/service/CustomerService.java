package com.jsp.ecommerce.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.jsp.ecommerce.dao.CustomerDao;
import com.jsp.ecommerce.dto.Customer;

import jakarta.validation.Valid;

@Service
public class CustomerService {
	@Autowired
	CustomerDao customerDao;

	public String signup(Customer customer, ModelMap map) {
		List<Customer> exCustomers=customerDao.findByEmailOrMobile(customer.getEmail(),customer.getMobile());
		if(!exCustomers.isEmpty())
		{
			map.put("fail","Account Already Exists");
			return "Signup";
		}
		else{
			int otp=new Random().nextInt(100000,999999);
			customer.setOtp(otp);
			customerDao.save(customer);
			//Logic for Sending Mail
			
			map.put("id", customer.getId());
			return "EnterOtp";
		}
	}

}
