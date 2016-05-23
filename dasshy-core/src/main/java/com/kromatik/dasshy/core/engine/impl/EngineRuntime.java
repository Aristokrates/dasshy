package com.kromatik.dasshy.core.engine.impl;

import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.core.engine.IEngineRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

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
