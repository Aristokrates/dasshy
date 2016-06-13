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
	private TPolicy											model;

	/** batch clock for this policy determining how many times this policy will be executed */
	private BatchClock clock;

	/** extractor holder: consisting of instance and its configuration */
	private ExtractorHolder									extractor;

	/** transformer tuple: consisting of instance and its configuration */
	private TransformerHolder								transformer;

	/** loader tuple: consisting of instance and its configuration */
	private LoaderHolder									loader;

	/** rule group */
	private RuleGroup										ruleGroup;

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
