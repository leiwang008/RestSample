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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.safs.rest.sample.model.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lei Wang
 *
 */
public abstract class InMemoryRepository <T extends Identifiable> implements IRepository<T>{

	@Autowired
	private IdGenerator idGenerator;

	protected List<T> elements = Collections.synchronizedList(new ArrayList<T>());

	@Override
	public T create(T element) {
		element.setId(idGenerator.nextLong());
		elements.add(element);
		return element;
	}

	private boolean isSameType(T e, Class type){
		return type.getName().equals(e.getClass().getName());
	}

	@Override
	public boolean delete(Long id, Class type) {
		return elements.removeIf(e -> isSameType(e, type) && e.getId().equals(id));
	}

	@Override
	public T update(Long id, T updated) {
		if(updated==null){
			return null;
		}else{
			Optional<T> element = find(id, updated.getClass());
			if(element==null){
				return null;
			}else{
				element.ifPresent(original -> updateIfExists(original, updated));
				return findById(id, updated.getClass());
			}
		}
	}

	@Override
	public List<T> findAll(Class type) {
		return elements.stream().filter(e -> isSameType(e, type)).collect(Collectors.toList());
	}

	@Override
	public T findById(Long id, Class type) {
		Optional<T> optional = find(id, type);
		if(optional==null){
			return null;
		}else{
			try{
				return optional.get();
			}catch(NoSuchElementException nse){
				return null;
			}
		}
	}

	private Optional<T> find(Long id, Class type) {
		return elements.stream().filter(e -> isSameType(e, type) && e.getId().equals(id)).findFirst();
	}

	@Override
	public int getCount(Class type) {
		return findAll(type).size();
	}

	@Override
	public void clear(Class type) {
		List<T> toDeletes = findAll(type);
		elements.removeAll(toDeletes);
	}

	protected abstract void updateIfExists(T original, T desired);
}
