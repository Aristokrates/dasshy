package com.kromatik.dasshy.server.config;

import com.kromatik.dasshy.core.config.IEngineCompositeConfiguration;
import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.config.impl.AbstractEngineConfiguration;
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
		zookeeperProperties.put(propertyName, new DynamicStringProperty(propertyName, zkConnect));
	}
}
