package com.kromatik.dasshy.server.exception;

import com.kromatik.dasshy.thrift.model.TError;

/**
 * This exception signalizes that exception occurred on server side.
 */
public class DasshyServerErrorException extends RuntimeException
{

	private Integer status;

	private TError error;

	public DasshyServerErrorException(final String message)
	{
		super(message);
	}

	public DasshyServerErrorException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public DasshyServerErrorException(final Integer status, final TError error)
	{
		super(error != null ? error.getMessage() : "");
		this.status = status;
		this.error = error;
	}

	public Integer getStatus()
	{
		return status;
	}

	public TError getError()
	{
		return error;
	}

}
