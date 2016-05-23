package com.kromatik.dasshy.sdk;

/**
 * Abstract loader
 */
public abstract class AbstractLoader implements Loader
{
	@Override
	public void init(final RuntimeContext runtimeContext, final StageConfiguration configuration)
	{
		// no implementation; override if needed
	}

	@Override
	public void clean(final RuntimeContext runtimeContext)
	{
		// no implementation; override if needed
	}
}
