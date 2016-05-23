package com.kromatik.dasshy.core.engine.impl;

import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.engine.IEngineRuntime;
import com.kromatik.dasshy.core.engine.IEngineRuntimeFactory;

/**
 * Default engine runtime factory
 */
public class DefaultEngineRuntimeFactory<T extends IEngineConfiguration> implements IEngineRuntimeFactory<T>
{

	/**
	 * @see IEngineRuntimeFactory#build(IEngineConfiguration)
	 */
	@Override
	public IEngineRuntime build(final T config)
	{
		return new EngineRuntime();
	}
}
