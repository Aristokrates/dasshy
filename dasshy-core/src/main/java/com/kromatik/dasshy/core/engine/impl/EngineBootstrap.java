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
