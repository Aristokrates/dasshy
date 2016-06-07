package com.kromatik.dasshy.sdk;

/**
 * Exception occurred within a stage
 */
public class StageException extends RuntimeException
{

	/**
	 * Default constructor
	 */
	public StageException()
	{
		// no-op
	}

	/**
	 * Exception with message
	 *
	 * @param message message
	 */
	public StageException(final String message)
	{
		super(message);
	}

	/**
	 * Exception with message and cause
	 *
	 * @param message message
	 * @param cause a cause
	 */
	public StageException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Exception with a cause
	 *
	 * @param cause a cause
	 */
	public StageException(final Throwable cause)
	{
		super(cause);
	}
}
