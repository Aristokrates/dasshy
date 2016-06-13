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

import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.core.engine.IEngineRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @see IEngineRuntime
 */
public class EngineRuntime implements IEngineRuntime
{

	private static final Logger LOGGER	=	LoggerFactory.getLogger(EngineRuntime.class);

	/** components in the engine runtime environment */
	private final Deque<IEngineComponent>				engineComponents;

	/**
	 * Default constructor
	 */
	public EngineRuntime()
	{
		engineComponents = new LinkedList<>();
	}

	/**
	 * @see IEngineRuntime#manage(IEngineComponent)
	 */
	@Override
	public void manage(final IEngineComponent component)
	{
		LOGGER.info("Running engine component: {}", component.getClass().getSimpleName());
		component.start();
		engineComponents.add(component);
	}

	/**
	 * @see IEngineRuntime#shutdown()
	 */
	@Override
	public void shutdown()
	{
		for (final IEngineComponent component : engineComponents)
		{
			LOGGER.info("Stopping engine component: {}", component.getClass().getSimpleName());
			component.stop();
		}
	}
}
