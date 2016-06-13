/**
 * Dasshy - Real time and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions (http://kromatiksolutions.com)
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
package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.server.model.RuleGroup;
import com.kromatik.dasshy.server.spark.BatchClock;
import com.kromatik.dasshy.thrift.model.TPolicy;

/**
 * A policy instance that is build out of a policy model
 */
public class Policy
{

	/** the model that built this policy instance */
	private TPolicy model;

	/** batch clock for this policy determining how many times this policy will be executed */
	private BatchClock clock;

	/** extractor holder: consisting of instance and its configuration */
	private ExtractorHolder extractor;

	/** transformer tuple: consisting of instance and its configuration */
	private TransformerHolder transformer;

	/** loader tuple: consisting of instance and its configuration */
	private LoaderHolder loader;

	/** rule group */
	private RuleGroup ruleGroup;

	public TPolicy getModel()
	{
		return model;
	}

	public void setModel(TPolicy model)
	{
		this.model = model;
	}

	public BatchClock getClock()
	{
		return clock;
	}

	public void setClock(BatchClock clock)
	{
		this.clock = clock;
	}

	public ExtractorHolder getExtractor()
	{
		return extractor;
	}

	public void setExtractor(ExtractorHolder extractor)
	{
		this.extractor = extractor;
	}

	public TransformerHolder getTransformer()
	{
		return transformer;
	}

	public void setTransformer(TransformerHolder transformer)
	{
		this.transformer = transformer;
	}

	public LoaderHolder getLoader()
	{
		return loader;
	}

	public void setLoader(LoaderHolder loader)
	{
		this.loader = loader;
	}

	public RuleGroup getRuleGroup()
	{
		return ruleGroup;
	}

	public void setRuleGroup(RuleGroup ruleGroup)
	{
		this.ruleGroup = ruleGroup;
	}
}
