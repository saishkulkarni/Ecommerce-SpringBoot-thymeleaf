package com.jsp.ecommerce.service;

import java.io.IOException;
import java.util.List;

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
		byte[] picture = new byte[pic.getInputStream().available()];
		pic.getInputStream().read(picture);
		product.setPicture(picture);
		productDao.save(product);

		map.put("pass", "Product Added Success");
		return "AdminHome";
	}

	public String fetchProducts(ModelMap map) {
		List<Product> products = productDao.fetchAll();
		if (products.isEmpty()) {
			map.put("fail", "No Products Found");
			return "AdminHome";
		} else {
			map.put("products", products);
			return "AdminViewProduct";
		}
	}

	public String changeStatus(int id, ModelMap map) {
		Product product=productDao.findById(id);
		if(product.isDisplay())
			product.setDisplay(false);
		else
			product.setDisplay(true);
		
		productDao.save(product);
		
		map.put("pass", "Status Update Success");
		return fetchProducts(map);
	}

	public String deleteProduct(int id, ModelMap map) {
		Product product=productDao.findById(id);
		productDao.delete(product);
		
		map.put("pass", "Product Deleted Success");
		return fetchProducts(map);
	}

	public String editProduct(int id, ModelMap map) {
		Product product=productDao.findById(id);
		map.put("product", product);
		return "EditProduct.html";
	}

	public String updateProduct(Product product, MultipartFile pic, ModelMap map) throws IOException {
		byte[] picture = new byte[pic.getInputStream().available()];
		pic.getInputStream().read(picture);
		
		if(picture.length==0)
			product.setPicture(productDao.findById(product.getId()).getPicture());
		else
		product.setPicture(picture);
		
		productDao.save(product);

		map.put("pass", "Product Updated Success");
		return fetchProducts(map);
	}

}
