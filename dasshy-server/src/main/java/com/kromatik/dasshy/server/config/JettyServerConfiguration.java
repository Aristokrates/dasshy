package com.kromatik.dasshy.server.config;

import com.kromatik.dasshy.core.config.IEngineCompositeConfiguration;
import com.kromatik.dasshy.core.config.impl.AbstractEngineConfiguration;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.kromatik.dasshy.server.config.DasshyProperties.*;

/**
 * Configuration for the embedded jetty server
 */
public class JettyServerConfiguration extends AbstractEngineConfiguration
{
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

		String localHost = null;
		try
		{
			localHost = InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException e)
		{
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
