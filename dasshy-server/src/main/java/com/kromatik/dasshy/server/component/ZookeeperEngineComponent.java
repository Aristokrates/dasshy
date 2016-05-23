package com.kromatik.dasshy.server.component;

import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.server.zookeeper.IZookeeperClientFactory;

import java.io.IOException;

/**
 * Engine component that manages zookeeper connections
 */
public class ZookeeperEngineComponent implements IEngineComponent
{
	/** factory for creating zookeeper client connections */
	private final IZookeeperClientFactory		clientFactory;

	/**
	 * Default constructor
	 *
	 * @param factory zk client factory
	 */
	public ZookeeperEngineComponent(final IZookeeperClientFactory factory)
	{
		clientFactory = factory;
	}

	/**
	 * @see IEngineComponent#start()
	 */
	@Override
	public void start()
	{
		// no need to explicitly start the zookeeper connections, since they are lazily initialized
	}

	/**
	 * @see IEngineComponent#stop()
	 */
	@Override
	public void stop()
	{
		try
		{
			clientFactory.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
