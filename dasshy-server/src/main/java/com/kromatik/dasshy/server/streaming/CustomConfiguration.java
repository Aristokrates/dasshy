package com.kromatik.dasshy.server.streaming;

import com.kromatik.dasshy.sdk.StageConfiguration;

import java.util.Map;

/**
 *
 */
public class CustomConfiguration extends StageConfiguration
{
	private final Map<String, String>			configMap;

	public CustomConfiguration(Map<String, String> values)
	{
		super();
		this.configMap = values;
	}

	@Override
	public Map<String, String> getConfigAsMap()
	{
		return configMap;
	}
}
