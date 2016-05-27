package com.kromatik.dasshy.core.config.impl;

import com.kromatik.dasshy.core.config.IConfigurationFactory;
import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.exception.EngineConfigurationException;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConcurrentMapConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * Simple implementation of {@link IConfigurationFactory} that uses
 * <a href="https://github.com/Netflix/archaius/wiki">Netflix Archaius</a>
 * for building the configuration
 *
 * @param <T> engine configuration
 */
public class ArchaiusConfigurationFactory<T extends IEngineConfiguration> implements IConfigurationFactory<T>
{
    /**
     * @see IConfigurationFactory
     */
    @Override
    public T build(final Class<T> klass)
    {

        final T configuration = newInstance(klass);

        // load the configuration from system property
        final SystemConfiguration systemConfiguration = new SystemConfiguration();
        final ConcurrentMapConfiguration configFromSystemProperties = new ConcurrentMapConfiguration(
                        systemConfiguration);

        // create a hierarchy of configurations
        final ConcurrentCompositeConfiguration finalConfig = new ConcurrentCompositeConfiguration();
        finalConfig.addConfiguration(configFromSystemProperties, "systemConfig");

        // install the configuration
        try
        {
            ConfigurationManager.install(finalConfig);
        }
        catch (final IllegalStateException exception)
        {
            throw new EngineConfigurationException("Configuration installation has failed", exception);
        }

        // load the configuration
        configuration.loadConfiguration(DynamicPropertyFactory.getInstance());
        return configuration;
    }

    /**
     * Creates an instance of the given configuration class
     *
     * @param klass
     * @return configuration object
     */
    private T newInstance(final Class<T> klass)
    {

        T configuration = null;
        try
        {

            configuration = klass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new EngineConfigurationException("Error instantiating configuration class: " + klass, e);
        }

        return configuration;
    }
}
