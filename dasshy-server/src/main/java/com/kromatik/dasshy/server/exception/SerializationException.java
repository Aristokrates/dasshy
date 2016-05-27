package com.kromatik.dasshy.server.exception;

import com.kromatik.dasshy.core.exception.EngineException;

import javax.ws.rs.core.Response;

/**
 * Thrift serialization exception
 */
public class SerializationException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public SerializationException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	@Override
	public int getStatus()
	{
		return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
	}
}
