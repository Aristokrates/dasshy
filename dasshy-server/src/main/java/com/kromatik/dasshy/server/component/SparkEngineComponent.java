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
package com.kromatik.dasshy.server.component;

import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.core.exception.EngineStartupException;
import com.kromatik.dasshy.sdk.RuntimeContext;
import com.kromatik.dasshy.server.config.DasshyConfiguration;
import com.kromatik.dasshy.server.spark.DasshyRuntime;
import com.kromatik.dasshy.server.spark.DasshySparkContextFactory;
import com.kromatik.dasshy.server.spark.SparkContextFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Engine component that runs Spark streams
 */
public class SparkEngineComponent implements IEngineComponent
{

	/** dasshy configuration */
	private final DasshyConfiguration		configuration;

	/** dasshy runtime */
	private final DasshyRuntime				runtime;

	/**
	 * Default constructor
	 *
	 * @param dasshyConfiguration dasshy configuration
	 * @param engineRuntime server runtime
	 */
	public SparkEngineComponent(final DasshyConfiguration dasshyConfiguration, final DasshyRuntime engineRuntime)
	{
		configuration = dasshyConfiguration;
		runtime = engineRuntime;
	}

	/**
	 * @see IEngineComponent#start()
	 */
	@Override
	public void start()
	{
		try
		{

			SparkContextFactory factory = new DasshySparkContextFactory(
							configuration.getSparkConfiguration());

			final SparkSession sparkSession = factory.createSparkSession();
			final JavaStreamingContext streamingContext = factory.createStreamingContext(sparkSession);

			final RuntimeContext runtimeContext = new RuntimeContext(streamingContext, sparkSession);
			runtime.setRuntimeContext(runtimeContext);

		}
		catch (final Exception e)
		{
			throw new EngineStartupException("Spark engine is not starting", e);
		}
	}

	/**
	 * @see IEngineComponent#stop()
	 */
	@Override
	public void stop()
	{
		final RuntimeContext runtimeContext = runtime.getRuntimeContext();
		if (runtimeContext != null)
		{
			final JavaStreamingContext javaStreamingContext = runtimeContext.getJavaStreamingContext();
			try
			{
				// shutdown the spark streaming context gracefully
				javaStreamingContext.ssc().scheduler().stop(true);
				javaStreamingContext.stop(true, true);
			}
			catch (final Exception e)
			{
				throw new EngineStartupException("Spark engine did not stop properly", e);
			}
		}
	}
}
