package com.jsp.ecommerce.dto;

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
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String category;
	private double price;
	private int quantity;
	
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private byte[] picture;
	
	public String generateBase64Image()
	{
	    return Base64.encodeBase64String(this.getPicture());
	}
}
