package com.kromatik.dasshy.server.streaming;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Creates spark context
 */
public interface SparkContextFactory
{
	/***
	 * Creates spark session
	 *
	 * @return spark session
	 */
	SparkSession			createSparkSession();

	/**
	 * Creates streaming context for the given spark session
	 *
	 * @param sparkSession spark session
	 *
	 * @return streaming context
	 */
	JavaStreamingContext	createStreamingContext(final SparkSession sparkSession);
}
