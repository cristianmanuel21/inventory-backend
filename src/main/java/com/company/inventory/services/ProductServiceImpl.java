package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.uitl.Util;

@Service
public class ProductServiceImpl implements IProductService{
	
	
	private ICategoryDao categoryDao; //better performance in unit test
	private IProductDao productDao;
	
	public ProductServiceImpl(ICategoryDao categoryDao,IProductDao productDao ) {
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		
		ProductResponseRest response=new ProductResponseRest();
		List<Product> list=new ArrayList<>();
		
		try {
			//search category to set in product object
			Optional<Category> category=categoryDao.findById(categoryId);
			if(!category.isEmpty()){
				product.setCategory(category.get());
			}else {
				response.setMetadata("respuesta nok","-1","Cateogy not found");
				return new ResponseEntity<ProductResponseRest>(response,HttpStatus.NOT_FOUND);
			}
		//save the product
		Product productSaved=productDao.save(product);
		if(productSaved!=null) {
			list.add(productSaved);
			response.getProductResponse().setProducts(list);
			response.setMetadata("respuesta ok","00","Product saved");
		}else {
			response.setMetadata("respuesta nok","-1","Product was not save");
			return new ResponseEntity<ProductResponseRest>(response,HttpStatus.BAD_REQUEST);
		}
		
			
		}catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok","-1","Internal Error");
			return new ResponseEntity<ProductResponseRest>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return new ResponseEntity<ProductResponseRest>(response,HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
		
		ProductResponseRest response=new ProductResponseRest();
		List<Product> list=new ArrayList<>();
		
		try {
			Optional<Product> product = productDao.findById(id);
			if (product.isPresent()) {
				byte[] imageDescompressed=Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imageDescompressed);
				list.add(product.get());
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta ok", "00", "Producto encontrada");
			} else {
				response.setMetadata("Respuesta nok", "-1", "Producto no encontrada");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			
			response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
			e.getStackTrace();
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		ProductResponseRest response=new ProductResponseRest();
		List<Product> list=new ArrayList<>();
		
		try {
			List<Product> product = productDao.findByNameContainingIgnoreCase(name);
			if (!product.isEmpty()) {
				byte[] imageDescompressed=Util.decompressZLib(product.get(0).getPicture());
				product.get(0).setPicture(imageDescompressed);
				list.add(product.get(0));
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta ok", "00", "Producto encontrada");
			} else {
				response.setMetadata("Respuesta nok", "-1", "Producto no encontrada");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			
			response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
			e.getStackTrace();
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> delete(Long id) {
		ProductResponseRest response = new ProductResponseRest();
		
		try {
			
			productDao.deleteById(id);
			response.setMetadata("respuesta ok", "00", "Registro eliminado");
		} catch (Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
			e.getStackTrace();
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
		
	}

	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> search() {
		ProductResponseRest response=new ProductResponseRest();
		List<Product> list=new ArrayList<>();
		
		try {
			list = (List<Product>)productDao.findAll();
			if (list.size()>0) {
				list.stream().map(p-> {
					byte[] imageDescompressed=Util.decompressZLib(p.getPicture());
					p.setPicture(imageDescompressed);
					return p;
				}).collect(Collectors.toList());
				response.getProductResponse().setProducts(list);
				response.setMetadata("Respuesta ok", "00", "Productos encontrados");
			} else {
				response.setMetadata("Respuesta nok", "-1", "Lista vacia");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			
			response.setMetadata("Respuesta nok", "-1", "Error al consultar");
			e.getStackTrace();
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}
	
	
	
	
	

}
