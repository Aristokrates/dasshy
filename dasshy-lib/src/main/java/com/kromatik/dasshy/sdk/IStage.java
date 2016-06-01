package com.kromatik.dasshy.sdk;

import java.util.List;

/**
 * Stage of the ETL execution
 */
public interface IStage
{
	/**
	 * Initializes the stage at the beginning of its execution
	 *
	 * @param runtimeContext running context
	 * @param configuration stage configuration
	 */
	void init(final RuntimeContext runtimeContext, final StageConfiguration configuration);

	/**
	 * Cleans the stage at the end of its execution
	 *
	 * @param runtimeContext context
	 */
	void clean(final RuntimeContext runtimeContext);

	/**
	 * Returns a list of attribute definitions for the stage
	 *
	 * @return attribute definitions. empty list if none.
	 */
	List<StageAttribute> getAttributeDefinitions();
}
