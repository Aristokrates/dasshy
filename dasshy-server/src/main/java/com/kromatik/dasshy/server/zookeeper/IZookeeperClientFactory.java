package com.kromatik.dasshy.server.zookeeper;

import java.io.Closeable;

/**
 * Factory interface that creates ZookeeperClient objects
 */
public interface IZookeeperClientFactory extends Closeable
{
	/**
	 * Creates ZookeeperClient instances if not already initialized for the connection String
	 *
	 * @param properties client properties
	 * @return ZookeeperClient instance
	 */
	IZookeeperClient createZkClient(final IZookeeperClientProperties properties);

}
