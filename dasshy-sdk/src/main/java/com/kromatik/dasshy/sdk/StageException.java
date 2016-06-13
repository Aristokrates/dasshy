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
package com.kromatik.dasshy.sdk;

/**
 * Exception occurred within a stage
 */
public class StageException extends RuntimeException
{

	/**
	 * Default constructor
	 */
	public StageException()
	{
		// no-op
	}

	/**
	 * Exception with message
	 *
	 * @param message message
	 */
	public StageException(final String message)
	{
		super(message);
	}

	/**
	 * Exception with message and cause
	 *
	 * @param message message
	 * @param cause   a cause
	 */
	public StageException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Exception with a cause
	 *
	 * @param cause a cause
	 */
	public StageException(final Throwable cause)
	{
		super(cause);
	}
}
