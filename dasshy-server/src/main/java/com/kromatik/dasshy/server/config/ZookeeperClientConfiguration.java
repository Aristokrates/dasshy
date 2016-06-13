/**
 * Dasshy - Real time Streaming and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions
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
import com.kromatik.dasshy.server.zookeeper.IZookeeperClientProperties;
import com.netflix.config.ConfigurationBackedDynamicPropertySupportImpl;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.kromatik.dasshy.server.config.DasshyProperties.*;

/**
 * Zookeeper client configuration
 */
public class ZookeeperClientConfiguration extends AbstractEngineConfiguration implements IZookeeperClientProperties
{

	/** zookeeper properties */
	private final Map<String, DynamicStringProperty>		zookeeperProperties	=	new HashMap<>();

	/**
	 * Default constructor
	 *
	 * @param composite composite configuration to register to
	 */
	public ZookeeperClientConfiguration(final IEngineCompositeConfiguration composite)
	{
		super(composite);
	}

	/**
	 * @see IEngineConfiguration#loadConfiguration(DynamicPropertyFactory)
	 */
	@Override
	public void loadConfiguration(final DynamicPropertyFactory dynamicPropertyFactory)
	{
		// get all properties that starts with zookeeper.
		Object backingConfigurationSource = dynamicPropertyFactory.getBackingConfigurationSource();
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

		// first load the default zk properties
		for (DasshyProperties dasshyProperty : DasshyProperties.values())
		{
			String propName = dasshyProperty.getPropertyName();
			if (propName.startsWith("zookeeper"))
			{
				zookeeperProperties.put(propName, new DynamicStringProperty(propName, dasshyProperty.getDefaultValue()));
			}
		}

		Iterator<String> zookeeperPropertiesIt = configuration.getKeys("zookeeper");
		while (zookeeperPropertiesIt.hasNext())
		{
			String propertyName = zookeeperPropertiesIt.next();
			DynamicStringProperty propertyValue = dynamicPropertyFactory.getStringProperty(propertyName,
							DasshyProperties.forName(propertyName).getDefaultValue());
			if (propertyValue != null)
			{
				zookeeperProperties.put(propertyName, propertyValue);
			}
		}
	}

	/**
	 * @return unmodifiable map of zookeeper client properties
	 */
	public Map<String, DynamicStringProperty> getZookeeperProperties()
	{
		return Collections.unmodifiableMap(zookeeperProperties);
	}

	/**
	 * Set zookeeper property
	 *
	 * @param propertyName name of the property
	 * @param propertyValue value for the given property
	 */
	public void setZookeeperProperty(final String propertyName, String propertyValue)
	{
		zookeeperProperties.put(propertyName, new DynamicStringProperty(propertyName, propertyValue));
	}

	/**
	 * @see IZookeeperClientProperties#getConnectionString()
	 */
	@Override
	public String getConnectionString()
	{
		final DynamicStringProperty zkConnect = getZookeeperProperties().get(ZOOKEEPER_CONNECTION_STRING.getPropertyName());
		return zkConnect != null ? zkConnect.get() : ZOOKEEPER_CONNECTION_STRING.getDefaultValue();
	}

	/**
	 * @see IZookeeperClientProperties#getRetryPolicy()
	 */
	@Override
	public RetryPolicy getRetryPolicy()
	{
		return new ExponentialBackoffRetry(1000, 50);
	}

	/**
	 * @see IZookeeperClientProperties#getConnectionSessionTimeout()
	 */
	@Override
	public int getConnectionSessionTimeout()
	{
		return 3000;
	}

	/**
	 * Set zk connection string
	 *
	 * @param zkConnect zk connect
	 */
	public void setConnectionString(final String zkConnect)
	{
		String propertyName = ZOOKEEPER_CONNECTION_STRING.getPropertyName();
		setZookeeperProperty(propertyName, zkConnect);
	}
}
