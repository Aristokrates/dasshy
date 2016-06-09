package com.kromatik.dasshy.server.command;

/**
 * Abstract command wrapper
 */
public abstract class DasshyCommand<R>
{
	/** command key */
	private	String	commandKey;

	/**
	 * Default command constructor
	 *
	 * @param commandKey command key
	 */
	public DasshyCommand(final String commandKey)
	{
		this.commandKey = commandKey;
	}

	/**
	 * Run the command
	 *
	 * @return result of command execution
	 *
	 * @throws Exception
	 */
	public abstract R run() throws Exception;
}
