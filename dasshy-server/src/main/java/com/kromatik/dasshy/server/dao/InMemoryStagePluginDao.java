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

import com.kromatik.dasshy.thrift.model.TStagePlugin;
import com.kromatik.dasshy.thrift.model.TStageType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * In-memory plugin dao
 */
public class InMemoryStagePluginDao implements StagePluginDao
{

	@Override
	public void saveJar(TStagePlugin plugin, InputStream file)
	{

	}

	@Override
	public void dropJar(TStagePlugin plugin)
	{

	}

	@Override
	public void create(TStageType type, String identifier, TStagePlugin plugin)
	{

	}

	@Override
	public void update(TStageType type, String identifier, TStagePlugin plugin)
	{

	}

	@Override
	public void delete(TStageType type, String identifier)
	{

	}

	@Override
	public TStagePlugin getByTypeAndId(TStageType type, String identifier)
	{
		return null;
	}

	@Override
	public Collection<TStagePlugin> getByType(TStageType type)
	{
		return new ArrayList<>();
	}
}
