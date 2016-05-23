package com.kromatik.dasshy.sdk;

/**
 * Action fired within a policy
 */
public interface Action
{
	/**
	 * Fire an action
	 *
	 * @param runtimeContext runtime context
	 */
	void fire(final RuntimeContext runtimeContext);

}
