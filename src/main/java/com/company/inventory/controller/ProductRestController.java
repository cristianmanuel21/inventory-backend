package com.company.inventory.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.IProductService;
import com.company.inventory.uitl.Util;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {
	
	private IProductService productService;
	
	public ProductRestController(IProductService productService) {
		super();
		this.productService = productService;
	}
	
	@GetMapping("/products")
	public ResponseEntity<ProductResponseRest> searchbyId(){
		ResponseEntity<ProductResponseRest> response= productService.search();
		
		return response;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> searchbyId(@PathVariable Long id){
		ResponseEntity<ProductResponseRest> response= productService.searchById(id);
		
		return response;
	}
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/products/name/{name}")
	public ResponseEntity<ProductResponseRest> searchByName(@PathVariable String name){
		ResponseEntity<ProductResponseRest> response= productService.searchByName(name);
		
		return response;
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> deletebyId(@PathVariable Long id){
		ResponseEntity<ProductResponseRest> response= productService.delete(id);
		
		return response;
	}



	/**
	 * 
	 * @param picture
	 * @param name
	 * @param price
	 * @param account
	 * @param categoryId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/products")
	public ResponseEntity<ProductResponseRest> save(
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("name") String name,
			@RequestParam("price") double price,
			@RequestParam("account") int account,
			@RequestParam("categoryId") long categoryId) throws IOException
	{
		Product product=new Product();
		product.setName(name);
		product.setAccount(account);
		product.setPrice(price);
		product.setPicture(Util.compressZLib(picture.getBytes()));
		
		ResponseEntity<ProductResponseRest> response= productService.save(product, categoryId);
		return response;
		
	}

}
