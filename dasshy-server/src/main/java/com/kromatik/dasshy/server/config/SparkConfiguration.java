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
	private final Map<String, DynamicStringProperty>				sparkProperties	=	new HashMap<>();

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

		final Iterator<String> sparkPropertiesIt = configuration.getKeys("spark");
		while (sparkPropertiesIt.hasNext())
		{
			final String propertyName = sparkPropertiesIt.next();
			final DynamicStringProperty propertyValue = dynamicPropertyFactory.getStringProperty(propertyName,
							DasshyProperties.forName(propertyName).getDefaultValue());
			if (propertyValue != null)
			{
				sparkProperties.put(propertyName, propertyValue);
			}
		}
	}

	/**
	 *
	 * @return unmodifiable map of spark properties
	 */
	public Map<String, DynamicStringProperty> getSparkProperties()
	{
		return Collections.unmodifiableMap(sparkProperties);
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
		final DynamicStringProperty batchDuration = getSparkProperties().get(DasshyProperties.SPARK_BATCH_DURATION.getPropertyName());
		return batchDuration != null ?
						Long.valueOf(batchDuration.get()) :
						Long.valueOf(DasshyProperties.SPARK_BATCH_DURATION.getDefaultValue());
	}
}
