package com.kromatik.dasshy.server.dao;

import com.kromatik.dasshy.thrift.model.TPolicy;

import java.util.Collection;

/**
 * Policy dao
 */
public interface PolicyDao
{

	/**
	 * Saves the policy
	 *
	 * @param policy policy model
	 */
	void create(final TPolicy policy);

	/**
	 * Updates a policy
	 *
	 * @param policy policy model
	 */
	void update(final TPolicy policy);

	/**
	 * Deletes a policy
	 *
	 * @param policy policy model
	 */
	void delete(final TPolicy policy);

	/**
	 * Get by id
	 *
	 * @return policy model
	 */
	TPolicy get(final String id);

	/**
	 * Checks if a policy with the same id exists
	 *
	 * @param id
	 *
	 * @return true/false
	 */
	boolean exists(final String id);

	/**
	 * List the policies
	 *
	 * @return policy list
	 */
	Collection<TPolicy> list();

}
