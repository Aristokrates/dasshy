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

import com.kromatik.dasshy.server.zookeeper.IZookeeperClient;
import com.kromatik.dasshy.server.zookeeper.IZookeeperClientFactory;
import com.kromatik.dasshy.server.zookeeper.IZookeeperClientProperties;

/**
 * Zookeeper Dao
 */
public class AbstractZookeeperDao
{
	/** client factory */
	private final IZookeeperClientFactory clientFactory;

	/** properties */
	private final IZookeeperClientProperties properties;

	/**
	 * Default constructor
	 *
	 * @param clientFactory zookeeper factory
	 * @param properties    zookeeper properties
	 */
	public AbstractZookeeperDao(final IZookeeperClientFactory clientFactory,
					final IZookeeperClientProperties properties)
	{
		this.clientFactory = clientFactory;
		this.properties = properties;
	}

	/**
	 * Builds the zookeeper client
	 *
	 * @return zookeeper client
	 */
	protected IZookeeperClient getClient()
	{
		return clientFactory.createZkClient(properties);
	}
}
