package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.thrift.model.TPolicy;

/**
 * Policy Listener
 */
public interface PolicyListener
{
	/**
	 * Called when the policy is saved; either created or updated
	 *
	 * @param policyModel
	 */
	void onPolicySave(final TPolicy policyModel);

	/**
	 * Called when a policy is deleted
	 *
	 * @param policyModel
	 */
	void onPolicyDelete(final TPolicy policyModel);
}
