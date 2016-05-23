package com.kromatik.dasshy.core.engine;

import com.kromatik.dasshy.core.config.IEngineConfiguration;

/**
 * Factory for creating the engine runtime environment
 *
 * @param <T> engine configuration
 */
public interface IEngineRuntimeFactory<T extends IEngineConfiguration>
{

	/**
	 * Builds the engine runtime environment
	 *
	 * @param config engine configuration
	 * @return engine runtime environment
	 */
	IEngineRuntime build(final T config);
}
