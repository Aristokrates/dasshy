package com.kromatik.dasshy.server.exception;

import com.kromatik.dasshy.core.exception.EngineException;

/**
 * Thrown when there is an error in initializing stages within a policy
 */
public class StageInitException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public StageInitException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	/**
	 * Message only constructor
	 *
	 * @param message	message
	 */
	public StageInitException(final String message)
	{
		this(message, null);
	}

	@Override
	public int getStatus()
	{
		return 500;
	}
}
