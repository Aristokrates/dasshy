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
package com.kromatik.dasshy.core.engine.impl;

import com.kromatik.dasshy.core.config.IEngineConfiguration;
import com.kromatik.dasshy.core.engine.IEngineRuntime;
import com.kromatik.dasshy.core.engine.IEngineRuntimeFactory;

/**
 * Default engine runtime factory
 */
public class DefaultEngineRuntimeFactory<T extends IEngineConfiguration> implements IEngineRuntimeFactory<T>
{

	/**
	 * @see IEngineRuntimeFactory#build(IEngineConfiguration)
	 */
	@Override
	public IEngineRuntime build(final T config)
	{
		return new EngineRuntime();
	}
}
