package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.server.spark.BatchClock;
import com.kromatik.dasshy.thrift.model.TPolicy;
import com.kromatik.dasshy.thrift.model.TStage;

/**
 * Factory for building policy components
 */
public interface PolicyFactory
{

	/**
	 * Build a policy instance out of its model
	 *
	 * @param policyModel policy model
	 *
	 * @return policy instance
	 */
	Policy buildPolicy(final TPolicy policyModel);

	/**
	 * Builds streaming clock from the policy model
	 *
	 * @param policyModel policy model
	 *
	 * @return streaming clock
	 */
	BatchClock buildStreamingClock(final TPolicy policyModel);

	/**
	 * Builds an extractor instance from its model
	 *
	 * @param extractorModel extractor model
	 *
	 * @return extractor holder
	 */
	ExtractorHolder buildExtractor(final TStage extractorModel);

	/**
	 * Builds an transformer instance from its model
	 *
	 * @param transformerModel transformer model
	 *
	 * @return transformer holder
	 */
	TransformerHolder buildTransformer(final TStage transformerModel);

	/**
	 * Builds a loader instance from its model
	 *
	 * @param loaderModel loader model
	 *
	 * @return loader holder
	 */
	LoaderHolder buildLoader(final TStage loaderModel);
}
