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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.safs.rest.sample.model.Customer;
import org.safs.rest.sample.model.Invoice;
import org.safs.rest.sample.model.Item;
import org.safs.rest.sample.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lei Wang
 *
 */
@CrossOrigin(origins="*")
@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE)
public class ExposedRestAPIController {

	@Autowired
	protected EntityLinks entityLinks;

	@GetMapping
	public ResponseEntity<Collection<ExposedRestAPI>> findAll(){
		List<ExposedRestAPI> exposedAPI = new ArrayList<ExposedRestAPI>();

		exposedAPI.add(new ExposedRestAPI(Product.class.getSimpleName(), entityLinks.linkFor(Product.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Customer.class.getSimpleName(), entityLinks.linkFor(Customer.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Invoice.class.getSimpleName(), entityLinks.linkFor(Invoice.class).withSelfRel().getHref()));
		exposedAPI.add(new ExposedRestAPI(Item.class.getSimpleName(), entityLinks.linkFor(Item.class).withSelfRel().getHref()));

		return new ResponseEntity<>(exposedAPI, HttpStatus.OK);
	}
}

class ExposedRestAPI{
	private String name;
	private String href;

	public ExposedRestAPI(String name, String href){
		this.name = name;
		this.href = href;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

}
