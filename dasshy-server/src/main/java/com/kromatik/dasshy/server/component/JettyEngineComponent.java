package com.kromatik.dasshy.server.component;

import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.server.config.DasshyConfiguration;
import com.kromatik.dasshy.server.config.JettyServerConfiguration;
import com.kromatik.dasshy.server.rest.DasshyRestApi;
import com.kromatik.dasshy.server.rest.PolicyRestApi;
import com.kromatik.dasshy.server.service.PolicyService;
import com.kromatik.dasshy.server.streaming.DasshyRuntime;
import com.kromatik.dasshy.server.thrift.SimpleJsonPayLoadProvider;
import com.kromatik.dasshy.server.thrift.ThriftJsonPayLoadProvider;
import com.kromatik.dasshy.server.thrift.ThriftPayLoadProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
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

	/** dasshy runtime */
	private static DasshyRuntime			runtime;

	/** policy service */
	private static PolicyService			policyService;

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
	 * @param dasshyRuntime	dasshy runtime
	 * @param service policy service
	 */
	public JettyEngineComponent(
					final DasshyConfiguration configuration,
					final DasshyRuntime dasshyRuntime,
					final PolicyService service)
	{
		jettyConfiguration = configuration.getJettyConfiguration();
		runtime = dasshyRuntime;
		policyService = service;
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
			LOGGER.error("Error when running embedded jetty server");
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
			LOGGER.error("Error when stopping the embedded jetty server");
		}
	}

	@Override
	public Set<Class<?>> getClasses()
	{
		Set<Class<?>> classes = new HashSet<Class<?>>();

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

		final DasshyRestApi root = new DasshyRestApi(runtime);
		final PolicyRestApi policyRestApi = new PolicyRestApi(policyService);

		// add more resources

		singletons.add(root);
		singletons.add(policyRestApi);

		return singletons;
	}
}
