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
	private static final IZookeeperClientFactory	INSTANCE	=	new ZookeeperClientFactory();

	/** map of ZookeeperClients by zookeeper cluster */
	private final Map<String, IZookeeperClient>		zkClients 	=	new HashMap<>();

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
	public static IZookeeperClientFactory	getInstance()
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
