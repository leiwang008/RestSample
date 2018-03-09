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

import org.safs.rest.sample.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface ItemRepository extends CrudRepository<Item, Long>{

	@Query("delete from Item i where i.invoiceId=:invoiceId")
	public boolean deleteByInvoiceId(@Param("invoiceId") Long invoiceId);
	@Query("select i from Item i where i.invoiceId=:invoiceId")
	public List<Item> findAllByInvoiceId(@Param("invoiceId") Long invoiceId);
	@Query("select i from Item i where i.productId=:productId")
	public List<Item> findAllByProductId(@Param("productId") Long productId);
}
