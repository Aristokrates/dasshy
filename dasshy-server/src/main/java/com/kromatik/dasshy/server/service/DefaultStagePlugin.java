package com.kromatik.dasshy.server.service;

import com.kromatik.dasshy.server.streaming.CassandraLoader;
import com.kromatik.dasshy.server.streaming.IdentityTransformer;
import com.kromatik.dasshy.server.streaming.KafkaExtractor;
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
	private final TStageType	type;

	/** identifier */
	private final String		identifier;

	/** classpath */
	private final String		classpath;

	/** description */
	private final String		description;

	/**
	 * Default constructor
	 *
	 * @param type	plugin type
	 * @param identifier plugin identifier
	 * @param classpath	plugin classpath
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
