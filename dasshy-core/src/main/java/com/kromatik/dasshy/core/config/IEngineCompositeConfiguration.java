package com.kromatik.dasshy.core.config;

/**
 *  Composite engine configuration that is composed of many other configurations
 */
public interface IEngineCompositeConfiguration extends IEngineConfiguration
{

	/**
	 * Adds the given configuration to the composite one
	 *
	 * @param configuration
	 */
	void addConfiguration(IEngineConfiguration configuration);
}
