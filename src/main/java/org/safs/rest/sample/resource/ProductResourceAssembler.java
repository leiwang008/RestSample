package org.safs.rest.sample.resource;

import org.safs.rest.sample.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductResourceAssembler extends ResourceAssembler<Product, ProductResource> {


	@Override
	public ProductResource toResource(Product customer) {
		ProductResource resource = new ProductResource(customer);

		return resource;
	}
}
