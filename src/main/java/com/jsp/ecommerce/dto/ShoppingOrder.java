package com.jsp.ecommerce.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class ShoppingOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String order_id;
	String payment_id;
	LocalDateTime dateTime;
	double amount;

	@OneToMany
	List<Item> items;

	@ManyToOne
	Customer customer;
	
	public String getDate()
	{
		return this.dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-YYYY hh:mm"));
	}
}
