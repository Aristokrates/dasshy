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
package com.kromatik.dasshy.core.engine;

import com.kromatik.dasshy.core.config.IEngineConfiguration;

/**
 *  The bundles represents a piece of code executed as part of the engine initialization.
 *  Any custom initialization needs to go in its own bundle
 *
 *  @param <T> engine configuration
 */
public interface IEngineBundle<T extends IEngineConfiguration>
{
	/**
	 * Run the bundle
	 *
	 * @param configuration engine's configuration
	 * @param env engine's environment
	 */
	void run(final T configuration, final IEngineRuntime env);

}
