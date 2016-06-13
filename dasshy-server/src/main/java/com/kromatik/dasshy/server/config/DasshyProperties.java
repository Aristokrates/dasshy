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
package com.kromatik.dasshy.server.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * All properties for the dasshy server configuration and their default values
 */
public enum DasshyProperties
{
	SERVER_ADDRESS("dasshy.server.addr", "localhost"),
	SERVER_PORT("dasshy.server.port", "8081"),
	SERVER_MAX_IDLE("dasshy.server.max.idle", "3000"),
	SERVER_CONTEXT_PATH("dasshy.server.context.path", "/"),

	ZOOKEEPER_CONNECTION_STRING("zookeeper.connect", "localhost:2181"),
	ZOOKEEPER_SESSION_TIMEOUT("zookeeper.session.timeout.ms", "3000"),
	ZOOKEEPER_CONNECTION_TIMEOUT("zookeeper.connection.timeout.ms", "3000"),

	SPARK_MASTER("spark.master", "local[10]"),
	SPARK_BATCH_DURATION("spark.batch.duration", "1"),
	SPARK_UI_PORT("spark.ui.port", "4070"),
	SPARK_SCHEDULER_MODE("spark.scheduler.mode", "FAIR"),
	SPARK_CORES_MAX("spark.cores.max", "10"),
	SPARK_SERIALIZER("spark.serializer", "org.apache.spark.serializer.KryoSerializer"),
	SPARK_DRIVER_ALLOW_MULTIPLE_CONTEXTS("spark.driver.allowMultipleContexts", "true"),
	SPARK_SQL_SHUFFLE_PARTITIONS("spark.sql.shuffle.partitions", "5");

	private static final Map<String, DasshyProperties> BY_NAME_MAP = new LinkedHashMap<>();

	static
	{
		for (DasshyProperties dce : DasshyProperties.values())
		{
			BY_NAME_MAP.put(dce.getPropertyName(), dce);
		}
	}

	private final String propertyName;

	private final String propertyDefaultValue;

	DasshyProperties(final String name, final String defaultValue)
	{
		propertyName = name;
		propertyDefaultValue = defaultValue;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public String getDefaultValue()
	{
		return propertyDefaultValue;
	}

	public Integer getDefaultValueAsInt()
	{
		return Integer.valueOf(propertyDefaultValue);
	}

	public Boolean getDefaultValueAsBool()
	{
		return Boolean.valueOf(propertyDefaultValue);
	}

	public Double getDefaultValueAsDouble()
	{
		return Double.valueOf(propertyDefaultValue);
	}

	public static DasshyProperties forName(String propertyName)
	{
		return BY_NAME_MAP.get(propertyName);
	}
}
