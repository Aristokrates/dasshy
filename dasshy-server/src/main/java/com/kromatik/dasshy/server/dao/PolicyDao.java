/**
 * Dasshy - Real time and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions (http://kromatiksolutions.com)
 *
 * This file is part of Dasshy
 *
 * Dasshy is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Dasshy is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dasshy.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kromatik.dasshy.server.dao;

import com.kromatik.dasshy.thrift.model.TPolicy;

import java.util.Collection;

/**
 * Policy dao
 */
public interface PolicyDao
{

	/**
	 * Saves the policy
	 *
	 * @param policy policy model
	 */
	void create(final TPolicy policy);

	/**
	 * Updates a policy
	 *
	 * @param policy policy model
	 */
	void update(final TPolicy policy);

	/**
	 * Deletes a policy
	 *
	 * @param policy policy model
	 */
	void delete(final TPolicy policy);

	/**
	 * Get by id
	 *
	 * @return policy model
	 */
	TPolicy get(final String id);

	/**
	 * Checks if a policy with the same id exists
	 *
	 * @param id
	 * @return true/false
	 */
	boolean exists(final String id);

	/**
	 * List the policies
	 *
	 * @return policy list
	 */
	Collection<TPolicy> list();

}
