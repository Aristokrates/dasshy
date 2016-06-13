/**
 * Dasshy - Real time and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions (http://kromatiksolutions.com)
 *
 * This file is part of Dasshy
 *
 * Dasshy is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Dasshy is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dasshy.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kromatik.dasshy.core.config.impl;

import com.kromatik.dasshy.core.config.IConfigurationFactory;
import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.exception.EngineConfigurationException;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConcurrentMapConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * Simple implementation of {@link IConfigurationFactory} that uses
 * <a href="https://github.com/Netflix/archaius/wiki">Netflix Archaius</a>
 * for building the configuration
 *
 * @param <T> engine configuration
 */
public class ArchaiusConfigurationFactory<T extends IEngineConfiguration> implements IConfigurationFactory<T>
{
	/**
	 * @see IConfigurationFactory
	 */
	@Override
	public T build(final Class<T> klass)
	{

		final T configuration = newInstance(klass);

		// load the configuration from system property
		final SystemConfiguration systemConfiguration = new SystemConfiguration();
		final ConcurrentMapConfiguration configFromSystemProperties = new ConcurrentMapConfiguration(
						systemConfiguration);

		// create a hierarchy of configurations
		final ConcurrentCompositeConfiguration finalConfig = new ConcurrentCompositeConfiguration();
		finalConfig.addConfiguration(configFromSystemProperties, "systemConfig");

		// install the configuration
		try
		{
			ConfigurationManager.install(finalConfig);
		}
		catch (final IllegalStateException exception)
		{
			throw new EngineConfigurationException("Configuration installation has failed", exception);
		}

		// load the configuration
		configuration.loadConfiguration(DynamicPropertyFactory.getInstance());
		return configuration;
	}

	/**
	 * Creates an instance of the given configuration class
	 *
	 * @param klass
	 * @return configuration object
	 */
	private T newInstance(final Class<T> klass)
	{

		T configuration = null;
		try
		{

			configuration = klass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new EngineConfigurationException("Error instantiating configuration class: " + klass, e);
		}

		return configuration;
	}
}
