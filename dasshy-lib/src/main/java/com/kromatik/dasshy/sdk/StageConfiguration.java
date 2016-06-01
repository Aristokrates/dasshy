package com.kromatik.dasshy.sdk;

import java.util.Map;

/**
 * Configuration class for any execution stage
 */
public class StageConfiguration
{
	/** values */
	private final Map<String, String>			values;

	/**
	 * Default constructor
	 *
	 * @param values configuration values
	 */
	public StageConfiguration(final Map<String, String> values)
	{
		this.values = values;
	}

	/**
	 * Configuration map
	 *
	 * @return map
	 */
	public Map<String, String> getValues()
	{
		return values;
	}
}
