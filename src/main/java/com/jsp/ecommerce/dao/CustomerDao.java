package com.jsp.ecommerce.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jsp.ecommerce.dto.Customer;
import com.jsp.ecommerce.repository.CustomerRepository;

@Repository
public class CustomerDao {
	
	@Autowired
	CustomerRepository customerRepository;

	public Customer findByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

}
