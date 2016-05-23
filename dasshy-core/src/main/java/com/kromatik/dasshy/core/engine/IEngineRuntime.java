package com.kromatik.dasshy.core.engine;

/**
 * Runtime environment of the engine
 */
public interface IEngineRuntime
{

	/**
	 * Manage a component within the runtime environment.
	 * This also starts the managed component
	 *
	 * @param component a component to be managed within the engine runtime environment
	 */
	void manage(final IEngineComponent component);

	/**
	 * Shutdown the engine runtime environment
	 */
	void shutdown();

}
