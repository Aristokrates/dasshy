package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.sdk.StageConfiguration;
import com.kromatik.dasshy.sdk.Extractor;

/**
 * Extractor placeholder
 */
public class ExtractorHolder extends StageHolder<Extractor, StageConfiguration>
{
	/**
	 * Default constructor
	 *
	 * @param extractor extractor instance
	 * @param extractorConfig extractor instance config
	 */
	public ExtractorHolder(final Extractor extractor, final StageConfiguration extractorConfig)
	{
		super(extractor, extractorConfig);
	}
}
