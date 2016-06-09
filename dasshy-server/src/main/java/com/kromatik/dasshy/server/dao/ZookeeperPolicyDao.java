package com.kromatik.dasshy.server.dao;

import com.kromatik.dasshy.server.exception.InvalidPolicyException;
import com.kromatik.dasshy.server.exception.PolicyExistsException;
import com.kromatik.dasshy.server.exception.PolicyNotFoundException;
import com.kromatik.dasshy.server.exception.ZookeeperException;
import com.kromatik.dasshy.server.thrift.TUtils;
import com.kromatik.dasshy.server.zookeeper.IZookeeperClient;
import com.kromatik.dasshy.server.zookeeper.IZookeeperClientFactory;
import com.kromatik.dasshy.server.zookeeper.IZookeeperClientProperties;
import com.kromatik.dasshy.thrift.model.TPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.codehaus.jackson.JsonProcessingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Dao that manages policies in Zookeepeer
 */
public class ZookeeperPolicyDao extends AbstractZookeeperDao implements PolicyDao
{

	public static final String	POLICY_PATH	=	"/policy";

	/**
	 * Zookeeper policy dao
	 *
	 * @param clientFactory factory
	 * @param properties properties
	 */
	public ZookeeperPolicyDao(final IZookeeperClientFactory clientFactory, final IZookeeperClientProperties properties)
	{
		super(clientFactory, properties);
	}

	@Override
	public void create(final TPolicy policy)
	{
		if (exists(policy.getId()))
		{
			throw new PolicyExistsException("Policy with the same Id already exists");
		}

		try
		{
			final IZookeeperClient zkClient = super.getClient();
			final CuratorFramework curatorFramework = zkClient.getCuratorFramework();

			String id = policy.getId();
			if (id == null)
			{
				id = UUID.randomUUID().toString();
			}
			policy.setId(id);

			byte[] policyBytes = TUtils.serializeJson(policy);
			curatorFramework.create().creatingParentsIfNeeded().forPath(getPolicyPath(id), policyBytes);
		}
		catch (final JsonProcessingException e)
		{
			throw new InvalidPolicyException("Invalid model", e);
		}
		catch (final Exception e)
		{
			throw new ZookeeperException("Zookeeper operation has failed", e);
		}

	}

	@Override
	public void update(final TPolicy policy)
	{
		TPolicy existing = get(policy.getId());

		if (existing == null)
		{
			throw new PolicyNotFoundException("Policy not found for update");
		}

		try
		{
			final IZookeeperClient zkClient = super.getClient();
			final CuratorFramework curatorFramework = zkClient.getCuratorFramework();

			// merge the policies
			existing.setInterval(policy.getInterval());
			existing.setExtractor(policy.getExtractor());
			existing.setTransformer(policy.getTransformer());
			existing.setLoader(policy.getLoader());
			existing.setLastUpdated(policy.getLastUpdated());

			existing.setError(policy.getError());
			existing.setStartTime(policy.getStartTime());
			existing.setEndTime(policy.getEndTime());
			existing.setState(policy.getState());

			byte[] policyBytes = TUtils.serializeJson(existing);
			curatorFramework.setData().forPath(getPolicyPath(existing.getId()), policyBytes);
		}
		catch (final JsonProcessingException e)
		{
			throw new InvalidPolicyException("Invalid model", e);
		}
		catch (final Exception e)
		{
			throw new ZookeeperException("Zookeeper operation has failed", e);
		}
	}

	@Override
	public void delete(final TPolicy policy)
	{
		try
		{
			final IZookeeperClient zkClient = super.getClient();
			zkClient.getCuratorFramework().delete().forPath(getPolicyPath(policy.getId()));
		}
		catch (final KeeperException.NoNodeException noNodeEx) //NOSONAR
		{
			// ignore it
			return;
		}
		catch (final Exception e)
		{
			throw new ZookeeperException("Operation has failed", e);
		}
	}

	@Override
	public TPolicy get(final String id)
	{
		byte[] zkObject = null;
		try
		{
			final IZookeeperClient zkClient = super.getClient();
			// read from ZK
			zkObject = zkClient.getCuratorFramework().getData().forPath(getPolicyPath(id));
		}
		catch (final KeeperException.NoNodeException noNodeEx)	//NOSONAR
		{
			return null;
		}
		catch (final Exception e)
		{
			throw new ZookeeperException("Operation has failed", e);
		}

		// convert to policy
		try
		{
			TPolicy policy = new TPolicy();
			TUtils.deserializeJson(zkObject, policy);
			return policy;
		}
		catch (final Exception e)
		{
			throw new ZookeeperException("Operation has failed", e);
		}
	}

	@Override
	public boolean exists(String id)
	{
		try
		{
			final IZookeeperClient zkClient = super.getClient();
			final Stat stat = zkClient.getCuratorFramework().checkExists().forPath(getPolicyPath(id));

			return stat != null ;
		}
		catch (final Exception e)
		{
			throw new ZookeeperException("Operation has failed", e);
		}
	}

	@Override
	public Collection<TPolicy> list()
	{
		final Collection<TPolicy> policies = new ArrayList<>();
		try
		{
			final IZookeeperClient zkClient = super.getClient();

			final Stat policyPath = zkClient.getCuratorFramework().checkExists().forPath(POLICY_PATH);

			/** Check if the path exists */
			if (policyPath != null)
			{
				final List<String> policyIds = zkClient.getCuratorFramework()
								.getChildren().forPath(POLICY_PATH);
				for (final String policyId : policyIds)
				{
					policies.add(get(policyId));
				}
			}
		}
		catch (final Exception e)
		{
			throw new ZookeeperException("Operation has failed", e);
		}

		return policies;
	}

	/**
	 * ZNode path of the policy
	 *
	 * @param policyId id of the policy
	 *
	 * @return policy path
	 */
	private String getPolicyPath(final String policyId)
	{
		return POLICY_PATH + "/" + policyId;
	}

}
