package com.jsp.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.ecommerce.dto.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>
{

	List<Product> findByDisplayTrue();

}
