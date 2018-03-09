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
import org.safs.rest.sample.model.Customer;
import org.safs.rest.sample.model.Invoice;
import org.safs.rest.sample.repository.CustomerRepository;
import org.safs.rest.sample.repository.InvoiceRepository;
import org.safs.rest.sample.resource.CustomerResource;
import org.safs.rest.sample.resource.CustomerResourceAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ExposesResourceFor(Customer.class)
@RequestMapping(value="/customer", produces=MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private CustomerResourceAssembler assembler;

	@GetMapping
	public ResponseEntity<Collection<CustomerResource>> findAll(){
		Iterable<Customer> customers = customerRepository.findAll();
		Collection<CustomerResource> f = assembler.toResourceCollection(customers);
		return new ResponseEntity<>(f, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerResource> create(@RequestBody Customer body){
		Customer customer = customerRepository.save(body);
		log.debug("Customer has been created in the repository.");
		return new ResponseEntity<>(assembler.toResource(customer), HttpStatus.CREATED);
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<CustomerResource> find(@PathVariable Long id){
		Optional<Customer> customer = customerRepository.findById(id);
		try{
			return new ResponseEntity<>(assembler.toResource(customer.get()), HttpStatus.OK);
		}catch(NoSuchElementException nse){
			throw new RestException("Failed to find Custome by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<CustomerResource> delete(@PathVariable Long id){
		List<Invoice> invoices = invoiceRepository.findAllByCustomerId(id);
		if(!invoices.isEmpty()){
			throw new RestException("Cannot delete customer by id '"+id+"', there are still Invoices depeding on it!", HttpStatus.FAILED_DEPENDENCY);
		}

		if(!customerRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			customerRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<CustomerResource> update(@PathVariable Long id, @RequestBody Customer body){
		if(!customerRepository.existsById(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else{
			Customer original = customerRepository.findById(id).get();
			original.setCity(body.getCity());
			original.setFirstName(body.getFirstName());
			original.setLastName(body.getLastName());
			original.setStreet(body.getStreet());
			customerRepository.save(original);
			return new ResponseEntity<>(assembler.toResource(original), HttpStatus.OK);
		}
	}

}
