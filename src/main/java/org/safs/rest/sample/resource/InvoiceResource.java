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

import org.safs.rest.sample.model.Invoice;
import org.safs.rest.sample.util.Util;

/**
 * @author Lei Wang
 *
 */
public class InvoiceResource {
	private final Long id;
	private final double total;
	private final Long customerId;

	public InvoiceResource(Invoice invoice){
		this.id = invoice.getId();
		this.customerId = invoice.getCustomerId();
		this.total = invoice.getTotal();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return Util.keep2Decimal(total);
	}

	/**
	 * @return the customerId
	 */
	public Long getCustomerId() {
		return customerId;
	}

}
