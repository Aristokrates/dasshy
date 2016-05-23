package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.sdk.StageConfiguration;
import com.kromatik.dasshy.sdk.Transformer;

/**
 * Transformer placeholder
 */
public class TransformerHolder extends StageHolder<Transformer, StageConfiguration>
{
	/**
	 * Default constructor
	 *
	 * @param stage         stage
	 * @param configuration configuration
	 */
	public TransformerHolder(final Transformer stage, final StageConfiguration configuration)
	{
		super(stage, configuration);
	}
}
