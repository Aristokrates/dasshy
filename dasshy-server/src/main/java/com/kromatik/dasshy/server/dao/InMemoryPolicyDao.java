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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory policy dao
 */
public class InMemoryPolicyDao implements PolicyDao
{

	private Map<String, TPolicy> memoryStore = new ConcurrentHashMap<>();

	@Override
	public void create(TPolicy policy)
	{
		memoryStore.put(policy.getId(), policy);
	}

	@Override
	public void update(TPolicy policy)
	{
		memoryStore.put(policy.getId(), policy);
	}

	@Override
	public void delete(TPolicy policy)
	{
		memoryStore.remove(policy.getId());
	}

	@Override
	public TPolicy get(String id)
	{
		return memoryStore.get(id);
	}

	@Override
	public boolean exists(String id)
	{
		return memoryStore.containsKey(id);
	}

	@Override
	public Collection<TPolicy> list()
	{
		return memoryStore.values();
	}
}
