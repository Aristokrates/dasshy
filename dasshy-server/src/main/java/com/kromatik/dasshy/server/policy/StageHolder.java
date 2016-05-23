package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.sdk.StageConfiguration;
import com.kromatik.dasshy.sdk.IStage;

/**
 * Holds an execution stage instance and its configuration
 *
 * @param <S> execution stage
 * @param <C> its configuration
 */
public abstract class StageHolder<S extends IStage, C extends StageConfiguration>
{

	private final S		stage;

	private final C		configuration;

	/**
	 * Default constructor
	 *
	 * @param stage stage
	 * @param configuration configuration
	 */
	public StageHolder(final S stage, final C configuration)
	{
		this.stage = stage;
		this.configuration = configuration;
	}

	public S stage()
	{
		return stage;
	}

	public C configuration()
	{
		return configuration;
	}
}
