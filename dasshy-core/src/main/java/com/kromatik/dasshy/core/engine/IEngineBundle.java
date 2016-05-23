package com.kromatik.dasshy.core.engine;

import com.kromatik.dasshy.core.config.IEngineConfiguration;

/**
 *  The bundles represents a piece of code executed as part of the engine initialization.
 *  Any custom initialization needs to go in its own bundle
 *
 *  @param <T> engine configuration
 */
public interface IEngineBundle<T extends IEngineConfiguration>
{
	/**
	 * Run the bundle
	 *
	 * @param configuration engine's configuration
	 * @param env engine's environment
	 */
	void run(final T configuration, final IEngineRuntime env);

}
