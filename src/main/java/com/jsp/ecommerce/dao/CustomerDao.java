package com.jsp.ecommerce.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jsp.ecommerce.dto.Customer;
import com.jsp.ecommerce.repository.CustomerRepository;

@Repository
public class CustomerDao {

	@Autowired
	CustomerRepository customerRepository;

	public List<Customer> findByEmailOrMobile(String email, long mobile) {
		return customerRepository.findByEmailOrMobile(email, mobile);
	}

	public void save(Customer customer) {
		customerRepository.save(customer);
	}
	
	public void update(Customer customer) {
		customerRepository.save(customer);
	}

	public Customer findById(int id) {
		return customerRepository.findById(id).orElseThrow(null);
	}

}
