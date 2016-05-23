package com.kromatik.dasshy.server.component;

import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.sdk.RuntimeContext;
import com.kromatik.dasshy.server.config.DasshyConfiguration;
import com.kromatik.dasshy.server.streaming.DasshyRuntime;
import com.kromatik.dasshy.server.streaming.DasshySparkContextFactory;
import com.kromatik.dasshy.server.streaming.SparkContextFactory;
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
			throw new RuntimeException("Spark engine is not starting", e);
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
				throw new RuntimeException("Spark engine did not stop properly", e);
			}
		}
	}
}
