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

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.safs.rest.sample.exception.RestException;
import org.safs.rest.sample.model.Invoice;
import org.safs.rest.sample.model.Item;
import org.safs.rest.sample.repository.InvoiceRepository;
import org.safs.rest.sample.repository.ItemRepository;
import org.safs.rest.sample.repository.ProductRepository;
import org.safs.rest.sample.resource.ItemResource;
import org.safs.rest.sample.resource.ItemResourceAssembler;
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
@ExposesResourceFor(Item.class)
@RequestMapping(value="/item", produces=MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	ItemResourceAssembler assembler;

	@GetMapping
	public ResponseEntity<Collection<ItemResource>> findAll(){
		Iterable<Item> entities = itemRepository.findAll();
		return new ResponseEntity<>( assembler.toResourceCollection(entities), HttpStatus.OK);
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemResource> create(@RequestBody Item body){
		Invoice invoice = null;

		try{
			//We need to make sure the invoice exists
			invoice = invoiceRepository.findById(body.getInvoiceId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find invoice by ID '"+body.getInvoiceId()+"', so the Item cannot be created with that invoice.");
		}
		try{
			//We need to make sure the product exists
			productRepository.findById(body.getProductId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Cannot find product by ID '"+body.getProductId()+"', so the Item cannot be created with that product.");
		}

		Item item = itemRepository.save(body);

		//update the invoice's total
		invoice.setTotal(invoice.getTotal()+item.getCost()*item.getQuantity());
		invoiceRepository.save(invoice);
		return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toResource(item));

	}

	@GetMapping(value="/{id}")
	public ResponseEntity<ItemResource> find(@PathVariable Long id){
		Optional<Item> entity = itemRepository.findById(id);
		try{
			return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(entity.get()));
		}catch(NoSuchElementException e){
			throw new RestException("Failed to find Item by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<ItemResource> delete(@PathVariable Long id){
		Item item = null;
		Invoice invoice = null;

		try{
			item = itemRepository.findById(id).get();
		}catch(NoSuchElementException e){
			throw new RestException("Failed to find Item by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
		try{
			invoice = invoiceRepository.findById(item.getInvoiceId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Failed to find Invoice by id '"+id+"'", HttpStatus.NOT_FOUND);
		}

		//Update the related invoice's total, subtract this item's total from invoice's total
		invoice.setTotal(invoice.getTotal()-item.getQuantity()*item.getCost());
		invoiceRepository.save(invoice);

		if(itemRepository.existsById(id)){
			itemRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}else{
			throw new RestException("Failed to delete Item by id '"+id+"'", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemResource> update(@PathVariable Long id, @RequestBody Item body){
		Item item = null;
		Invoice originInvoice = null;
		Invoice holderInvoice = null;

		try{
			item = itemRepository.findById(id).get();
		}catch(NoSuchElementException e){
			throw new RestException("Failed to find Item by id '"+id+"'", HttpStatus.NOT_FOUND);
		}

		//1. Update related invoices
		try{
			originInvoice = invoiceRepository.findById(item.getInvoiceId()).get();
		}catch(NoSuchElementException e){
			throw new RestException("Failed to find original Invoice by id '"+id+"'", HttpStatus.NOT_FOUND);
		}

		if(body.getInvoiceId()!=item.getInvoiceId()){
			//The item will be assigned to an other holder Invoice
			try{
				holderInvoice = invoiceRepository.findById(body.getInvoiceId()).get();
			}catch(NoSuchElementException e){
				throw new RestException("Failed to find the Invoice (item's new holder invoice) by id '"+id+"'", HttpStatus.NOT_FOUND);
			}
			//subtract the original item's quantity*cost from the original invoice
			originInvoice.setTotal(originInvoice.getTotal()-item.getQuantity()*item.getCost());
			invoiceRepository.save(originInvoice);

			//add the body item's quantity*cost to the new holder invoice
			holderInvoice.setTotal(holderInvoice.getTotal()+body.getCost()*body.getQuantity());
			invoiceRepository.save(holderInvoice);
		}else{
			//we didn't change the invoice id for this item
			//subtract the original item's quantity*cost from the original invoice, and add the body item's quantity*cost to the original invoice
			originInvoice.setTotal(originInvoice.getTotal()-item.getQuantity()*item.getCost()+body.getCost()*body.getQuantity());
			invoiceRepository.save(originInvoice);
		}

		//2. Make sure the new product id exists.
		if(item.getProductId()!=body.getProductId()){//If we are going to update the product id
			//Make sure that the body item's product id exist
			if(!productRepository.existsById(body.getProductId())){
				throw new RestException("Failed to find the product (to set to Item) by id '"+id+"'", HttpStatus.NOT_FOUND);
			}
		}

		//Finally update the itme itself
		item.setCost(body.getCost());
		item.setInvoiceId(body.getInvoiceId());
		item.setProductId(body.getProductId());
		item.setQuantity(body.getQuantity());
		itemRepository.save(item);
		return ResponseEntity.status(HttpStatus.OK).body(assembler.toResource(item));
	}

}
