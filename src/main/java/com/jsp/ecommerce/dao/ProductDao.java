package com.jsp.ecommerce.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jsp.ecommerce.dto.Product;
import com.jsp.ecommerce.repository.ProductRepository;

@Repository
public class ProductDao {
	@Autowired
	ProductRepository productRepository;

	public void save(Product product) {
		productRepository.save(product);
	}

	public List<Product> fetchAll() {
		return productRepository.findAll();
	}
	
	
}
