package com.kromatik.dasshy.server.streaming;

import com.kromatik.dasshy.core.engine.IEngineRuntime;
import com.kromatik.dasshy.core.engine.IEngineRuntimeFactory;
import com.kromatik.dasshy.server.config.DasshyConfiguration;

/**
 * Runtime factory for the Dasshy engine
 */
public class DasshyRuntimeFactory implements IEngineRuntimeFactory<DasshyConfiguration>
{
	@Override
	public IEngineRuntime build(final DasshyConfiguration config)
	{
		return new DasshyRuntime();
	}
}
