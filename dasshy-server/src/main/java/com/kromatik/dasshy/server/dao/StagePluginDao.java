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
package com.kromatik.dasshy.server.dao;

import com.kromatik.dasshy.thrift.model.TStagePlugin;
import com.kromatik.dasshy.thrift.model.TStageType;

import java.io.InputStream;
import java.util.Collection;

/**
 * Stage plugin dao
 */
public interface StagePluginDao
{
	/**
	 * Saves the plugin jar file
	 *
	 * @param plugin stage plugin info
	 * @param file   file input stream
	 */
	void saveJar(final TStagePlugin plugin, final InputStream file);

	/**
	 * Drop the jar file for the given plugin
	 *
	 * @param plugin stage plugin info
	 */
	void dropJar(final TStagePlugin plugin);

	/**
	 * Creates a stage plugin
	 *
	 * @param type       plugin type
	 * @param identifier plugin id
	 * @param plugin     plugin info
	 */
	void create(final TStageType type, final String identifier, final TStagePlugin plugin);

	/**
	 * Updates a plugin
	 *
	 * @param type       plugin type
	 * @param identifier plugin id
	 * @param plugin     plugin info
	 */
	void update(final TStageType type, final String identifier, final TStagePlugin plugin);

	/**
	 * Deletes a plugin
	 *
	 * @param type       plugin type
	 * @param identifier plugin id
	 */
	void delete(final TStageType type, String identifier);

	/**
	 * Gets the stage plugin
	 *
	 * @param type       plugin type
	 * @param identifier plugin id
	 * @return plugin info
	 */
	TStagePlugin getByTypeAndId(final TStageType type, final String identifier);

	/**
	 * Get all plugins for the given type
	 *
	 * @param type plugin type
	 * @return list of plugins
	 */
	Collection<TStagePlugin> getByType(final TStageType type);
}
