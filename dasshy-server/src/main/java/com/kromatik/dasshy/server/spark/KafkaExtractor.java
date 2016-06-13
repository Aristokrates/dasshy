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
package com.kromatik.dasshy.server.spark;

import com.kromatik.dasshy.sdk.AbstractExtractor;
import com.kromatik.dasshy.sdk.StageAttribute;
import com.kromatik.dasshy.sdk.RuntimeContext;
import kafka.common.TopicAndPartition;
import kafka.message.MessageAndMetadata;
import kafka.serializer.DefaultDecoder;
import kafka.serializer.StringDecoder;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Extractor that reads from Kafka
 */
public class KafkaExtractor extends AbstractExtractor
{

	public static final String			HOST 		=	"host";
	public static final String			PORT 		=	"port";
	public static final String			TOPIC 		=	"topic";
	public static final String			OFFSET 		=	"offset";

	/** input stream */
	private JavaInputDStream<byte[]>	dStream;

	/**
	 * Default constructor
	 */
	public KafkaExtractor()
	{
		// set the attribute definitions for this extractor
		setAttributeDefinitions(
						new StageAttribute(HOST, StageAttribute.Type.STRING, true),
						new StageAttribute(PORT, StageAttribute.Type.INTEGER, true),
						new StageAttribute(TOPIC, StageAttribute.Type.STRING, true),
						new StageAttribute(OFFSET, StageAttribute.Type.STRING, false)
		);
	}

	@Override
	public Map<String, Dataset<Row>> extract(final RuntimeContext context, final Time time)
	{
		Map<String, Dataset<Row>> extractMap = new HashMap<>();
		if (dStream == null)
		{
			dStream = extract(context);
		}
		final JavaRDD<byte[]> rdd = dStream.compute(time);
		extractMap.put("kafka", context.getSparkSession().createDataset(rdd.rdd(), (Encoder)Encoders.BINARY()));

		return extractMap;
	}


	@Override
	public void commit()
	{
		// commit the offset
	}

	@Override
	public void rollback()
	{
		// set the previous offsets
	}

	/**
	 * Extract the data as an input stream of bytes
	 *
	 * @param context runtime context
	 * @return input DStream
	 */
	private JavaInputDStream<byte[]> extract(final RuntimeContext context)
	{
		final Map<String, String> kafkaMap = buildKafkaMap();
		Map<TopicAndPartition, Long> initialOffsets  = getInitialOffsets();

		return KafkaUtils.createDirectStream(context.getJavaStreamingContext(), String.class, byte[].class,
						StringDecoder.class, DefaultDecoder.class, byte[].class, kafkaMap,
						initialOffsets,
						new KafkaMessageAndMetadataFunction());
	}

	/**
	 * Build the kafka parameter map
	 *
	 * @return Kafka parameter map
	 */
	private Map<String, String> buildKafkaMap()
	{
		final Map<String, String> kafkaMap = new HashMap<>();
		final String broker = getAttribute(HOST, "localhost") + ":" + getAttribute(PORT, 9092);

		kafkaMap.put("bootstrap.servers", broker);

		// common kafka configuration
		kafkaMap.put("zookeeper.session.timeout.ms", "3000");
		kafkaMap.put("zookeeper.connection.timeout.ms", "3000");

		return kafkaMap;
	}

	/**
	 * Get offsets. Initial implementation manages the offsets in Zookeeper
	 *
	 * @return offset info
	 */
	protected Map<TopicAndPartition, Long> getInitialOffsets()
	{
		// TODO (pai)
		return new HashMap<>();
	}

	/**
	 * Inner kafka function that returns the message payload
	 */
	private static class KafkaMessageAndMetadataFunction implements Function<MessageAndMetadata<String, byte[]>, byte[]>
	{
		@Override
		public byte[] call(final MessageAndMetadata<String, byte[]> payload) throws Exception
		{
			return payload.message();
		}
	}
}
