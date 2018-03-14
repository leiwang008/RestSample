/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-02    (Lei Wang) Initial release.
 */
package org.safs.rest.sample.resource;

import org.safs.rest.sample.model.Customer;
import org.safs.rest.sample.model.Gender;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lei Wang
 *
 */
public class CustomerResource extends ResourceSupport{
	private final Long id;
	private final String firstName;
	private final String lastName;
	private final String street;
	private final String city;
	private final Gender gender;

	public CustomerResource(Customer customer){
		id = customer.getId();
		firstName = customer.getFirstName();
		lastName = customer.getLastName();
		street = customer.getStreet();
		city = customer.getCity();
		gender = customer.getGender();
	}

	/**
	 * @return the id
	 */
	@JsonProperty("id")
	public Long getResourceId() {
		return id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

    /**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	@Override
	public String toString(){
        return id+" | " + lastName+ " | "+ firstName+ " | "+ gender+ " | "+ street+" | "+city;
    }
}
