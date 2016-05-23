package com.kromatik.dasshy.core.engine;

import com.kromatik.dasshy.core.config.IEngineConfiguration;

/**
 * Pre-start environment of the engine
 *
 * @param <T> engine configuration
 */
public interface IEngineBootstrap<T extends IEngineConfiguration>
{

	/**
	 * Run the given bundles within the engine environment
	 *
	 * @param configuration engine configuration
	 * @param engineRuntime engine runtime environment
	 */
	void run(final T configuration, final IEngineRuntime engineRuntime);

	/**
	 * Adds the bundle to the engine bootstrap
	 *
	 * @param bundle a bundle
	 */
	void addBundle(final IEngineBundle<T> bundle);

}
