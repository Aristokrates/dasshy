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

import com.kromatik.dasshy.server.zookeeper.IZookeeperClientFactory;
import com.kromatik.dasshy.server.zookeeper.IZookeeperClientProperties;
import com.kromatik.dasshy.thrift.model.TStagePlugin;
import com.kromatik.dasshy.thrift.model.TStageType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Dao that manages plugins in Zookeeper
 */
public class ZookeeperStagePluginDao extends AbstractZookeeperDao implements StagePluginDao
{

	public static final String	PLUGINS_PATH	=	"/plugins";

	public static final String	JARS_PATH		=	"/jars";

	/**
	 * Default constructor
	 *
	 * @param clientFactory zookeeper factory
	 * @param properties    zookeeper properties
	 */
	public ZookeeperStagePluginDao(IZookeeperClientFactory clientFactory, IZookeeperClientProperties properties)
	{
		super(clientFactory, properties);
	}

	@Override
	public void saveJar(final TStagePlugin plugin, final InputStream file)
	{
		// TODO (pai)
	}

	@Override
	public void dropJar(final TStagePlugin plugin)
	{
		// TODO (pai)
	}

	@Override
	public void create(final TStageType type, final String identifier, final TStagePlugin plugin)
	{
		// TODO (pai)
	}

	@Override
	public void update(final TStageType type, final String identifier, final TStagePlugin plugin)
	{
		// TODO (pai)
	}

	@Override
	public void delete(TStageType type, String identifier)
	{
		// TODO (pai)
	}

	@Override
	public TStagePlugin getByTypeAndId(final TStageType type, final String identifier)
	{
		// TODO (pai)
		return null;
	}

	@Override
	public Collection<TStagePlugin> getByType(final TStageType type)
	{
		// TODO (pai)
		return new ArrayList<>();
	}
}
