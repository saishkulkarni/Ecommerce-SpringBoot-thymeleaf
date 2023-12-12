package com.jsp.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.ecommerce.dto.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>
{

	List<Customer> findByEmailOrMobile(String email, long mobile);

}
