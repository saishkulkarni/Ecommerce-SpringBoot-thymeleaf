package com.jsp.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.ecommerce.dto.Item;

public interface ItemRepository extends JpaRepository<Item,Integer>
{

}
