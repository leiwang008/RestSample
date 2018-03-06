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

import org.safs.rest.sample.model.Customer;
import org.springframework.stereotype.Repository;

/**
 * @author Lei Wang
 *
 */

@Repository
public class CustomerRepository extends InMemoryRepository<Customer>{

	@Override
	protected void updateIfExists(Customer original, Customer desired) {
		original.setCity(desired.getCity());
		original.setFirstName(desired.getFirstName());
		original.setLastName(desired.getLastName());
		original.setStreet(desired.getStreet());
	}
}
