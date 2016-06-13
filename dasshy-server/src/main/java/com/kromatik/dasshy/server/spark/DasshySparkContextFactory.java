package com.kromatik.dasshy.server.spark;

import com.kromatik.dasshy.server.config.SparkConfiguration;
import com.netflix.config.DynamicStringProperty;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.Map;

/**
 * Factory for creating spark streaming context
 */
public class DasshySparkContextFactory implements SparkContextFactory
{
	/** spark configuration */
	private final SparkConfiguration			sparkConfiguration;

	/**
	 * Default constructor
	 *
	 * @param configuration spark configuration
	 */
	public DasshySparkContextFactory(final SparkConfiguration configuration)
	{
		sparkConfiguration = configuration;
	}

	@Override
	public SparkSession createSparkSession()
	{
		SparkConf conf = new SparkConf()
						.setMaster(sparkConfiguration.getMaster())
						.setAppName(this.getClass().getName())
						.setJars(JavaStreamingContext.jarOfClass(this.getClass()));

		SparkSession.Builder sessionBuilder = SparkSession.builder().config(conf);

		// add any other spark configuration properties
		for (Map.Entry<String, DynamicStringProperty> entry : sparkConfiguration.getSparkProperties().entrySet())
		{
			sessionBuilder = sessionBuilder.config(entry.getKey(), entry.getValue().getValue());
		}

		return sessionBuilder.getOrCreate();
	}

	@Override
	public JavaStreamingContext createStreamingContext(final SparkSession sparkSession)
	{
		return new JavaStreamingContext(new StreamingContext(sparkSession.sparkContext(),
						Durations.seconds(sparkConfiguration.getBatchDuration())));
	}
}
