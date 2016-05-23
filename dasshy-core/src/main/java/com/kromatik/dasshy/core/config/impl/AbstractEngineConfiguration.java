package com.kromatik.dasshy.core.config.impl;

import com.kromatik.dasshy.core.config.IEngineCompositeConfiguration;
import com.kromatik.dasshy.core.config.IEngineConfiguration;

/**
 * Basic engine configuration
 */
public abstract class AbstractEngineConfiguration implements IEngineConfiguration
{
	/**
	 * Constructor that accepts composite configuration for self-registration
	 *
	 * @param compositeConfiguration composite configuration
	 */
	protected AbstractEngineConfiguration(final IEngineCompositeConfiguration compositeConfiguration)
	{
		compositeConfiguration.addConfiguration(this);
	}
}
