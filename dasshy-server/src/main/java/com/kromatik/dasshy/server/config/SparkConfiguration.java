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
package com.kromatik.dasshy.server.config;

import com.kromatik.dasshy.core.config.IEngineCompositeConfiguration;
import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.config.impl.AbstractEngineConfiguration;
import com.kromatik.dasshy.core.exception.EngineConfigurationException;
import com.netflix.config.ConfigurationBackedDynamicPropertySupportImpl;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.apache.commons.configuration.AbstractConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Apache Spark related configuration
 */
public class SparkConfiguration extends AbstractEngineConfiguration
{

	/** map containing all spark properties */
	private final Map<String, DynamicStringProperty> sparkProperties = new HashMap<>();

	/**
	 * Default constructor
	 *
	 * @param composite composite configuration to register to
	 */
	public SparkConfiguration(final IEngineCompositeConfiguration composite)
	{
		super(composite);
	}

	/**
	 * @see IEngineConfiguration#loadConfiguration(DynamicPropertyFactory)
	 */
	@Override
	public void loadConfiguration(final DynamicPropertyFactory dynamicPropertyFactory)
	{
		// get all properties that starts with spark.
		final Object backingConfigurationSource = dynamicPropertyFactory.getBackingConfigurationSource();
		AbstractConfiguration configuration = null;

		if (backingConfigurationSource instanceof ConfigurationBackedDynamicPropertySupportImpl)
		{
			configuration = ((ConfigurationBackedDynamicPropertySupportImpl) backingConfigurationSource)
							.getConfiguration();
		}

		if (backingConfigurationSource instanceof AbstractConfiguration)
		{
			configuration = (AbstractConfiguration) backingConfigurationSource;
		}

		if (configuration == null)
		{
			throw new EngineConfigurationException("Configuration cannot be found", null);
		}

		// first load the default spark properties
		for (DasshyProperties dasshyProperty : DasshyProperties.values())
		{
			String propName = dasshyProperty.getPropertyName();
			if (propName.startsWith("spark"))
			{
				sparkProperties.put(propName, new DynamicStringProperty(propName, dasshyProperty.getDefaultValue()));
			}
		}

		final Iterator<String> sparkPropertiesIt = configuration.getKeys("spark");
		while (sparkPropertiesIt.hasNext())
		{
			final String propertyName = sparkPropertiesIt.next();
			final DynamicStringProperty propertyValue = dynamicPropertyFactory
							.getStringProperty(propertyName, DasshyProperties.forName(propertyName).getDefaultValue());
			if (propertyValue != null)
			{
				sparkProperties.put(propertyName, propertyValue);
			}
		}
	}

	/**
	 * @return unmodifiable map of spark properties
	 */
	public Map<String, DynamicStringProperty> getSparkProperties()
	{
		return Collections.unmodifiableMap(sparkProperties);
	}

	/**
	 * Set spark property
	 *
	 * @param propertyName  name of the spark property
	 * @param propertyValue value for the given property
	 */
	public void setSparkProperty(final String propertyName, final String propertyValue)
	{
		sparkProperties.put(propertyName, new DynamicStringProperty(propertyName, propertyValue));
	}

	/**
	 * @return spark master
	 */
	public String getMaster()
	{
		final DynamicStringProperty master = getSparkProperties().get(DasshyProperties.SPARK_MASTER.getPropertyName());
		return master != null ? master.get() : DasshyProperties.SPARK_MASTER.getDefaultValue();
	}

	/**
	 * @return spark streaming batch duration
	 */
	public long getBatchDuration()
	{
		final DynamicStringProperty batchDuration = getSparkProperties()
						.get(DasshyProperties.SPARK_BATCH_DURATION.getPropertyName());
		return batchDuration != null ?
						Long.valueOf(batchDuration.get()) :
						Long.valueOf(DasshyProperties.SPARK_BATCH_DURATION.getDefaultValue());
	}
}
