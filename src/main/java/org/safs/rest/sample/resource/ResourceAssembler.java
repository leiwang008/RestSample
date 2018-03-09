package org.safs.rest.sample.resource;

import java.util.Collection;
import java.util.stream.Collectors;

import org.safs.rest.sample.util.Util;

/**
 * This class aims to convert the domain data to resource data for presentation layer.
 * @author Lei Wang
 *
 * @param <DomainType>
 * @param <ResourceType>
 */
public abstract class ResourceAssembler<DomainType, ResourceType> {

	public abstract ResourceType toResource(DomainType domainObject);

	public Collection<ResourceType> toResourceCollection(Collection<DomainType> domainObjects) {
		return domainObjects.stream().map(o -> toResource(o)).collect(Collectors.toList());
	}
	public Collection<ResourceType> toResourceCollection(Iterable<DomainType> domainObjects) {
		return toResourceCollection(Util.toCollection(domainObjects));
	}
}
