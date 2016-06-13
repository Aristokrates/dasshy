package com.kromatik.dasshy.server.dao;

import com.kromatik.dasshy.server.EmbeddedZooKeeper;
import com.kromatik.dasshy.server.config.DasshyConfiguration;
import com.kromatik.dasshy.server.config.ZookeeperClientConfiguration;
import com.kromatik.dasshy.server.zookeeper.IZookeeperClientFactory;
import com.kromatik.dasshy.server.zookeeper.ZookeeperClientFactory;
import com.kromatik.dasshy.thrift.model.TBatchClock;
import com.kromatik.dasshy.thrift.model.TJobState;
import com.kromatik.dasshy.thrift.model.TPolicy;
import com.kromatik.dasshy.thrift.model.TStreamingBatchClock;
import com.netflix.config.DynamicPropertyFactory;
import org.fest.assertions.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collection;

/**
 *	Tests for policy dao
 */
@Test(groups = {"dao"})
public class PolicyDaoTest
{

	protected static EmbeddedZooKeeper testingServer;

	protected static ZookeeperClientConfiguration zookeeperConfiguration =
					new ZookeeperClientConfiguration(new DasshyConfiguration());

	protected static IZookeeperClientFactory zookeeperClientFactory = ZookeeperClientFactory.getInstance();

	private PolicyDao policyDao;

	@BeforeClass
	public void setup() throws Exception
	{
		testingServer = new EmbeddedZooKeeper();
		testingServer.start();
		zookeeperConfiguration.loadConfiguration(DynamicPropertyFactory.getInstance());
		zookeeperConfiguration.setConnectionString(testingServer.getConnectString());

		policyDao = new ZookeeperPolicyDao(zookeeperClientFactory, zookeeperConfiguration);
	}

	@AfterClass
	public void tearDown() throws Exception
	{
		zookeeperClientFactory.close();
		testingServer.close();
	}


	public void savePolicy()
	{
		TPolicy policy = new TPolicy();
		policyDao.create(policy);

		TPolicy existing = policyDao.get(policy.getId());

		Assertions.assertThat(existing).isEqualsToByComparingFields(policy);
	}

	public void updatePolicy()
	{
		TPolicy policy = new TPolicy();
		policyDao.create(policy);

		Integer interval = 15;
		Long startTime = System.currentTimeMillis();
		Long endTime = System.currentTimeMillis();
		String errorMessage = "Error message";

		policy.setClock(new TBatchClock(TBatchClock._Fields.STREAMING, new TStreamingBatchClock(interval)));
		policy.setStartTime(startTime);
		policy.setEndTime(endTime);
		policy.setError(errorMessage);
		policy.setState(TJobState.ERROR);

		policyDao.update(policy);
		TPolicy existing = policyDao.get(policy.getId());

		Assertions.assertThat(existing).isEqualsToByComparingFields(policy);
	}

	public void policyExists()
	{
		TPolicy policy = new TPolicy();
		policyDao.create(policy);

		boolean exists = policyDao.exists(policy.getId());
		Assertions.assertThat(exists).isTrue();
	}

	public void deletePolicy()
	{
		TPolicy policy = new TPolicy();
		policyDao.create(policy);

		policyDao.delete(policy);

		boolean exists = policyDao.exists(policy.getId());
		Assertions.assertThat(exists).isFalse();

		TPolicy existing = policyDao.get(policy.getId());
		Assertions.assertThat(existing).isNull();
	}

	public void listPolicy()
	{
		TPolicy policy1 = new TPolicy();
		policyDao.create(policy1);

		TPolicy policy2 = new TPolicy();
		policyDao.create(policy2);

		Collection<TPolicy> policies = policyDao.list();
		Assertions.assertThat(policies).containsOnly(policy1, policy2);
	}
}
