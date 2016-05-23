package com.kromatik.dasshy.core.engine.impl;

import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.engine.IEngineBootstrap;
import com.kromatik.dasshy.core.engine.IEngineContext;
import com.kromatik.dasshy.core.engine.IEngineBundle;
import com.kromatik.dasshy.core.engine.IEngineRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @see IEngineBootstrap
 */
public class EngineBootstrap<T extends IEngineConfiguration> implements IEngineBootstrap<T>
{

	private static final Logger LOGGER	=	LoggerFactory.getLogger(EngineBootstrap.class);

	/** engine itself */
	private final AbstractEngine<T>					engine;

	/** list of loaded bundles as part of the bootstrap */
	private final List<IEngineBundle<? super T>>	bundles;

	/** engine init context */
	private final IEngineContext<T> 				engineContext;

	/**
	 * Creates a new engine bootstrap
	 *
	 * @param engine      an engine
	 * @param engineContext initialization context for the engine
	 */
	public EngineBootstrap(final AbstractEngine<T> engine, final IEngineContext<T> engineContext)
	{
		this.engine = engine;
		this.engineContext = engineContext;
		this.bundles = new ArrayList<>();
	}

	/**
	 * @see IEngineBootstrap#run(IEngineConfiguration, IEngineRuntime)
	 */
	@Override
	public void run(final T configuration, final IEngineRuntime engineRuntime)
	{
		for (final IEngineBundle<? super T> bundle : bundles)
		{
			LOGGER.info("Loading bundle: {}", bundle.getClass().getSimpleName());
			bundle.run(configuration, engineRuntime);
		}
	}

	/**
	 * @see IEngineBootstrap#addBundle(IEngineBundle)
	 */
	@Override
	public void addBundle(final IEngineBundle<T> bundle)
	{
		bundles.add(bundle);
	}

	/**
	 *
	 * @return the engine
	 */
	public AbstractEngine<T> getEngine()
	{
		return engine;
	}

	/**
	 *
	 * @return engine context
	 */
	public IEngineContext<T> getEngineContext()
	{
		return engineContext;
	}
}
