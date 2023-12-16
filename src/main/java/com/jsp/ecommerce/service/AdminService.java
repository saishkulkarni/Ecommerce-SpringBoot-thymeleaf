package com.jsp.ecommerce.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.ecommerce.dao.ProductDao;
import com.jsp.ecommerce.dto.Product;

@Service
public class AdminService {
	
	@Autowired
	ProductDao productDao;

	public String addProduct(Product product, MultipartFile pic, ModelMap map) throws IOException {
		byte[] picture=new byte[pic.getInputStream().available()];
		pic.getInputStream().read(picture);
		
		product.setPicture(picture);
		productDao.save(product);
		
		map.put("pass", "Product Added Success");
		return "AdminHome";
	}

}
