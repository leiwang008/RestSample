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

import org.safs.rest.sample.model.Product;
import org.safs.rest.sample.util.Util;

/**
 * @author Lei Wang
 *
 */
public class ProductResource{
	private final Long id;
	private final String name;
	private final double price;

	public ProductResource(Product product){
		id = product.getId();
		name = product.getName();
		price = product.getPrice();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return Util.keep2Decimal(price);
	}

}
