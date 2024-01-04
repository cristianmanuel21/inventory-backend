package com.company.inventory.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Product;

public interface IProductDao extends CrudRepository<Product, Long>{
	
	public Optional<Product> findByNameIgnoreCase(String name);
	
	@Query("select p from  Product p where p.name like %?1%")
	List<Product> findByNameLike(String name);
	
	List<Product> findByNameContainingIgnoreCase(String name);

}
