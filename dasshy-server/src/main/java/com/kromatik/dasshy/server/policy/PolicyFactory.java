package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.server.streaming.BatchClock;
import com.kromatik.dasshy.thrift.model.TExtractor;
import com.kromatik.dasshy.thrift.model.TLoader;
import com.kromatik.dasshy.thrift.model.TPolicy;
import com.kromatik.dasshy.thrift.model.TTransformer;

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
	 * Builds batch clock from the policy model
	 *
	 * @param policyModel policy model
	 *
	 * @return batch clock
	 */
	BatchClock buildBatchClock(final TPolicy policyModel);

	/**
	 * Builds an extractor instance from its model
	 *
	 * @param extractorModel extractor model
	 *
	 * @return extractor holder
	 */
	ExtractorHolder buildExtractor(final TExtractor extractorModel);

	/**
	 * Builds an transformer instance from its model
	 *
	 * @param transformerModel transformer model
	 *
	 * @return transformer holder
	 */
	TransformerHolder buildTransformer(final TTransformer transformerModel);

	/**
	 * Builds a loader instance from its model
	 *
	 * @param loaderModel loader model
	 *
	 * @return loader holder
	 */
	LoaderHolder buildLoader(final TLoader loaderModel);
}
