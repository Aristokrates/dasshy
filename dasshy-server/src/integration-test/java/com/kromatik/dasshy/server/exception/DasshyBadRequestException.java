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
