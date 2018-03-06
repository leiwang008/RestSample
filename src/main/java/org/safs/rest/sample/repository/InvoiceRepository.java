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
package org.safs.rest.sample.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.safs.rest.sample.model.Invoice;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public class InvoiceRepository extends InMemoryRepository<Invoice>{

	@Override
	protected void updateIfExists(Invoice original, Invoice desired) {
		original.setCustomerId(desired.getCustomerId());
		original.setTotal(desired.getTotal());
	}

	public List<Invoice> findAllByCustomerId(Long customerId){
		return elements.stream().filter(e -> e.getCustomerId().equals(customerId)).collect(Collectors.toList());
	}
}