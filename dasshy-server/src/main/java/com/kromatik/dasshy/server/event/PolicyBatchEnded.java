package com.kromatik.dasshy.server.event;

import java.util.List;

/**
 * Batch ended
 */
public class PolicyBatchEnded extends JobEvent
{
	private boolean				success;

	private List<ErrorInfo>		errors;

	private ExtractorInfo		extractorInfo;

	private TransformerInfo		transformerInfo;

	private LoaderInfo			loaderInfo;

	/***
	 * New job event
	 *
	 * @param id        event id
	 * @param jobId     job id
	 * @param timestamp creation timestamp
	 */
	public PolicyBatchEnded(String id, String jobId, Long timestamp)
	{
		super(id, jobId, timestamp, JobEventType.BATCH_ENDED);
	}

	public boolean isSuccess()
	{
		return success;
	}

	public List<ErrorInfo> getErrors()
	{
		return errors;
	}

	public ExtractorInfo getExtractorInfo()
	{
		return extractorInfo;
	}

	public TransformerInfo getTransformerInfo()
	{
		return transformerInfo;
	}

	public LoaderInfo getLoaderInfo()
	{
		return loaderInfo;
	}
}
