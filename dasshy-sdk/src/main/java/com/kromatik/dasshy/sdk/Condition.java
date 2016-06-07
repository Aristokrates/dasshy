package com.kromatik.dasshy.sdk;

/**
 * Rule condition
 */
public interface Condition
{
	/**
	 * Determines how the condition evaluates the data
	 *
	 * @param context runtme context
	 * @return true or false
	 */
	boolean evaluate(final RuntimeContext context);
}
