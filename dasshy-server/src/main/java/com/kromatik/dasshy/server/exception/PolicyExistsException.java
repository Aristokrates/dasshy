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

import com.kromatik.dasshy.core.exception.EngineException;

import javax.ws.rs.core.Response;

/**
 * Exception thrown when a given policy already exists
 * and we are trying to create a new one with the same identifier
 */
public class PolicyExistsException extends EngineException
{
	/**
	 * Default constructor
	 *
	 * @param message   message
	 * @param exception underlying cause
	 */
	public PolicyExistsException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	/**
	 * Message only constructor
	 *
	 * @param message message
	 */
	public PolicyExistsException(final String message)
	{
		this(message, null);
	}

	@Override
	public int getStatus()
	{
		return Response.Status.CONFLICT.getStatusCode();
	}
}
