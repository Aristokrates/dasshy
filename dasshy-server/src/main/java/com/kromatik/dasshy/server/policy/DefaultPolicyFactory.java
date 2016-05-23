package com.kromatik.dasshy.server.policy;
;
import com.kromatik.dasshy.sdk.Extractor;
import com.kromatik.dasshy.sdk.Loader;
import com.kromatik.dasshy.sdk.Transformer;
import com.kromatik.dasshy.server.streaming.BatchClock;
import com.kromatik.dasshy.server.streaming.CassandraLoader;
import com.kromatik.dasshy.server.streaming.CustomConfiguration;
import com.kromatik.dasshy.server.streaming.DefaultBatchClock;
import com.kromatik.dasshy.server.streaming.IdentityTransformer;
import com.kromatik.dasshy.server.streaming.KafkaExtractor;
import com.kromatik.dasshy.server.streaming.KafkaExtractorConfiguration;
import com.kromatik.dasshy.thrift.model.TCassandraLoaderConfiguration;
import com.kromatik.dasshy.thrift.model.TCustomConfiguration;
import com.kromatik.dasshy.thrift.model.TExtractor;
import com.kromatik.dasshy.thrift.model.TExtractorConfiguration;
import com.kromatik.dasshy.thrift.model.TExtractorType;
import com.kromatik.dasshy.thrift.model.TKafkaExtractorConfiguration;
import com.kromatik.dasshy.thrift.model.TLoader;
import com.kromatik.dasshy.thrift.model.TLoaderConfiguration;
import com.kromatik.dasshy.thrift.model.TLoaderType;
import com.kromatik.dasshy.thrift.model.TPolicy;
import com.kromatik.dasshy.thrift.model.TTransformer;
import com.kromatik.dasshy.thrift.model.TTransformerConfiguration;
import com.kromatik.dasshy.thrift.model.TTransformerType;

import java.util.Map;

/**
 * Default factory for building policy instance out of the model
 */
public class DefaultPolicyFactory implements PolicyFactory
{

	@Override
	public Policy buildPolicy(final TPolicy policyModel)
	{
		final Policy policy = new Policy();

		policy.setModel(policyModel);
		policy.setExtractor(buildExtractor(policyModel.getExtractor()));
		policy.setTransformer(buildTransformer(policyModel.getTransformer()));
		policy.setLoader(buildLoader(policyModel.getLoader()));

		policy.setClock(buildBatchClock(policyModel));
		return policy;
	}

	@Override
	public BatchClock buildBatchClock(final TPolicy policyModel)
	{
		return new DefaultBatchClock(policyModel.getInterval());
	}

	@Override
	public ExtractorHolder buildExtractor(final TExtractor extractorModel)
	{
		final TExtractorType type = extractorModel.getType();
		final TExtractorConfiguration configuration = extractorModel.getConfiguration();

		switch (type)
		{
			case KAFKA:

				final TKafkaExtractorConfiguration tKafkaExtractorConfig = configuration.getKafka();
				if (tKafkaExtractorConfig == null)
				{
					throw new RuntimeException("Kafka extractor configuration is not present");
				}

				final KafkaExtractorConfiguration kafkaExtractorConfig = new KafkaExtractorConfiguration();
				kafkaExtractorConfig.setHost(tKafkaExtractorConfig.getHost());
				kafkaExtractorConfig.setPort(tKafkaExtractorConfig.getPort());
				kafkaExtractorConfig.setTopic(tKafkaExtractorConfig.getTopic());
				kafkaExtractorConfig.setOffset(tKafkaExtractorConfig.getOffset().getValue());

				return new ExtractorHolder(new KafkaExtractor(), kafkaExtractorConfig);

			case CUSTOM:

				TCustomConfiguration tCustomConfiguration = configuration.getCustom();
				if (tCustomConfiguration == null)
				{
					throw new RuntimeException("Custom extractor configuration is not present");
				}

				final String className = tCustomConfiguration.getClassName();
				final Map<String, String> values = tCustomConfiguration.getValue();

				return new ExtractorHolder((Extractor)instanceOf(className), new CustomConfiguration(values));
		}

		return null;
	}

	@Override
	public TransformerHolder buildTransformer(final TTransformer transformerModel)
	{
		final TTransformerType type = transformerModel.getType();
		final TTransformerConfiguration configuration = transformerModel.getConfiguration();

		switch (type)
		{
			case IDENTITY:

				return new TransformerHolder(new IdentityTransformer(), null);

			case CUSTOM:

				TCustomConfiguration tCustomConfiguration = configuration.getCustom();
				if (tCustomConfiguration == null)
				{
					throw new RuntimeException("Custom transformer configuration is not present");
				}

				final String className = tCustomConfiguration.getClassName();
				final Map<String, String> values = tCustomConfiguration.getValue();

				return new TransformerHolder((Transformer)instanceOf(className), new CustomConfiguration(values));
		}

		return null;

	}

	@Override
	public LoaderHolder buildLoader(final TLoader loaderModel)
	{
		final TLoaderType type = loaderModel.getType();
		final TLoaderConfiguration configuration = loaderModel.getConfiguration();

		switch (type)
		{
			case CASSANDRA:

				final TCassandraLoaderConfiguration tCassandraLoaderConfiguration = configuration.getCassandra();
				if (tCassandraLoaderConfiguration == null)
				{
					throw new RuntimeException("Cassandra loader configuration is not present");
				}

				return new LoaderHolder(new CassandraLoader(), null);

			case CUSTOM:

				TCustomConfiguration tCustomConfiguration = configuration.getCustom();
				if (tCustomConfiguration == null)
				{
					throw new RuntimeException("Custom transformer configuration is not present");
				}

				final String className = tCustomConfiguration.getClassName();
				final Map<String, String> values = tCustomConfiguration.getValue();

				return new LoaderHolder((Loader)instanceOf(className), new CustomConfiguration(values));
		}

		return null;
	}

	/**
	 * Creates an instance of a given class
	 *
	 * @param className classname
	 * @param <M>  class for the instance
	 *
	 * @return instance
	 */
	private <M> M instanceOf(final String className)
	{
		try
		{
			return (M) Class.forName(className).newInstance();
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			throw new IllegalStateException("Unable to create class " + className, e);
		}
	}
}
