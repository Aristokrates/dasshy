package com.kromatik.dasshy.sdk;

/**
 * Stage of the ETL execution
 */
public interface IStage
{
	/**
	 * Initializes the stage at the begining of its execution
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
}
