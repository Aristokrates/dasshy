package com.kromatik.dasshy.server.exception;

import com.kromatik.dasshy.core.exception.EngineException;

import javax.ws.rs.core.Response;

/**
 * Throw when the policy is invalid
 */
public class InvalidPolicyException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public InvalidPolicyException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	/**
	 * Message only constructor
	 *
	 * @param message	message
	 */
	public InvalidPolicyException(final String message)
	{
		this(message, null);
	}

	@Override
	public int getStatus()
	{
		return Response.Status.BAD_REQUEST.getStatusCode();
	}
}
