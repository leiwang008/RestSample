package org.safs.rest.sample.repository;
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


import java.util.List;

/**
 * @author Lei Wang
 *
 */
public interface IRepository<T> {
	public T create(T element);

	public boolean delete(Long id, Class<T> type);

	public T update(Long id, T element);

	public List<T> findAll(Class<T> type);

	public T findById(Long id, Class<T> type);

	public int getCount(Class<T> type);

	public void clear(Class<T> type);
}
