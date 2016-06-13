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
