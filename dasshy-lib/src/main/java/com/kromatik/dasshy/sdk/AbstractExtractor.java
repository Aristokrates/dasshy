package com.kromatik.dasshy.sdk;

/**
 * Abstract extractor
 */
public abstract class AbstractExtractor implements Extractor
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
