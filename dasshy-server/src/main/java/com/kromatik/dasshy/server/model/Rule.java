package com.kromatik.dasshy.server.model;

import com.kromatik.dasshy.sdk.Action;
import com.kromatik.dasshy.sdk.Condition;
import com.kromatik.dasshy.sdk.RuntimeContext;

/**
 * A rule that is evaluated against the given policy.
 * The rule evaluation consist of: IF condition then action
 */
public class Rule implements Action
{
	/** condition */
	private Condition condition;

	/** action */
	private Action action;

	@Override
	public void fire(final RuntimeContext runtimeContext)
	{
		boolean conditionMet = false;

		if (condition != null)
		{
			conditionMet = condition.evaluate(runtimeContext);
		}
		if (conditionMet)
		{
			action.fire(runtimeContext);
		}
	}
}
