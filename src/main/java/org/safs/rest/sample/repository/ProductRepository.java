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

import org.safs.rest.sample.model.Product;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */

@Repository
public class ProductRepository extends InMemoryRepository<Product>{

	@Override
	protected void updateIfExists(Product original, Product desired) {
		original.setName(desired.getName());
		original.setPrice(desired.getPrice());
	}
}
