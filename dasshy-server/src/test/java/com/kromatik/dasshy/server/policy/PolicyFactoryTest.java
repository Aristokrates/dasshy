package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.server.service.DefaultStagePlugin;
import com.kromatik.dasshy.server.service.StagePluginService;
import com.kromatik.dasshy.server.spark.BatchClock;
import com.kromatik.dasshy.server.spark.CassandraLoader;
import com.kromatik.dasshy.server.spark.StreamingIntervalBatchClock;
import com.kromatik.dasshy.server.spark.IdentityTransformer;
import com.kromatik.dasshy.server.spark.KafkaExtractor;
import com.kromatik.dasshy.thrift.model.TPolicy;
import com.kromatik.dasshy.thrift.model.TStage;
import com.kromatik.dasshy.thrift.model.TStagePlugin;
import com.kromatik.dasshy.thrift.model.TStageType;
import org.fest.assertions.api.Assertions;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for policy factory
 */
@Test(groups = {"policy"})
public class PolicyFactoryTest
{

	private StagePluginService	pluginService;

	private PolicyFactory		policyFactory;

	@BeforeMethod
	public void setup() throws Exception
	{
		pluginService = Mockito.mock(StagePluginService.class);
		policyFactory = new DefaultPolicyFactory(pluginService);
	}

	public void buildBatchClock() throws Exception
	{

		Long interval = 1L;

		TPolicy policyModel = new TPolicy();
		policyModel.setInterval(interval);

		BatchClock batchClock = policyFactory.buildStreamingClock(policyModel);
		Assertions.assertThat(batchClock).isNotNull();

		Assertions.assertThat(batchClock.acquire()).isTrue();
		Long currentTime = System.currentTimeMillis();

		// check the batch time
		Long previousBatchTime = batchClock.getBatchTime();
		Assertions.assertThat(previousBatchTime).isLessThan(currentTime);

		// increment the batch and sleep
		batchClock.increment(1);

		// check that the batch time has been incremented
		Long nextBatchTime = batchClock.getBatchTime();
		Assertions.assertThat(nextBatchTime).isGreaterThan(currentTime);

		// check if time difference between the batch times is exactly the batch interval
		Assertions.assertThat(nextBatchTime-previousBatchTime).isEqualTo(interval*1000);
	}

	public void buildExtractor() throws Exception
	{
		DefaultStagePlugin kafkaPluginEnum = DefaultStagePlugin.KAFKA;
		TStagePlugin kafkaPlugin = new TStagePlugin(
						kafkaPluginEnum.getType(), kafkaPluginEnum.getIdentifier(),
						kafkaPluginEnum.getClasspath(), kafkaPluginEnum.getDescription());

		Mockito.when(pluginService.getStagePluginByTypeAndId(TStageType.EXTRACTOR, kafkaPluginEnum.getIdentifier()))
						.thenReturn(kafkaPlugin);

		TStage extractorStage = new TStage(kafkaPluginEnum.getIdentifier());

		Map<String, String> kafkaConfig = new HashMap<>();
		kafkaConfig.put(KafkaExtractor.HOST, "localhost");
		kafkaConfig.put(KafkaExtractor.PORT, "9092");
		kafkaConfig.put(KafkaExtractor.TOPIC, "testTopic");

		extractorStage.setConfiguration(kafkaConfig);

		ExtractorHolder extractorHolder = policyFactory.buildExtractor(extractorStage);

		Assertions.assertThat(extractorHolder).isNotNull();

		Assertions.assertThat(extractorHolder.stage()).isExactlyInstanceOf(KafkaExtractor.class);
		Assertions.assertThat(extractorHolder.configuration().getValues()).isNotEmpty().isEqualTo(kafkaConfig);
	}

	public void buildTransformer() throws Exception
	{
		DefaultStagePlugin identityPluginEnum = DefaultStagePlugin.IDENTITY;
		TStagePlugin identityPlugin = new TStagePlugin(
						identityPluginEnum.getType(), identityPluginEnum.getIdentifier(),
						identityPluginEnum.getClasspath(), identityPluginEnum.getDescription());

		Mockito.when(pluginService.getStagePluginByTypeAndId(TStageType.TRANSFORMER, identityPluginEnum.getIdentifier()))
						.thenReturn(identityPlugin);

		TStage transformerStage = new TStage(identityPlugin.getIdentifier());
		TransformerHolder transformerHolder = policyFactory.buildTransformer(transformerStage);

		Assertions.assertThat(transformerHolder).isNotNull();

		Assertions.assertThat(transformerHolder.stage()).isExactlyInstanceOf(IdentityTransformer.class);
		Assertions.assertThat(transformerHolder.configuration().getValues()).isNullOrEmpty();
	}

	public void buildLoader() throws Exception
	{
		DefaultStagePlugin cassandraPluginEnum = DefaultStagePlugin.CASANDRA;
		TStagePlugin cassandraPlugin = new TStagePlugin(
						cassandraPluginEnum.getType(), cassandraPluginEnum.getIdentifier(),
						cassandraPluginEnum.getClasspath(), cassandraPluginEnum.getDescription());

		Mockito.when(pluginService.getStagePluginByTypeAndId(TStageType.LOADER, cassandraPluginEnum.getIdentifier()))
						.thenReturn(cassandraPlugin);

		TStage loaderStage = new TStage(cassandraPlugin.getIdentifier());
		LoaderHolder loaderHolder = policyFactory.buildLoader(loaderStage);

		Assertions.assertThat(loaderHolder).isNotNull();

		// TODO (pai) change this once Cassandra loader is implemented
		Assertions.assertThat(loaderHolder.stage()).isExactlyInstanceOf(CassandraLoader.class);
		Assertions.assertThat(loaderHolder.configuration().getValues()).isNullOrEmpty();
	}

	public void buildPolicy() throws Exception
	{

		DefaultStagePlugin kafkaPluginEnum = DefaultStagePlugin.KAFKA;
		Mockito.when(pluginService.getStagePluginByTypeAndId(TStageType.EXTRACTOR, kafkaPluginEnum.getIdentifier()))
						.thenReturn(new TStagePlugin(
										kafkaPluginEnum.getType(), kafkaPluginEnum.getIdentifier(),
										kafkaPluginEnum.getClasspath(), kafkaPluginEnum.getDescription()));

		DefaultStagePlugin identityPluginEnum = DefaultStagePlugin.IDENTITY;
		Mockito.when(pluginService.getStagePluginByTypeAndId(TStageType.TRANSFORMER, identityPluginEnum.getIdentifier()))
						.thenReturn(new TStagePlugin(
										identityPluginEnum.getType(), identityPluginEnum.getIdentifier(),
										identityPluginEnum.getClasspath(), identityPluginEnum.getDescription()));

		DefaultStagePlugin cassandraPluginEnum = DefaultStagePlugin.CASANDRA;
		Mockito.when(pluginService.getStagePluginByTypeAndId(TStageType.LOADER, cassandraPluginEnum.getIdentifier()))
						.thenReturn(new TStagePlugin(
										cassandraPluginEnum.getType(), cassandraPluginEnum.getIdentifier(),
										cassandraPluginEnum.getClasspath(), cassandraPluginEnum.getDescription()));

		// build the kafka extractor stage with configuration
		TStage kafkaStage = new TStage(kafkaPluginEnum.getIdentifier());
		Map<String, String> kafkaConfig = new HashMap<>();
		kafkaConfig.put(KafkaExtractor.HOST, "localhost");
		kafkaConfig.put(KafkaExtractor.PORT, "9092");
		kafkaConfig.put(KafkaExtractor.TOPIC, "testTopic");
		kafkaStage.setConfiguration(kafkaConfig);

		// build transformer stage
		TStage transformerStage = new TStage(identityPluginEnum.getIdentifier());

		// build loader stage
		TStage loaderStage = new TStage(cassandraPluginEnum.getIdentifier());

		// finally build the model
		String policyId = "id";
		TPolicy policyModel = new TPolicy();
		policyModel.setId(policyId);
		policyModel.setInterval(1L);
		policyModel.setExtractor(kafkaStage);
		policyModel.setTransformer(transformerStage);
		policyModel.setLoader(loaderStage);

		// build the policy instance out of the policy model
		Policy policyInstance = policyFactory.buildPolicy(policyModel);
		Assertions.assertThat(policyInstance).isNotNull();

		Assertions.assertThat(policyInstance.getModel()).isEqualsToByComparingFields(policyModel);
		Assertions.assertThat(policyInstance.getClock()).isInstanceOf(StreamingIntervalBatchClock.class);

		Assertions.assertThat(policyInstance.getExtractor().stage()).isExactlyInstanceOf(KafkaExtractor.class);
		Assertions.assertThat(policyInstance.getExtractor().configuration().getValues()).isNotEmpty().isEqualTo(kafkaConfig);

		Assertions.assertThat(policyInstance.getTransformer().stage()).isExactlyInstanceOf(IdentityTransformer.class);
		Assertions.assertThat(policyInstance.getTransformer().configuration().getValues()).isNullOrEmpty();

		Assertions.assertThat(policyInstance.getLoader().stage()).isExactlyInstanceOf(CassandraLoader.class);
		Assertions.assertThat(policyInstance.getLoader().configuration().getValues()).isNullOrEmpty();
	}
}
