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
