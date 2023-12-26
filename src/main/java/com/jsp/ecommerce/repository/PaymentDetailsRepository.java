package com.jsp.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.ecommerce.dto.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Integer>
{

}
