package com.kromatik.dasshy.sdk;

/**
 * Abstract transformer
 */
public abstract class AbstractTransformer implements Transformer
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
