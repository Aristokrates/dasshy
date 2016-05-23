package com.kromatik.dasshy.server.config;

import com.kromatik.dasshy.core.config.impl.AbstractEngineCompositeConfiguration;

/**
 * MAIN configuration of the Dasshy Server
 * It contains all nested, dependency configurations for the server
 */
public class DasshyConfiguration extends AbstractEngineCompositeConfiguration
{

	/** spark related configuration */
	private final SparkConfiguration			sparkConfiguration;

	/** kafka related configuration */
	private final KafkaConfiguration			kafkaConfiguration;

	/** zookeeper related configurations */
	private final ZookeeperClientConfiguration	zookeeperClientConfiguration;

	/** configuration for the embedded jetty server */
	private final JettyServerConfiguration		jettyConfiguration;

	/**
	 * Default constructor
	 */
	public DasshyConfiguration()
	{
		sparkConfiguration = new SparkConfiguration(this);
		kafkaConfiguration = new KafkaConfiguration(this);
		zookeeperClientConfiguration = new ZookeeperClientConfiguration(this);
		jettyConfiguration = new JettyServerConfiguration(this);

		// add other configurations
	}

	/**
	 * @return spark configuration
	 */
	public SparkConfiguration getSparkConfiguration()
	{
		return sparkConfiguration;
	}

	/**
	 * @return kafka configuration
	 */
	public KafkaConfiguration getKafkaConfiguration()
	{
		return kafkaConfiguration;
	}

	/**
	 * @return zookeeper client configuration
	 */
	public ZookeeperClientConfiguration getZookeeperClientConfiguration()
	{
		return zookeeperClientConfiguration;
	}

	/**
	 * @return jetty configutation
	 */
	public JettyServerConfiguration getJettyConfiguration()
	{
		return jettyConfiguration;
	}
}
