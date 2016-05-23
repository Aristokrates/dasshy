package com.kromatik.dasshy.core.config;

import com.netflix.config.DynamicPropertyFactory;

/**
 * Engine configuration
 */
public interface IEngineConfiguration
{
	/**
	 * Load the configuration using property factory
	 *
	 * @param dynamicPropertyFactory property factory
	 */
	void loadConfiguration(final DynamicPropertyFactory dynamicPropertyFactory);
}
