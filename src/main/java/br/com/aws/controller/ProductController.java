package br.com.aws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.aws.enums.EventType;
import br.com.aws.model.Product;
import br.com.aws.services.ProductPublisher;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	ProductPublisher productPublisher;
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<Product> saveProduct(@RequestBody Product product){
		productPublisher.publishProductEvent(product, EventType.PRODUCT_CREATED, product.getUsername());
		return new ResponseEntity<Product>(product, HttpStatus.CREATED);
	}
}
