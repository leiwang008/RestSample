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
import org.springframework.stereotype.Component;

/**
 * @author Lei Wang
 *
 */
@Component
public class ItemResourceAssembler extends ResourceAssembler<Item, ItemResource> {

	@Override
	public ItemResource toResource(Item domainObject) {
		return new ItemResource(domainObject);
	}

}
