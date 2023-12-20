package com.jsp.ecommerce.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Component
@Entity
public class Customer {
	@Id
	@GeneratedValue(generator = "slno")
	@SequenceGenerator(initialValue = 101, allocationSize = 1, sequenceName = "slno", name = "slno")
	private int id;
	@NotEmpty(message = "*This is Mandatory")
	@Size(max = 10, min = 3, message = "*Enter between 3 to 10")
	private String name;
	@NotEmpty(message = "*This is Mandatory")
	@Email(message = "*Enter Proper Format")
	private String email;
	@NotEmpty(message = "*This is Mandatory")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "*Should Match Pattern")
	private String password;
	@DecimalMin(value = "6000000000", message = "*Enter Proper Mobile Number")
	@DecimalMax(value = "9999999999", message = "*Enter Proper Mobile Number")
	private long mobile;
	@Past(message = "*Enter Proper Date")
	@NotNull(message = "Select One Date")
	private LocalDate dob;
	@NotEmpty(message = "*This is Mandatory")
	private String gender;
	
	private int otp;
	boolean verified;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	ShoppingCart cart;
}
