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
import com.kromatik.dasshy.core.config.impl.AbstractEngineConfiguration;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.kromatik.dasshy.server.config.DasshyProperties.*;

/**
 * Configuration for the embedded jetty server
 */
public class JettyServerConfiguration extends AbstractEngineConfiguration
{

	private static final Logger LOGGER	=	LoggerFactory.getLogger(JettyServerConfiguration.class);

	/** server host */
	private DynamicStringProperty		host;

	/** server port */
	private DynamicIntProperty			port;

	/** server max idle time */
	private DynamicIntProperty			maxIdleTime;

	/** context path */
	private DynamicStringProperty		contextPath;

	/**
	 * Default constructor
	 *
	 * @param composite composite configuration to register to
	 */
	public JettyServerConfiguration(final IEngineCompositeConfiguration composite)
	{
		super(composite);
	}

	@Override
	public void loadConfiguration(final DynamicPropertyFactory dynamicPropertyFactory)
	{

		String localHost;
		try
		{
			localHost = InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException e)
		{
			LOGGER.info("Unknown local host address", e);
			localHost = SERVER_ADDRESS.getDefaultValue();
		}

		host = dynamicPropertyFactory
						.getStringProperty(SERVER_ADDRESS.getPropertyName(), localHost);

		port = dynamicPropertyFactory
						.getIntProperty(SERVER_PORT.getPropertyName(),
										SERVER_PORT.getDefaultValueAsInt());

		maxIdleTime = dynamicPropertyFactory
						.getIntProperty(SERVER_MAX_IDLE.getPropertyName(),
										SERVER_MAX_IDLE.getDefaultValueAsInt());

		contextPath = dynamicPropertyFactory
						.getStringProperty(SERVER_CONTEXT_PATH.getPropertyName(),
										SERVER_CONTEXT_PATH.getDefaultValue());
	}

	public String getHost()
	{
		return host.get();
	}

	public Integer getPort()
	{
		return port.get();
	}

	public Integer getMaxIdleTime()
	{
		return maxIdleTime.get();
	}

	public String getContextPath()
	{
		return contextPath.get();
	}
}
