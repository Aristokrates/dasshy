package com.kromatik.dasshy.server;

import com.kromatik.dasshy.core.config.IConfigurationFactory;
import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.config.impl.ArchaiusConfigurationFactory;
import com.kromatik.dasshy.core.engine.IEngineBootstrap;
import com.kromatik.dasshy.core.engine.IEngineContext;
import com.kromatik.dasshy.core.engine.IEngineRuntime;
import com.kromatik.dasshy.core.engine.IEngineRuntimeFactory;
import com.kromatik.dasshy.core.engine.impl.AbstractEngine;
import com.kromatik.dasshy.server.component.JettyEngineComponent;
import com.kromatik.dasshy.server.component.SparkEngineComponent;
import com.kromatik.dasshy.server.component.StreamingEngineComponent;
import com.kromatik.dasshy.server.component.ZookeeperEngineComponent;
import com.kromatik.dasshy.server.config.DasshyConfiguration;
import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.dao.ZookeeperPolicyDao;
import com.kromatik.dasshy.server.policy.DefaultPolicyFactory;
import com.kromatik.dasshy.server.policy.JobsOnPolicyListener;
import com.kromatik.dasshy.server.policy.PolicyFactory;
import com.kromatik.dasshy.server.policy.PolicyListener;
import com.kromatik.dasshy.server.scheduler.ConcurrentPolicyScheduler;
import com.kromatik.dasshy.server.scheduler.JobUpdateListener;
import com.kromatik.dasshy.server.scheduler.PolicyScheduler;
import com.kromatik.dasshy.server.service.PolicyService;
import com.kromatik.dasshy.server.streaming.DasshyRuntime;
import com.kromatik.dasshy.server.streaming.DasshyRuntimeFactory;
import com.kromatik.dasshy.server.zookeeper.ZookeeperClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

/**
 * The entry point of the Dasshy server
 *
 * @see AbstractEngine
 */
public class DasshyServer extends AbstractEngine<DasshyConfiguration>
{

	private static final Logger LOGGER	=	LoggerFactory.getLogger(DasshyServer.class);

	private static DasshyServer		INSTANCE;

	/**
	 * Runs the dasshy server as java App
	 *
	 * @param args command line arguments
	 */
	public static void main(final String [] args)
	{
		final IEngineContext<DasshyConfiguration> dasshyServerContext = new DasshyServerContext(args);

		if (DasshyServer.getInstance() == null)
		{
			DasshyServer.setInstance(new DasshyServer());
		}

		try
		{
			DasshyServer.getInstance().start(dasshyServerContext);
			Runtime.getRuntime().addShutdownHook(new DasshyServerShutdownThread(DasshyServer.getInstance()));
		}
		catch (Exception engineException)
		{
			LOGGER.error("Exception occurred when starting the engine", engineException);
		}
	}

	/**
	 * @see AbstractEngine#init(IEngineBootstrap)
	 */
	@Override
	protected void init(final IEngineBootstrap<DasshyConfiguration> bootstrap)
	{
		// add bundles to the bootstrap --> bootstrap.addBundle()
	}

	/**
	 * @see AbstractEngine#run(IEngineConfiguration, IEngineRuntime, IEngineContext)
	 */
	@Override
	protected void run(final DasshyConfiguration configuration, final IEngineRuntime engineRuntime,
					final IEngineContext<DasshyConfiguration> engineContext)
	{

		final DasshyRuntime dasshyRuntime = (DasshyRuntime) engineRuntime;

		// 1. manage zookeeper component
		engineRuntime.manage(new ZookeeperEngineComponent(ZookeeperClientFactory.getInstance()));

		// 2. manage spark engine component
		engineRuntime.manage(new SparkEngineComponent(configuration, dasshyRuntime));

		final PolicyDao policyDao = new ZookeeperPolicyDao(
						ZookeeperClientFactory.getInstance(),
						configuration.getZookeeperClientConfiguration());

		final PolicyScheduler policyScheduler = new ConcurrentPolicyScheduler(Executors.newScheduledThreadPool(10));
		final PolicyFactory policyFactory = new DefaultPolicyFactory();
		final PolicyListener policyListener = new JobsOnPolicyListener(
						dasshyRuntime.getRuntimeContext(),
						policyFactory,
						policyScheduler,
						new JobUpdateListener(policyDao));

		// 3. manage jetty component
		final PolicyService policyService = new PolicyService(policyDao, policyListener);
		engineRuntime.manage(new JettyEngineComponent(configuration, dasshyRuntime, policyService));

		// 4. manage policy streaming
		engineRuntime.manage(new StreamingEngineComponent(policyListener, policyScheduler, policyDao));
	}

	/**
	 * Build the server context from the input arguments
	 */
	private static final class DasshyServerContext implements IEngineContext<DasshyConfiguration>
	{

		/**
		 * Default constructor
		 *
		 * @param args input server arguments
		 */
		public DasshyServerContext(final String[] args)
		{
			// ignore the args for now
		}

		/**
		 * @see IEngineContext#getConfigurationFactory()
		 */
		@Override
		public IConfigurationFactory<DasshyConfiguration> getConfigurationFactory()
		{
			return new ArchaiusConfigurationFactory<>();
		}

		/**
		 * @see IEngineContext#getEngineRuntimeFactory()
		 */
		@Override
		public IEngineRuntimeFactory<DasshyConfiguration> getEngineRuntimeFactory()
		{
			return new DasshyRuntimeFactory();
		}
	}

	/**
	 * Shutdown thread called on {@link Runtime#addShutdownHook}}
	 */
	private static final class DasshyServerShutdownThread extends Thread
	{
		private final DasshyServer	dasshyServer;

		/**
		 * @param dasshyServer the server
		 */
		public DasshyServerShutdownThread(final DasshyServer dasshyServer)
		{
			this.dasshyServer = dasshyServer;
		}

		@Override
		public void run()
		{
			dasshyServer.stop();
		}
	}

	/**
	 * @param instance server instance
	 */
	public static void setInstance(final DasshyServer instance)
	{
		DasshyServer.INSTANCE = instance;
	}

	/**
	 * @return server instance
	 */
	public static DasshyServer getInstance()
	{
		return INSTANCE;
	}
}
