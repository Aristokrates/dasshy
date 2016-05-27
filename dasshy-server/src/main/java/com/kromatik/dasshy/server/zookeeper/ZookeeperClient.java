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
		if (!closed)
		{
			if (curatorFramework != null)
			{
				curatorFramework.close();
				closed = true;
			}
		}
	}
}
