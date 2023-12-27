package com.jsp.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.ecommerce.dto.Customer;
import com.jsp.ecommerce.dto.ShoppingOrder;

public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Integer> {

	List<ShoppingOrder> findByCustomer(Customer customer);

}
