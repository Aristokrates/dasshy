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

import com.kromatik.dasshy.core.config.IEngineCompositeConfiguration;
import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic composite engine configuration that is composed of nested engine configurations.
 */
public abstract class AbstractEngineCompositeConfiguration implements IEngineCompositeConfiguration
{

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEngineCompositeConfiguration.class);

	/** engine configurations */
	private final List<IEngineConfiguration> engineConfigurations = new ArrayList<>();

	/**
	 * Default constructor
	 */
	protected AbstractEngineCompositeConfiguration()
	{
		// default constructor
	}

	/**
	 * @see IEngineConfiguration#loadConfiguration(DynamicPropertyFactory)
	 */
	@Override
	public void loadConfiguration(final DynamicPropertyFactory dynamicPropertyFactory)
	{

		for (IEngineConfiguration configuration : engineConfigurations)
		{
			LOGGER.info("Loading engine configuration: {}", configuration.getClass().getSimpleName());
			// loading configuration
			configuration.loadConfiguration(dynamicPropertyFactory);
		}
	}

	/**
	 * @see IEngineCompositeConfiguration#addConfiguration(IEngineConfiguration)
	 */
	@Override
	public void addConfiguration(final IEngineConfiguration configuration)
	{
		engineConfigurations.add(configuration);
	}
}
