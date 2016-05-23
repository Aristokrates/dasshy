package com.kromatik.dasshy.server.zookeeper;

import org.apache.curator.RetryPolicy;

/**
 * Zookeeper client properties needed to setup zookeeper connections
 */
public interface IZookeeperClientProperties
{
	/**
	 * @return zookeeper connection string
	 */
	String getConnectionString();

	/**
	 * Zookeeper retry policy used when a zookeeper operation fails
	 *
	 * @return retry policy
	 */
	RetryPolicy	getRetryPolicy();

	/**
	 * Zookeeper connection timeout (in miliseconds)
	 *
	 * @return connection timeout
	 */
	int getConnectionSessionTimeout();
}
