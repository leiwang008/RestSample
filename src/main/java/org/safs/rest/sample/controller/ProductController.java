/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-05    (Lei Wang) Initial release.
 */
package org.safs.rest.sample.controller;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.rest.sample.exception.RestException;
import org.safs.rest.sample.model.Item;
import org.safs.rest.sample.model.Product;
import org.safs.rest.sample.repository.ItemRepository;
import org.safs.rest.sample.repository.ProductRepository;
import org.safs.rest.sample.resource.ProductResource;
import org.safs.rest.sample.resource.ProductResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lei Wang
 *
 */
@CrossOrigin(origins="*")
@RestController
@ExposesResourceFor(Product.class)
@RequestMapping(value="/product", produces=MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ProductResourceAssembler assembler;

	@GetMapping
	public ResponseEntity<Collection<ProductResource>> findAll(){
		Iterable<Product> products = productRepository.findAll();
		Collection<ProductResource> f = assembler.toResourceCollection(products);
		return new ResponseEntity<>(f, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResource> create(@RequestBody Product product){
		Product cc = productRepository.save(product);
		return new ResponseEntity<>(assembler.toResource(cc), HttpStatus.CREATED);
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<ProductResource> find(@PathVariable Long id){
		Optional<Product> c = productRepository.findById(id);
		try{
			return new ResponseEntity<>(assembler.toResource(c.get()), HttpStatus.OK);
		}catch(NoSuchElementException e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<ProductResource> delete(@PathVariable Long id){
		List<Item> items = itemRepository.findAllByProductId(id);
		if(!items.isEmpty()){
			throw new RestException("Cannot delete product by id '"+id+"', there are still Items depeding on it!", HttpStatus.FAILED_DEPENDENCY);
		}

		if(productRepository.existsById(id)){
			productRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}else{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<ProductResource> update(@PathVariable Long id, @RequestBody Product body){
		try{
			Product product = productRepository.findById(id).get();
			product.setName(body.getName());
			product.setPrice(body.getPrice());
			return new ResponseEntity<>(assembler.toResource(product), HttpStatus.OK);
		}catch(NoSuchElementException e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
