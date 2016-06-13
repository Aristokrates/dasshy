/**
 * Dasshy - Real time Streaming and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions
 *
 * This file is part of Dasshy
 *
 * Dasshy is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Dasshy is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dasshy.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	private final Condition		condition;

	/** action */
	private final Action		action;

	/**
	 * Default constructor
	 *
	 * @param condition	condition
	 * @param action	action
	 */
	public Rule(final Condition condition, final Action action)
	{
		this.condition = condition;
		this.action = action;
	}

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
