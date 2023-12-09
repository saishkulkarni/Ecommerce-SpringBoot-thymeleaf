package com.jsp.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.ecommerce.dto.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>
{

	Customer findByEmail(String email);

}
