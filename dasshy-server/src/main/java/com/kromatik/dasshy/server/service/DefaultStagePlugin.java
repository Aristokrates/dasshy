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
package com.kromatik.dasshy.server.service;

import com.kromatik.dasshy.server.spark.CassandraLoader;
import com.kromatik.dasshy.server.spark.IdentityTransformer;
import com.kromatik.dasshy.server.spark.KafkaExtractor;
import com.kromatik.dasshy.thrift.model.TStageType;

public enum DefaultStagePlugin
{

	// extractors
	KAFKA(TStageType.EXTRACTOR, "kafka", KafkaExtractor.class.getName(), "Kafka Extractor"),

	// transformers
	IDENTITY(TStageType.TRANSFORMER, "identity", IdentityTransformer.class.getName(), "Identity Transformer"),

	// loaders
	CASANDRA(TStageType.LOADER, "cassandra", CassandraLoader.class.getName(), "Cassandra loader");

	/** stage type */
	private final TStageType type;

	/** identifier */
	private final String identifier;

	/** classpath */
	private final String classpath;

	/** description */
	private final String description;

	/**
	 * Default constructor
	 *
	 * @param type        plugin type
	 * @param identifier  plugin identifier
	 * @param classpath   plugin classpath
	 * @param description plugin description
	 */
	DefaultStagePlugin(final TStageType type, final String identifier, final String classpath, final String description)
	{
		this.type = type;
		this.identifier = identifier;
		this.classpath = classpath;
		this.description = description;
	}

	public TStageType getType()
	{
		return type;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public String getClasspath()
	{
		return classpath;
	}

	public String getDescription()
	{
		return description;
	}
}
