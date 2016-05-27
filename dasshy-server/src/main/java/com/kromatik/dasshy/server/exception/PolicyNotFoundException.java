package com.kromatik.dasshy.server.exception;

import com.kromatik.dasshy.core.exception.EngineException;

import javax.ws.rs.core.Response;

/**
 * Exception thrown when policy cannot be found
 */
public class PolicyNotFoundException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public PolicyNotFoundException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	/**
	 * Message only constructor
	 *
	 * @param message	message
	 */
	public PolicyNotFoundException(final String message)
	{
		this(message, null);
	}

	@Override
	public int getStatus()
	{
		return Response.Status.NOT_FOUND.getStatusCode();
	}
}
