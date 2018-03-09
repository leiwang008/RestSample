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

import org.safs.rest.sample.model.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */
@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long>{

	@Query("select i FROM Invoice i where i.customerId=:customerId")
	public List<Invoice> findAllByCustomerId(@Param("customerId") Long customerId);
}