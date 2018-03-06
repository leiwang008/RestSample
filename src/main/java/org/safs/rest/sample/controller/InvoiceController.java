/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-06    (Lei Wang) Initial release.
 */
package org.safs.rest.sample.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.safs.rest.sample.exception.RestException;
import org.safs.rest.sample.model.Customer;
import org.safs.rest.sample.model.Invoice;
import org.safs.rest.sample.model.Item;
import org.safs.rest.sample.repository.CustomerRepository;
import org.safs.rest.sample.repository.InvoiceRepository;
import org.safs.rest.sample.repository.ItemRepository;
import org.safs.rest.sample.resource.InvoiceResource;
import org.safs.rest.sample.resource.InvoiceResourceAssembler;
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
@ExposesResourceFor(Invoice.class)
@RequestMapping(value="/invoice", produces=MediaType.APPLICATION_JSON_VALUE)
public class InvoiceController {

	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	ItemRepository itemRepository;

	@Autowired
	InvoiceResourceAssembler assembler;

	@GetMapping
	public ResponseEntity<Collection<InvoiceResource>> findAll(){
		List<Invoice> entities = invoiceRepository.findAll(Invoice.class);
		return new ResponseEntity<>( assembler.toResourceCollection(entities), HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InvoiceResource> create(@RequestBody Invoice body){
		//First we need to make sure that customer exists.
		Customer customer = customerRepository.findById(body.getCustomerId(), Customer.class);

		if(customer != null){
			Invoice invoice = invoiceRepository.create(body);
			if(invoice==null){
				throw new RestException("Failed to create Invoice: "+body.toString());
			}else{
				return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toResource(invoice));
			}
		}else{
			throw new RestException("Cannot find customer by ID '"+body.getCustomerId()+"', so the Invoice cannot be created with that invoice.");
		}
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<InvoiceResource> find(@PathVariable Long id){
		Invoice invoice = invoiceRepository.findById(id, Invoice.class);
		if(invoice==null){
			throw new RestException("Failed to find Invoice by id '"+id+"'", HttpStatus.NOT_FOUND);
		}else{
			return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(invoice));
		}
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<InvoiceResource> delete(@PathVariable Long id){
		//Delete the related items from item repository
		itemRepository.deleteByInvoiceId(id/*invoiceId*/);

		//Verify there is no related items left in the item repository
		List<Item> items = itemRepository.findAllByInvoiceId(id);
		if(!items.isEmpty()){
			throw new RestException("There are still items related to invoice id '"+id+"', cannot delete this invoice!", HttpStatus.FAILED_DEPENDENCY);
		}

		if(invoiceRepository.delete(id, Invoice.class)){
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}else{
			throw new RestException("Failed to delete Invoice by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InvoiceResource> update(@PathVariable Long id, @RequestBody Invoice body){
		//Get all items from itemRepository according to the invoice's id, and calculate the Invoice's total
		List<Item> items = new ArrayList<Item>();
		items = itemRepository.findAllByInvoiceId(id/*invoiceId*/);
		double total = 0;
		for(Item item: items){
			total += item.getQuantity()*item.getCost();
		}
		body.setTotal(total);

		//Make sure the customer id exist if we are going to update this field
		Customer customer = customerRepository.findById(body.getCustomerId(), Customer.class);
		if(customer==null){
			throw new RestException("Failed to upate Invoice. Its field customer '"+body.getCustomerId()+"' doesn't exist!", HttpStatus.NOT_FOUND);
		}

		Invoice invoice = invoiceRepository.update(id, body);
		if(invoice==null){
			throw new RestException("Failed to upate Invoice by id '"+id+"'", HttpStatus.NOT_FOUND);
		}else{
			return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(invoice));
		}
	}

}
