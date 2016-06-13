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
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;

import static com.kromatik.dasshy.server.config.DasshyProperties.*;

/**
 * Apache Kafka consumer related configuration
 */
public class KafkaConfiguration extends AbstractEngineConfiguration
{
	/** zookeeper session timeout */
	private DynamicIntProperty		zookeeperSessionTimeoutMs;

	/** zookeeper connection timeout */
	private DynamicIntProperty		zookeeperConnectionTimeoutMs;

	/**
	 * Default constructor
	 *
	 * @param composite composite configuration to register to
	 */
	public KafkaConfiguration(final IEngineCompositeConfiguration composite)
	{
		super(composite);
	}

	/**
	 * @see IEngineConfiguration#loadConfiguration(DynamicPropertyFactory)
	 */
	@Override
	public void loadConfiguration(final DynamicPropertyFactory dynamicPropertyFactory)
	{
		zookeeperSessionTimeoutMs = dynamicPropertyFactory
						.getIntProperty(ZOOKEEPER_SESSION_TIMEOUT.getPropertyName(),
										ZOOKEEPER_SESSION_TIMEOUT.getDefaultValueAsInt());

		zookeeperConnectionTimeoutMs = dynamicPropertyFactory
						.getIntProperty(ZOOKEEPER_CONNECTION_TIMEOUT.getPropertyName(),
										ZOOKEEPER_SESSION_TIMEOUT.getDefaultValueAsInt());
	}

	/**
	 * @return zookeeper session timeout
	 */
	public Integer getZookeeperSessionTimeoutMs()
	{
		return zookeeperSessionTimeoutMs.get();
	}

	public Integer getZookeeperConnectionTimeoutMs()
	{
		return zookeeperConnectionTimeoutMs.get();
	}
}
