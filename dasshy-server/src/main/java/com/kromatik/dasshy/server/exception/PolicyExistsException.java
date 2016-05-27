package com.kromatik.dasshy.server.exception;

import com.kromatik.dasshy.core.exception.EngineException;

import javax.ws.rs.core.Response;

/**
 * Exception thrown when a given policy already exists
 * and we are trying to create a new one with the same identifier
 */
public class PolicyExistsException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public PolicyExistsException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	/**
	 * Message only constructor
	 *
	 * @param message	message
	 */
	public PolicyExistsException(final String message)
	{
		this(message, null);
	}


	@Override
	public int getStatus()
	{
		return Response.Status.CONFLICT.getStatusCode();
	}
}
