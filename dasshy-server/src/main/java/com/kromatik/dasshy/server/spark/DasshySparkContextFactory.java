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
