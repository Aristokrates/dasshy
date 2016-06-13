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

import com.kromatik.dasshy.server.exception.ZookeeperClientException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Zookeeper client
 */
public class ZookeeperClient implements IZookeeperClient
{

	private static final Logger	LOGGER	=	LoggerFactory.getLogger(ZookeeperClient.class);

	/** underlying curator client */
	private CuratorFramework							curatorFramework = null;

	/** zk properties */
	private final IZookeeperClientProperties			zookeeperClientProperties;

	/** tracks the state of the underlying zookeeper connection */
	private boolean										closed		=	true;

	/**
	 * Creates zookeeper client for the given properties
	 *
	 * @param properties properties
	 */
	public ZookeeperClient(final IZookeeperClientProperties properties)
	{
		zookeeperClientProperties = properties;

		try
		{

			curatorFramework = CuratorFrameworkFactory.builder().connectString(zookeeperClientProperties.getConnectionString())
							.connectionTimeoutMs(zookeeperClientProperties.getConnectionSessionTimeout())
							.sessionTimeoutMs(zookeeperClientProperties.getConnectionSessionTimeout())
							.retryPolicy(zookeeperClientProperties.getRetryPolicy()).build();

			curatorFramework.getConnectionStateListenable().addListener(new ZookeeperConnectionStateListener());

			curatorFramework.start();
			closed = false;

			LOGGER.info("Zookeeper client started");

			boolean isConnected = curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut();
			if (!isConnected)
			{
				LOGGER.warn("Zookeeper state not healthy.");
			}

		}
		catch (final Exception e)
		{
			close();
			throw new ZookeeperClientException("Zookeeper connection has failed", e);
		}
	}

	/**
	 * Zookeeper connection state listener
	 */
	private static class ZookeeperConnectionStateListener implements ConnectionStateListener
	{

		@Override
		public void stateChanged(CuratorFramework client, ConnectionState newState)
		{
			switch (newState)
			{
				case LOST:
					LOGGER.warn("Zookeeper connection has been lost");
					break;

				case RECONNECTED:
					LOGGER.info("Zookeeper connection has been re-established");
					break;

				case SUSPENDED:
					LOGGER.warn("Zookeeper connection has been suspended");
					break;

				default:
					break;
			}
		}
	}

	/**
	 * @see IZookeeperClient#getCuratorFramework()
	 */
	@Override
	public CuratorFramework getCuratorFramework()
	{
		return curatorFramework;
	}

	@Override
	public void close()
	{
		if (!closed && curatorFramework != null)
		{
			curatorFramework.close();
			closed = true;
		}
	}
}
