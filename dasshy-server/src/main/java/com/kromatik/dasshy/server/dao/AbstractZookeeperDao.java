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
	private final IZookeeperClientFactory			clientFactory;

	/** properties */
	private final IZookeeperClientProperties		properties;

	/**
	 * Default constructor
	 *
	 * @param clientFactory zookeeper factory
	 * @param properties zookeeper properties
	 */
	public AbstractZookeeperDao(
					final IZookeeperClientFactory clientFactory,
					final IZookeeperClientProperties properties
	)
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
