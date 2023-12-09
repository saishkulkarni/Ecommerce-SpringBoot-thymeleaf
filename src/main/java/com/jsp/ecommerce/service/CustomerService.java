package com.jsp.ecommerce.service;

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
		Customer exCustomer=customerDao.findByEmail(customer.getEmail());
		if(exCustomer!=null)
		{
			map.put("fail","Account Already Exists");
			return "Signup";
		}
		else{
			int otp=new Random().nextInt(100000,999999);
			return "AboutUs";
		}
	}

}
