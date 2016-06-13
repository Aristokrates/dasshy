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
import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.core.engine.IEngineContext;
import com.kromatik.dasshy.core.engine.IEngineRuntime;
import com.kromatik.dasshy.core.engine.IEngineRuntimeFactory;
import com.kromatik.dasshy.core.exception.EngineStartupException;
import com.kromatik.dasshy.server.config.DasshyConfiguration;
import com.kromatik.dasshy.server.spark.DasshyRuntime;
import com.netflix.config.DynamicPropertyFactory;
import org.fest.assertions.api.Assertions;
import org.mockito.Mockito;
import org.testng.annotations.Test;

@Test(groups = {"server"})
public class DasshyServerTest
{

	public void serverStartMocked() throws Exception
	{
		final DasshyServer server = Mockito.mock(DasshyServer.class);
		Mockito.doNothing().when(server).start(Mockito.any(IEngineContext.class));

		DasshyServer.setInstance(server);
		server.main(new String[]{});

		DasshyServer.getInstance().stop();
	}

	public void serverStartThrowsEngineException() throws Exception
	{
		final DasshyServer engine = Mockito.mock(DasshyServer.class);
		Mockito.doThrow(new EngineStartupException("Engine start failed", new Exception()))
						.when(engine).start(Mockito.any(IEngineContext.class));;

		DasshyServer.setInstance(engine);
		engine.main(new String[]{});

		DasshyServer.getInstance().stop();
	}

	public void serverStartThrowsGenericException() throws Exception
	{
		final DasshyServer engine = Mockito.mock(DasshyServer.class);
		Mockito.doThrow(new RuntimeException()).when(engine).start(Mockito.any(IEngineContext.class));

		DasshyServer.setInstance(engine);
		engine.main(new String[]{});

		DasshyServer.getInstance().stop();
	}

	public void serverStartTest() throws Exception
	{
		final DasshyServer engine = new DasshyServer();
		final IEngineContext<DasshyConfiguration> dasshyContext = buildTestServerContext();

		engine.start(dasshyContext);
		engine.stop();
	}

	public void serverStop() throws Exception
	{
		final DasshyServer engine = new DasshyServer();
		final IEngineContext<DasshyConfiguration> dasshyContext = buildTestServerContext();

		engine.start(dasshyContext);
		engine.stop();

		// start again to verify that we've shutdown correctly
		engine.start(dasshyContext);
		engine.stop();
	}

	public void serverShutdownThread() throws Exception
	{
		final DasshyServer engine = Mockito.mock(DasshyServer.class);
		Mockito.doNothing().when(engine).stop();

		final DasshyServer.DasshyServerShutdownThread shutdownThread = new DasshyServer.DasshyServerShutdownThread(engine);
		shutdownThread.run();

		Mockito.verify(engine, Mockito.times(1)).stop();
	}

	public void serverContext() throws Exception
	{
		final DasshyServer.DasshyServerContext engineContext = new DasshyServer.DasshyServerContext(new String[]{});

		Assertions.assertThat(engineContext.getConfigurationFactory()).isNotNull();
		Assertions.assertThat(engineContext.getEngineRuntimeFactory()).isNotNull();
	}

	private IEngineContext<DasshyConfiguration> buildTestServerContext()
	{
		return new IEngineContext<DasshyConfiguration>()
		{
			@Override
			public IConfigurationFactory<DasshyConfiguration> getConfigurationFactory()
			{
				return new TestDasshyConfiguration();
			}

			@Override
			public IEngineRuntimeFactory<DasshyConfiguration> getEngineRuntimeFactory()
			{
				return new IEngineRuntimeFactory<DasshyConfiguration>()
				{
					@Override
					public IEngineRuntime build(DasshyConfiguration config)
					{
						return new TestDasshyRuntime();
					}
				};
			}
		};
	}

	private static final class TestDasshyConfiguration implements IConfigurationFactory
	{
		@Override
		public IEngineConfiguration build(Class klass)
		{
			final DasshyConfiguration config = new DasshyConfiguration();
			// load defaults
			config.loadConfiguration(DynamicPropertyFactory.getInstance());

			return config;
		}
	}

	private static final class TestDasshyRuntime extends DasshyRuntime
	{
		@Override
		public void manage(IEngineComponent component)
		{
			// do-nothing
		}

		@Override
		public void shutdown()
		{
			// do-nothing
		}
	}
}
