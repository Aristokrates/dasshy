package com.kromatik.dasshy.core.engine;

/**
 * Component that is part of the engine and it's started & stopped when the engine is started and stopped.
 * The engine components are running within the engine runtime environment
 */
public interface IEngineComponent
{

	/**
	 * Starts the component within the engine runtime
	 */
	void start();

	/**
	 * Stops the component
	 */
	void stop();
}
