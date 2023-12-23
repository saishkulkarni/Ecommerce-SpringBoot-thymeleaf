package com.jsp.ecommerce.dto;

import lombok.Data;

@Data
public class PaymentDetails {

	int id;
	String key;
	double amount;
	String currency;
	String name;
	String description;
	String image;
	String order_id;
	String payment_id;
	String status;

}
