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
package com.kromatik.dasshy.core.engine.impl;

import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.config.IConfigurationFactory;
import com.kromatik.dasshy.core.engine.IEngine;
import com.kromatik.dasshy.core.engine.IEngineBootstrap;
import com.kromatik.dasshy.core.engine.IEngineContext;
import com.kromatik.dasshy.core.engine.IEngineRuntime;
import com.kromatik.dasshy.core.engine.IEngineRuntimeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Base abstract class for all running engines.
 * Provides standardized ways of configuring the engines, injecting dependencies on bootstrap and running the engine
 *
 * @see IEngine
 *
 * @param <T> engine's configuration
 */
public abstract class AbstractEngine<T extends IEngineConfiguration> implements IEngine<T>
{

	private static final Logger LOGGER	=	LoggerFactory.getLogger(AbstractEngine.class);

	/** engine's runtime environment */
	private IEngineRuntime			engineRuntime;

	/**
	 * Initialize the engine
	 *
	 * @param bootstrap engine's bootstrap
	 */
	protected abstract void init(final IEngineBootstrap<T> bootstrap);

	/**
	 * Called when the engine has been initialized and is ready to be run.
	 * Each engine should provide its own implementation on how to run itself
	 *
	 * @param configuration engine's configuration
	 * @param engineRuntime engine's runtime
	 * @param engineContext engine's context
	 */
	protected abstract void run(final T configuration, final IEngineRuntime engineRuntime,
					final IEngineContext<T> engineContext);

	/**
	 * @see IEngine#start(IEngineContext)
	 */
	@Override
	public void start(final IEngineContext<T> engineContext)
	{
		LOGGER.info("About to start the engine");
		// load the configuration factory from the context
		final IConfigurationFactory<T> configurationFactory = engineContext.getConfigurationFactory();

		LOGGER.info("Initializing the engine configuration");
		// build the configuration
		final T configuration = buildEngineConfiguration(configurationFactory);

		// load the environment factory
		final IEngineRuntimeFactory<T> engineRuntimeFactory = engineContext.getEngineRuntimeFactory();

		LOGGER.info("Initializing the engine runtime environment");
		// build engine runtime environment
		engineRuntime = buildEngineRuntime(configuration, engineRuntimeFactory);

		LOGGER.info("Bootstraping the engine");
		// init the bootstrap and run it
		final EngineBootstrap<T> bootstrap = new EngineBootstrap<>(this, engineContext);
		init(bootstrap);
		bootstrap.run(configuration, engineRuntime);

		LOGGER.info("Running the engine");
		// run the engine itself
		run(configuration, engineRuntime, engineContext);
	}

	/**
	 * @see IEngine#stop()
	 */
	@Override
	public void stop()
	{
		if (engineRuntime != null)
		{
			LOGGER.info("About to stop the engine");
			engineRuntime.shutdown();
		}
	}

	/**
	 * Build the engine's runtime environment
	 *
	 * @param configuration engine configuration
	 * @param engineRuntimeFactory engine runtime environment factory
	 *
	 * @return engine runtime environment
	 */
	protected IEngineRuntime buildEngineRuntime(final T configuration, final IEngineRuntimeFactory<T> engineRuntimeFactory)
	{
		return engineRuntimeFactory.build(configuration);
	}

	/**
	 * Build the configuration using the provided factory
	 *
	 * @param configurationFactory configuration factory
	 *
	 * @return engine configuration
	 */
	protected T buildEngineConfiguration(final IConfigurationFactory<T> configurationFactory)
	{
		final Class<T> klass = getConfigurationClass();
		return configurationFactory.build(klass);
	}

	/**
	 * Get the configuration class using reflection
	 *
	 * @return configuration class
	 */
	private Class<T> getConfigurationClass()
	{
		final Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return (Class<T>) type;
	}
}
