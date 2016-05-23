package com.kromatik.dasshy.core.engine;

import com.kromatik.dasshy.core.config.IEngineConfiguration;

/**
 * Common engine abstraction. The engine is bootstrapped using the engine context
 * within its own engine configuration
 */
public interface IEngine<T extends IEngineConfiguration>
{
	/**
	 * Starts the engine. This is the entry point of the engine
	 *
	 * @param engineContext init engine context
	 */
	void start(final IEngineContext<T> engineContext);

	/**
	 * Gracefully stops the engine
	 */
	void stop();
}
