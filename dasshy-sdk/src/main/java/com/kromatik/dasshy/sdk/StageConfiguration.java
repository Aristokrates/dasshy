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
package com.kromatik.dasshy.sdk;

import java.util.Map;

/**
 * Configuration class for any execution stage
 */
public class StageConfiguration
{
	/** values */
	private final Map<String, String>			values;

	/**
	 * Default constructor
	 *
	 * @param values configuration values
	 */
	public StageConfiguration(final Map<String, String> values)
	{
		this.values = values;
	}

	/**
	 * Configuration map
	 *
	 * @return map
	 */
	public Map<String, String> getValues()
	{
		return values;
	}
}
