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
package com.kromatik.dasshy.server;

import com.kromatik.dasshy.core.config.IConfigurationFactory;
import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.engine.IEngineContext;
import com.kromatik.dasshy.core.engine.IEngineRuntime;
import com.kromatik.dasshy.core.engine.IEngineRuntimeFactory;
import com.kromatik.dasshy.server.command.CreatePolicyCommand;
import com.kromatik.dasshy.server.command.GetPolicyCommand;
import com.kromatik.dasshy.server.config.DasshyConfiguration;
import com.kromatik.dasshy.server.service.DefaultStagePlugin;
import com.kromatik.dasshy.server.spark.DasshyRuntime;
import com.kromatik.dasshy.server.spark.KafkaExtractor;
import com.kromatik.dasshy.server.thrift.SimpleJsonPayLoadProvider;
import com.kromatik.dasshy.server.thrift.ThriftJsonPayLoadProvider;
import com.kromatik.dasshy.server.thrift.ThriftPayLoadProvider;
import com.kromatik.dasshy.thrift.model.TBatchClock;
import com.kromatik.dasshy.thrift.model.TPolicy;
import com.kromatik.dasshy.thrift.model.TStage;
import com.kromatik.dasshy.thrift.model.TStreamingBatchClock;
import com.netflix.config.DynamicPropertyFactory;
import org.fest.assertions.api.Assertions;
import org.glassfish.jersey.filter.LoggingFilter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Integration tests for policy
 */
@Test(groups = {"IT"})
public class PolicyIT
{
	protected static EmbeddedZooKeeper testingServer;

	protected DasshyServer	server;

	protected WebTarget		dasshyServerApi;

	@BeforeClass
	public void setup() throws Exception
	{
		testingServer = new EmbeddedZooKeeper();
		testingServer.start();

		server = new DasshyServer();
		server.start(buildITDasshyContext());

		final Client client = ClientBuilder.newClient();
		client.register(SimpleJsonPayLoadProvider.class);
		client.register(ThriftJsonPayLoadProvider.class);
		client.register(ThriftPayLoadProvider.class);

		client.register(new LoggingFilter());

		dasshyServerApi = client.target(UriBuilder.fromUri("http://{host}:{port}/{path}")
										.resolveTemplate("host", InetAddress.getLocalHost().getHostName())
										.resolveTemplate("port", "8081").resolveTemplate("path", "api"));
	}

	public void createPolicy() throws Exception
	{

		String policyId = "id";
		TPolicy policyModel = new TPolicy();
		policyModel.setId(policyId);
		policyModel.setClock(new TBatchClock(TBatchClock._Fields.STREAMING, new TStreamingBatchClock(15L)));

		DefaultStagePlugin kafkaPluginEnum = DefaultStagePlugin.KAFKA;
		DefaultStagePlugin identityPluginEnum = DefaultStagePlugin.IDENTITY;
		DefaultStagePlugin cassandraPluginEnum = DefaultStagePlugin.CASANDRA;

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

		policyModel.setExtractor(kafkaStage);
		policyModel.setTransformer(transformerStage);
		policyModel.setLoader(loaderStage);

		TPolicy createdPolicy = new CreatePolicyCommand(dasshyServerApi, policyModel).run();
		Assertions.assertThat(createdPolicy.getId()).isEqualTo(policyId);

		// wait some time and check if the job has been updated
		Thread.sleep(3000);

		TPolicy existingPolicy = new GetPolicyCommand(dasshyServerApi, policyId).run();

		// TODO (pai) check the correct status once the kafka extractor has been implemented
		Assertions.assertThat(existingPolicy.getState()).isNotNull();
	}

	@AfterClass
	public void tearDown() throws Exception
	{
		server.stop();
		testingServer.close();
	}


	private IEngineContext<DasshyConfiguration> buildITDasshyContext()
	{
		return new IEngineContext<DasshyConfiguration>()
		{
			@Override
			public IConfigurationFactory<DasshyConfiguration> getConfigurationFactory()
			{
				return new ITDasshyConfiguration();
			}

			@Override
			public IEngineRuntimeFactory<DasshyConfiguration> getEngineRuntimeFactory()
			{
				return new IEngineRuntimeFactory<DasshyConfiguration>()
				{
					@Override
					public IEngineRuntime build(DasshyConfiguration config)
					{
						return new DasshyRuntime();
					}
				};
			}
		};
	}


	private static final class ITDasshyConfiguration implements IConfigurationFactory
	{
		@Override
		public IEngineConfiguration build(Class klass)
		{
			final DasshyConfiguration config = new DasshyConfiguration();
			// load defaults
			config.loadConfiguration(DynamicPropertyFactory.getInstance());

			config.getZookeeperClientConfiguration().setConnectionString(testingServer.getConnectString());

			return config;
		}
	}
}
