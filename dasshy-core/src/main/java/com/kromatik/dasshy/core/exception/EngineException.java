package com.kromatik.dasshy.core.exception;

/**
 * Exception thrown when there is an exception in the engine
 */
public abstract class EngineException extends RuntimeException
{
	/**
	 * Default constructor
	 *
	 * @param message message
	 * @param exception underlying cause
	 */
	public EngineException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	/**
	 * Get status code for the given exception
	 *
	 * @return status code
	 */
	public abstract int getStatus();

}
