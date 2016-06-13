package com.kromatik.dasshy.server.spark;

import com.kromatik.dasshy.core.engine.impl.EngineRuntime;
import com.kromatik.dasshy.sdk.RuntimeContext;

/**
 * Runtime for the dasshy server
 */
public class DasshyRuntime extends EngineRuntime
{
	/** spark runtime */
	private RuntimeContext runtimeContext;

	/**
	 * Default constrctor
	 */
	public DasshyRuntime()
	{
		super();
	}

	/**
	 * @return spark runtime context
	 */
	public RuntimeContext getRuntimeContext()
	{
		return runtimeContext;
	}

	/**
	 * @param runtimeContext spark runtime context to set
	 */
	public void setRuntimeContext(RuntimeContext runtimeContext)
	{
		this.runtimeContext = runtimeContext;
	}
}
