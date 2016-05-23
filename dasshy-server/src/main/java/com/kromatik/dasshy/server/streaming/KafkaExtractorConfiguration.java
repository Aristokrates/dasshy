package com.kromatik.dasshy.server.streaming;

import com.kromatik.dasshy.sdk.StageConfiguration;

import java.util.Map;

/**
 * Configuration for Kafka extractor
 */
public class KafkaExtractorConfiguration extends StageConfiguration
{
	/** kafka broker host*/
	private String					host;

	/** kafka broker port */
	private Integer					port;

	/** kafka topic to consume from */
	private String					topic;

	/** kafka consumer offset*/
	private int						offset;

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public Integer getPort()
	{
		return port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}

	public String getTopic()
	{
		return topic;
	}

	public void setTopic(String topic)
	{
		this.topic = topic;
	}

	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	@Override
	public Map<String, String> getConfigAsMap()
	{
		return null;
	}
}
