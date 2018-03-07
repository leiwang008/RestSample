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
		List<Customer> customers = customerRepository.findAll(Customer.class);
		Collection<CustomerResource> f = assembler.toResourceCollection(customers);
		return new ResponseEntity<>(f, HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerResource> create(@RequestBody Customer body){
		Customer customer = customerRepository.create(body);
		log.debug("Customer has been created in the repository.");
		return new ResponseEntity<>(assembler.toResource(customer), HttpStatus.CREATED);
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<CustomerResource> find(@PathVariable Long id){
		Customer c = customerRepository.findById(id, Customer.class);

		if(c==null){
			throw new RestException("Failed to find Custome by id '"+id+"'", HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<>(assembler.toResource(c), HttpStatus.OK);
		}
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<CustomerResource> delete(@PathVariable Long id){
		List<Invoice> invoices = invoiceRepository.findAllByCustomerId(id);
		if(!invoices.isEmpty()){
			throw new RestException("Cannot delete customer by id '"+id+"', there are still Invoices depeding on it!", HttpStatus.FAILED_DEPENDENCY);
		}

		HttpStatus status = customerRepository.delete(id, Customer.class)? HttpStatus.NO_CONTENT: HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(status);
	}

	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<CustomerResource> update(@PathVariable Long id, @RequestBody Customer body){
		Customer customer = customerRepository.update(id, body);
		if(customer==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<>(assembler.toResource(customer), HttpStatus.OK);
		}
	}

}
