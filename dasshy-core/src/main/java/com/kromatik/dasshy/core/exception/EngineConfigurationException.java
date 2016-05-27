package com.kromatik.dasshy.core.exception;

/**
 * Exception thrown when there is an exception in the loading an engine configuration
 */
public class EngineConfigurationException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public EngineConfigurationException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	@Override
	public int getStatus()
	{
		return 500;
	}
}
