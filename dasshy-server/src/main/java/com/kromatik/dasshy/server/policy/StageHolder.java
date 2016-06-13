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
package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.sdk.StageConfiguration;
import com.kromatik.dasshy.sdk.IStage;

/**
 * Holds an execution stage instance and its configuration
 *
 * @param <S> execution stage
 * @param <C> its configuration
 */
public abstract class StageHolder<S extends IStage, C extends StageConfiguration>
{

	private final S stage;

	private final C configuration;

	/**
	 * Default constructor
	 *
	 * @param stage         stage
	 * @param configuration configuration
	 */
	public StageHolder(final S stage, final C configuration)
	{
		this.stage = stage;
		this.configuration = configuration;
	}

	public S stage()
	{
		return stage;
	}

	public C configuration()
	{
		return configuration;
	}
}
