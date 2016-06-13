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
package com.kromatik.dasshy.server.component;

import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.server.config.DasshyConfiguration;
import com.kromatik.dasshy.server.config.JettyServerConfiguration;
import com.kromatik.dasshy.server.exception.mapper.CommonExceptionMapper;
import com.kromatik.dasshy.server.exception.mapper.EngineExceptionMapper;
import com.kromatik.dasshy.server.exception.mapper.NotFoundExceptionMapper;
import com.kromatik.dasshy.server.exception.mapper.WebApplicationExceptionMapper;
import com.kromatik.dasshy.server.rest.DasshyRestApi;
import com.kromatik.dasshy.server.rest.PolicyRestApi;
import com.kromatik.dasshy.server.rest.StagePluginRestApi;
import com.kromatik.dasshy.server.service.PolicyService;
import com.kromatik.dasshy.server.service.StagePluginService;
import com.kromatik.dasshy.server.thrift.SimpleJsonPayLoadProvider;
import com.kromatik.dasshy.server.thrift.ThriftJsonPayLoadProvider;
import com.kromatik.dasshy.server.thrift.ThriftPayLoadProvider;
import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Engine component that runs embedded jetty and initializes a rest api
 */
public class JettyEngineComponent extends Application implements IEngineComponent
{

	private static final Logger LOGGER	=	LoggerFactory.getLogger(JettyEngineComponent.class);

	/** underlying jetty server */
	private Server							jettyServer;

	/** jetty server configuration */
	private JettyServerConfiguration		jettyConfiguration;

	// all objects used by methods #getClasses and #getSingletons needs to be static!!!

	/** policy service */
	private static PolicyService			policyService;

	/** plugin service */
	private static StagePluginService		pluginService;

	/**
	 * Constructor called by Jersey to initialize the Application and its resources
	 * This constructor is called via reflection
	 */
	public JettyEngineComponent()
	{
		// no-arg
	}

	/**
	 * Default constructor, entry point of this component
	 *
	 * @param configuration engine configuration
	 * @param service policy service
	 * @param stagePluginService plugin service
	 */
	public JettyEngineComponent(
					final DasshyConfiguration configuration,
					final PolicyService service,
					final StagePluginService stagePluginService)
	{
		jettyConfiguration = configuration.getJettyConfiguration();
		policyService = service;	//NOSONAR
		pluginService = stagePluginService;	//NOSONAR
	}

	@Override
	public void start()
	{
		jettyServer = initWebServer();
		final ServletContextHandler apiContext = initApiServletContextHandler();

		// add handlers
		final ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[]{apiContext});
		jettyServer.setHandler(contexts);

		LOGGER.info("Starting embedded jetty server");

		try
		{
			jettyServer.start();
		}
		catch (Exception e)
		{
			LOGGER.error("Error when running embedded jetty server", e);
			return;
		}

		LOGGER.info("Embedded jetty server started");

	}

	/**
	 * @return jetty server
	 */
	private Server initWebServer()
	{
		final Server server = new Server();

		// TODO (pai) support https
		final AbstractConnector connector = new SelectChannelConnector();

		connector.setHost(jettyConfiguration.getHost());
		connector.setPort(jettyConfiguration.getPort());
		connector.setSoLingerTime(-1);
		connector.setMaxIdleTime(jettyConfiguration.getMaxIdleTime());

		server.addConnector(connector);

		return server;
	}

	/**
	 * @return servlet context handler
	 */
	private ServletContextHandler initApiServletContextHandler()
	{
		final ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
		servletHolder.setName("rest");
		servletHolder.setForcedPath("rest");

		servletHolder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

		// this line results in calling the no-arg constructor and methods(getSingletons and getClasses) on jetty start
		servletHolder.setInitParameter("javax.ws.rs.Application", JettyEngineComponent.class.getName());


		final ServletContextHandler context = new ServletContextHandler();
		context.setSessionHandler(new SessionHandler());
		context.setContextPath(jettyConfiguration.getContextPath());
		context.addServlet(servletHolder, "/api/*");


		// TODO (pai) enable CORS and auth
		/*
		context.addFilter(new FilterHolder(CorsFilter.class), "*//*", EnumSet.allOf(DispatcherType.class));

		context.setInitParameter("shiroConfigLocations", new File("shiro.ini").toURI().toString());
		context.addFilter(ShiroFilter.class, "*//*", EnumSet.allOf(DispatcherType.class));
		context.addEventListener(new EnvironmentLoaderListener());
		*/

		return context;

	}

	@Override
	public void stop()
	{
		LOGGER.info("Stopping embedded jetty server");

		try
		{
			jettyServer.stop();
		}
		catch (Exception e)
		{
			LOGGER.error("Error when stopping the embedded jetty server", e);
		}
	}

	@Override
	public Set<Class<?>> getClasses()
	{
		Set<Class<?>> classes = new HashSet<>();

		// add other providers here
		classes.add(SimpleJsonPayLoadProvider.class);
		classes.add(ThriftJsonPayLoadProvider.class);
		classes.add(ThriftPayLoadProvider.class);

		classes.add(GZipEncoder.class);
		classes.add(EncodingFilter.class);

		return classes;
	}

	@Override
	public Set<Object> getSingletons()
	{
		Set<Object> singletons = new HashSet<>();

		final DasshyRestApi root = new DasshyRestApi();
		final PolicyRestApi policyRestApi = new PolicyRestApi(policyService);
		final StagePluginRestApi stagePluginRestApi = new StagePluginRestApi(pluginService);

		// add more resources

		singletons.add(root);
		singletons.add(policyRestApi);
		singletons.add(stagePluginRestApi);


		// add more mappers

		singletons.add(new CommonExceptionMapper());
		singletons.add(new EngineExceptionMapper());
		singletons.add(new NotFoundExceptionMapper());
		singletons.add(new WebApplicationExceptionMapper());

		return singletons;
	}
}
