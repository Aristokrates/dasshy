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
package com.kromatik.dasshy.server.command;

/**
 * Abstract command wrapper
 */
public abstract class DasshyCommand<R>
{
	/** command key */
	private	String	commandKey;

	/**
	 * Default command constructor
	 *
	 * @param commandKey command key
	 */
	public DasshyCommand(final String commandKey)
	{
		this.commandKey = commandKey;
	}

	/**
	 * Run the command
	 *
	 * @return result of command execution
	 *
	 * @throws Exception
	 */
	public abstract R run() throws Exception;
}
