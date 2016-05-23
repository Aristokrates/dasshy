package com.kromatik.dasshy.sdk;

import java.util.Map;

/**
 * Common configuration class for any execution stage
 */
public abstract class StageConfiguration
{
	/**
	 *
	 * @return configuration as map
	 */
	public abstract Map<String, String> getConfigAsMap();
}
