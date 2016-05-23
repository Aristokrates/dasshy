package com.kromatik.dasshy.core.config;

/**
 * Factory for loading the configuration for the given engine
 *
 * @param <T> configuration to be build
 */
public interface IConfigurationFactory<T extends IEngineConfiguration>
{

	/**
	 * Builds the configuration
	 *
	 * @param klass configuration class
	 *
	 * @return configuration
	 */
	T build(final Class<T> klass);
}
