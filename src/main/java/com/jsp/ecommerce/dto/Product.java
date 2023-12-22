package com.jsp.ecommerce.dto;

import java.util.List;

import org.apache.commons.codec.binary.Base64;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String category;
	private double price;
	private int stock;
	
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private byte[] picture;
	private boolean display;
	
	public String generateBase64Image()
	{
	    return Base64.encodeBase64String(this.getPicture());
	}
	
	public int getQuantity(List<Item> items)
	{
		int quantity=0;
		if(items==null)
			return quantity;
		else {
		for(Item item:items)
		{
			if(this.name.equals(item.getName()))
				quantity=item.getQuantity();
		}
		return quantity;
		}
	}
}
