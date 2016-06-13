package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.sdk.AttributeUtils;
import com.kromatik.dasshy.sdk.Extractor;
import com.kromatik.dasshy.sdk.Loader;
import com.kromatik.dasshy.sdk.StageConfiguration;
import com.kromatik.dasshy.sdk.Transformer;
import com.kromatik.dasshy.server.service.StagePluginService;
import com.kromatik.dasshy.server.spark.BatchClock;
import com.kromatik.dasshy.server.spark.ExecuteNTimesBackoffBatchClock;
import com.kromatik.dasshy.server.spark.ExecuteNTimesBatchClock;
import com.kromatik.dasshy.server.spark.StreamingIntervalBatchClock;
import com.kromatik.dasshy.thrift.model.TBatchClock;
import com.kromatik.dasshy.thrift.model.TBatchClockN;
import com.kromatik.dasshy.thrift.model.TBatchClockNBackoff;
import com.kromatik.dasshy.thrift.model.TPolicy;
import com.kromatik.dasshy.thrift.model.TStage;
import com.kromatik.dasshy.thrift.model.TStagePlugin;
import com.kromatik.dasshy.thrift.model.TStageType;

import java.util.Map;

import static com.kromatik.dasshy.thrift.model.TBatchClock._Fields.*;

/**
 * Default factory for building policy instance out of the model
 */
public class DefaultPolicyFactory implements PolicyFactory
{
	/** stage plugin service */
	private final StagePluginService		stagePluginService;

	/**
	 * Default constructor
	 *
	 * @param service plugin service
	 */
	public DefaultPolicyFactory(final StagePluginService service)
	{
		this.stagePluginService = service;
	}

	@Override
	public Policy buildPolicy(final TPolicy policyModel)
	{
		final Policy policy = new Policy();

		policy.setModel(policyModel);
		policy.setExtractor(buildExtractor(policyModel.getExtractor()));
		policy.setTransformer(buildTransformer(policyModel.getTransformer()));
		policy.setLoader(buildLoader(policyModel.getLoader()));

		policy.setClock(buildStreamingClock(policyModel));
		return policy;
	}

	@Override
	public BatchClock buildStreamingClock(final TPolicy policyModel)
	{
		TBatchClock clock = policyModel.getClock();

		switch (clock.getSetField())
		{
			case STREAMING:
				Long interval = clock.getStreaming().getInterval();
				return new StreamingIntervalBatchClock(interval);

			case N_TIMES:
				TBatchClockN nTimes = clock.getNTimes();
				return new ExecuteNTimesBatchClock(nTimes.getMaxBatches());

			case N_TIMES_BACKOFF:
				TBatchClockNBackoff nTimesBackoff = clock.getNTimesBackoff();
				return new ExecuteNTimesBackoffBatchClock(nTimesBackoff.getMaxBatches(), nTimesBackoff.getSleepMs());

			default:
				return null;
		}
	}

	@Override
	public ExtractorHolder buildExtractor(final TStage extractorModel)
	{
		final String extractorId = extractorModel.getIdentifier();
		final Map<String, String> extractorConfig = extractorModel.getConfiguration();

		// get the plugin from the plugin registry
		final TStagePlugin extractorPlugin = getPlugin(TStageType.EXTRACTOR, extractorId);
		final Extractor extractor = instanceOf(extractorPlugin.getClasspath());

		// validate the configuration against the attribute definitions of the extractor
		AttributeUtils.validateConfiguration(extractor, extractorConfig);

		return new ExtractorHolder(extractor, new StageConfiguration(extractorConfig));
	}


	@Override
	public TransformerHolder buildTransformer(final TStage transformerModel)
	{
		final String transformerId = transformerModel.getIdentifier();
		final Map<String, String> transformerConfig = transformerModel.getConfiguration();

		// get the plugin from the plugin registry
		final TStagePlugin transformerPlugin = getPlugin(TStageType.TRANSFORMER, transformerId);
		final Transformer transformer = instanceOf(transformerPlugin.getClasspath());

		// validate the configuration against the attribute definitions of the transformer
		AttributeUtils.validateConfiguration(transformer, transformerConfig);

		return new TransformerHolder(transformer, new StageConfiguration(transformerConfig));
	}

	@Override
	public LoaderHolder buildLoader(final TStage loaderModel)
	{
		final String loaderId = loaderModel.getIdentifier();
		final Map<String, String> loaderConfig = loaderModel.getConfiguration();

		// get the plugin from the plugin registry
		final TStagePlugin loaderPlugin = getPlugin(TStageType.LOADER, loaderId);
		final Loader loader = instanceOf(loaderPlugin.getClasspath());

		// validate the configuration against the attribute definitions of the transformer
		AttributeUtils.validateConfiguration(loader, loaderConfig);

		return new LoaderHolder(loader, new StageConfiguration(loaderConfig));
	}

	/**
	 * Get plugin by type and id
	 *
	 * @param type stage type
	 * @param identifier stage identifier
	 *
	 * @return a stage plugin
	 */
	private TStagePlugin getPlugin(final TStageType type, final String identifier)
	{
		// we need to get the plugin from the plugin service
		return stagePluginService.getStagePluginByTypeAndId(type, identifier);
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
