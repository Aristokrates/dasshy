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

	private static final Logger LOGGER	=	LoggerFactory.getLogger(AbstractEngineCompositeConfiguration.class);

	/** engine configurations */
	private final List<IEngineConfiguration> 		engineConfigurations	=	new ArrayList<>();

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
