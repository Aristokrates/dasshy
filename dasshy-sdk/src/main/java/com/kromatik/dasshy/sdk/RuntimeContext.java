package com.kromatik.dasshy.sdk;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Runtime context of the spark engine component
 */
public class RuntimeContext
{
	/** streaming context */
	private final JavaStreamingContext				javaStreamingContext;

	/** spark session */
	private final SparkSession						sparkSession;

	/**
	 * Default constructor
	 *
	 * @param javaStreamingContext spark streaming context
	 * @param sparkSession spark session
	 */
	public RuntimeContext(final JavaStreamingContext javaStreamingContext, final SparkSession sparkSession)
	{
		this.javaStreamingContext = javaStreamingContext;
		this.sparkSession = sparkSession;
	}

	/**
	 * @return spark streaming context
	 */
	public JavaStreamingContext getJavaStreamingContext()
	{
		return javaStreamingContext;
	}

	/**
	 * @return spark session
	 */
	public SparkSession getSparkSession()
	{
		return sparkSession;
	}
}
