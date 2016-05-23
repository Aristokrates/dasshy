package com.kromatik.dasshy.server.streaming;

import com.kromatik.dasshy.sdk.AbstractExtractor;
import com.kromatik.dasshy.sdk.StageConfiguration;
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

	/** input stream */
	private JavaInputDStream<byte[]>	dStream;

	@Override
	public Dataset<Row> next(
					final RuntimeContext context,
					final StageConfiguration configuration,
					final Time time
	)
	{

		if (configuration instanceof KafkaExtractorConfiguration)
		{
			final KafkaExtractorConfiguration kafkaConfig = (KafkaExtractorConfiguration) configuration;

			if (dStream == null)
			{
				dStream = extract(context, kafkaConfig);
			}
			final JavaRDD<byte[]> rdd = dStream.compute(time);
			return context.getSparkSession().createDataset(rdd.rdd(), (Encoder)Encoders.BINARY());
		}
		else
		{
			throw new RuntimeException("The configuration is not related to kafka extractor");
		}
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
	 * @param kafkaConfig kafka extractor configuration
	 * @return input DStream
	 */
	private JavaInputDStream<byte[]> extract(
					final RuntimeContext context,
					final KafkaExtractorConfiguration kafkaConfig)
	{
		final Map<String, String> kafkaMap = buildKafkaMap(kafkaConfig);
		Map<TopicAndPartition, Long> initialOffsets  = getInitialOffsets(kafkaConfig);

		return KafkaUtils.createDirectStream(context.getJavaStreamingContext(), String.class, byte[].class,
						StringDecoder.class, DefaultDecoder.class, byte[].class, kafkaMap,
						initialOffsets,
						new KafkaMessageAndMetadataFunction());
	}

	/**
	 * Build the kafka parameter map
	 *
	 * @param kafkaConfig kafka extractor configuration
	 * @return Kafka parameter map
	 */
	private Map<String, String> buildKafkaMap(final KafkaExtractorConfiguration kafkaConfig)
	{
		final Map<String, String> kafkaMap = new HashMap<>();
		final String broker = kafkaConfig.getHost() + ":" + kafkaConfig.getPort();

		kafkaMap.put("bootstrap.servers", broker);

		// common kafka configuration
		kafkaMap.put("zookeeper.session.timeout.ms", "3000");
		kafkaMap.put("zookeeper.connection.timeout.ms", "3000");

		return kafkaMap;
	}

	/**
	 * Get offsets. Initial implementation manages the offsets in Zookeeper
	 *
	 * @param kafkaConfig kafka extractor configuration
	 * @return offset info
	 */
	protected Map<TopicAndPartition, Long> getInitialOffsets(final KafkaExtractorConfiguration kafkaConfig)
	{
		switch (kafkaConfig.getOffset())
		{
			case 0:
				// auto
				break;

			case 1:
				// smallest
				break;

			case 2:
				// largest
				break;
		}

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
