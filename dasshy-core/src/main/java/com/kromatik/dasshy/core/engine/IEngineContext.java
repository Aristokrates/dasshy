package com.kromatik.dasshy.core.engine;

import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.config.IConfigurationFactory;

/**
 * Engine initialization context. Stores common context parameters used for starting the engine
 *
 * @param <T> engine's configuration
 */
public interface IEngineContext<T extends IEngineConfiguration>
{

	/**
	 * Get the factory used for creating the engine's configuration
	 *
	 * @return configuration factory
	 */
	IConfigurationFactory<T>	getConfigurationFactory();

	/**
	 * Get the factory used for creating the engine runtime environment
	 *
	 * @return engine runtime factory
	 */
	IEngineRuntimeFactory<T>	getEngineRuntimeFactory();
}
