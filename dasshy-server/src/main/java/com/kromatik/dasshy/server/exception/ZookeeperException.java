package com.kromatik.dasshy.server.exception;

import com.kromatik.dasshy.core.exception.EngineException;

import javax.ws.rs.core.Response;

/**
 * Exception thrown when there is an exception in the zookeeper client
 * in trying to execute an operation on a zookeeper zNode
 */
public class ZookeeperException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public ZookeeperException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	@Override
	public int getStatus()
	{
		return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
	}
}
