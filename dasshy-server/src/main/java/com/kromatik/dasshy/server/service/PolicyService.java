package com.kromatik.dasshy.server.service;

import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.exception.PolicyNotFoundException;
import com.kromatik.dasshy.server.policy.PolicyListener;
import com.kromatik.dasshy.thrift.model.TPolicy;
import com.kromatik.dasshy.thrift.model.TPolicyList;

import java.util.ArrayList;

/**
 * Service that handles all the policy management logic
 */
public class PolicyService
{
	/** policy dao */
	private final PolicyDao			policyDao;

	/** policy listener */
	private final PolicyListener	policyListener;

	/**
	 * Default constructor
	 *
	 * @param policyDao policy dao
	 * @param policyListener policy listener
	 */
	public PolicyService(final PolicyDao policyDao, final PolicyListener policyListener)
	{
		this.policyDao = policyDao;
		this.policyListener = policyListener;
	}

	/**
	 * Creates a policy
	 *
	 * @param policy policy to be created
	 *
	 * @return an created policy
	 */
	public TPolicy createPolicy(final TPolicy policy)
	{
		policyDao.create(policy);
		policyListener.onPolicySave(policy);
		return policy;
	}

	/**
	 * Updates a policy
	 *
	 * @param policy policy to be updated
	 *
	 * @return updated policy
	 */
	public TPolicy updatePolicy(final TPolicy policy)
	{
		policy.setLastUpdated(System.currentTimeMillis());

		policyDao.update(policy);
		policyListener.onPolicySave(policy);
		return policy;
	}

	/**
	 * Deletes a policy
	 *
	 * @param policyId policy Id
	 *
	 * @return true/false
	 */
	public boolean deletePolicy(final String policyId)
	{
		TPolicy policy = getPolicy(policyId);
		if (policy == null)
		{
			return false;
		}

		policyDao.delete(policy);
		policyListener.onPolicyDelete(policy);
		return true;
	}

	/**
	 * Retrieves a policy based on the given Id
	 *
	 * @param policyId policy Id
	 *
	 * @return policy
	 */
	public TPolicy getPolicy(final String policyId)
	{
		final TPolicy policy = policyDao.get(policyId);

		if (policy == null)
		{
			throw new PolicyNotFoundException("Policy with id: " + policyId + " not found");
		}

		return policy;
	}

	/**
	 * List all available policies
	 *
	 * @return list of policies
	 */
	public TPolicyList listPolicies()
	{
		return new TPolicyList(new ArrayList<>(policyDao.list()));
	}
}
