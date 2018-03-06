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
package org.safs.rest.sample.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.safs.rest.sample.model.Item;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */

@Repository
public class ItemRepository extends InMemoryRepository<Item>{

	@Override
	protected void updateIfExists(Item original, Item desired) {
		original.setCost(desired.getCost());
		original.setInvoiceId(desired.getInvoiceId());
		original.setProductId(desired.getProductId());
		original.setQuantity(desired.getQuantity());
	}

	public boolean deleteByInvoiceId(Long invoiceId){
		return elements.removeIf(e -> e.getInvoiceId()==invoiceId );
	}
	public List<Item> findAllByInvoiceId(Long invoiceId){
		return elements.stream().filter(e-> e.getInvoiceId().equals(invoiceId)).collect(Collectors.toList());
	}
	public List<Item> findAllByProductId(Long productId){
		return elements.stream().filter(e-> e.getProductId().equals(productId)).collect(Collectors.toList());
	}
}
