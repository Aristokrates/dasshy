/**
 * Dasshy - Real time Streaming and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions
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
