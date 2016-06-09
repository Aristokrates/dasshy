package com.kromatik.dasshy.server.exception;

import com.kromatik.dasshy.thrift.model.TError;

/**
 * This exception signalizes that exception is on the client side i.e the client has sent a bad request
 */
public class DasshyBadRequestException extends RuntimeException
{
	private Integer	status;

	private TError	error;

	public DasshyBadRequestException(final String message)
	{
		super(message);
	}

	public DasshyBadRequestException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public DasshyBadRequestException(final Integer status, final TError error)
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
