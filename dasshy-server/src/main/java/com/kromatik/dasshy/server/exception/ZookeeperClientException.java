package com.kromatik.dasshy.server.exception;

import com.kromatik.dasshy.core.exception.EngineException;

/**
 * Exception thrown when there is an exception in the zookeper client in trying to manage the zookeeper connection
 */
public class ZookeeperClientException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public ZookeeperClientException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	@Override
	public int getStatus()
	{
		return 500;
	}
}
