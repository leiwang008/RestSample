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
package org.safs.rest.sample.resource;

import org.safs.rest.sample.model.Item;
import org.safs.rest.sample.util.Util;

/**
 * @author Lei Wang
 *
 */
public class ItemResource {
	private final Long id;
	private final Long invoiceId;
	private final Long productId;
	private final int quantity;
	private final double cost;

	public ItemResource(Item item){
		this.id = item.getId();
		this.invoiceId = item.getInvoiceId();
		this.productId = item.getProductId();
		this.quantity = item.getQuantity();
		this.cost = item.getCost();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the invoiceId
	 */
	public Long getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @return the productId
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return Util.keep2Decimal(cost);
	}

}
