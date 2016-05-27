package com.kromatik.dasshy.core.exception;

/**
 * Thrown when there is problem is starting stopping of the engines components
 */
public class EngineStartupException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public EngineStartupException(String message, Throwable exception)
	{
		super(message, exception);
	}

	@Override
	public int getStatus()
	{
		return 500;
	}
}
