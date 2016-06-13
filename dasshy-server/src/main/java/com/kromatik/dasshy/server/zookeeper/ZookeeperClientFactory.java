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
package com.kromatik.dasshy.server.zookeeper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating ZookeeperClient instances cached by zookeeper cluster(connection string)
 */
public class ZookeeperClientFactory implements IZookeeperClientFactory
{

	/** singleton instance */
	private static final IZookeeperClientFactory INSTANCE = new ZookeeperClientFactory();

	/** map of ZookeeperClients by zookeeper cluster */
	private final Map<String, IZookeeperClient> zkClients = new HashMap<>();

	/**
	 * Default private constructor
	 */
	private ZookeeperClientFactory()
	{
		// no-op
	}

	/**
	 * @return ZookeeperClientFactory instance
	 */
	public static IZookeeperClientFactory getInstance()
	{
		return INSTANCE;
	}

	@Override
	public synchronized IZookeeperClient createZkClient(final IZookeeperClientProperties properties)
	{
		final String zkConnect = properties.getConnectionString();
		if (!zkClients.containsKey(zkConnect))
		{
			final IZookeeperClient zkClient = new ZookeeperClient(properties);
			zkClients.put(zkConnect, zkClient);
		}
		return zkClients.get(zkConnect);
	}

	@Override
	public void close() throws IOException
	{
		for (final IZookeeperClient client : zkClients.values())
		{
			client.close();
		}
	}
}
