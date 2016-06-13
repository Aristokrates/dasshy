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
package com.kromatik.dasshy.core.engine;

import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.config.IConfigurationFactory;

/**
 * Engine initialization context. Stores common context parameters used for starting the engine
 *
 * @param <T> engine's configuration
 */
public interface IEngineContext<T extends IEngineConfiguration>
{

	/**
	 * Get the factory used for creating the engine's configuration
	 *
	 * @return configuration factory
	 */
	IConfigurationFactory<T> getConfigurationFactory();

	/**
	 * Get the factory used for creating the engine runtime environment
	 *
	 * @return engine runtime factory
	 */
	IEngineRuntimeFactory<T> getEngineRuntimeFactory();
}
