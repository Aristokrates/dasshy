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
package com.kromatik.dasshy.server.service;

import com.kromatik.dasshy.server.dao.StagePluginDao;
import com.kromatik.dasshy.server.exception.StagePluginNotFoundException;
import com.kromatik.dasshy.thrift.model.TStagePlugin;
import com.kromatik.dasshy.thrift.model.TStageType;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Stage plugins service
 */
public class StagePluginService
{
	/** stage plugin dao */
	private final StagePluginDao dao;

	/** available plugins */
	private EnumMap<TStageType, Map<String, TStagePlugin>> stagePlugins;

	/**
	 * Default constructor
	 *
	 * @param dao stage plugin dao
	 */
	public StagePluginService(final StagePluginDao dao)
	{
		this.dao = dao;
		fetchPlugins();
	}

	/**
	 * Registers a new plugin
	 *
	 * @param type       plugin type
	 * @param identifier plugin identifier
	 */
	public void registerStagePlugin(final TStageType type, final String identifier, final TStagePlugin plugin)
	{
		dao.create(type, identifier, plugin);
	}

	/**
	 * Updates a given stage plugin
	 *
	 * @param type       plugin type
	 * @param identifier plugin identifier
	 * @param plugin     plugin
	 */
	public void updateStagePlugin(final TStageType type, final String identifier, final TStagePlugin plugin)
	{
		dao.update(type, identifier, plugin);
	}

	/**
	 * Deletes a stage plugin
	 *
	 * @param type       plugin type
	 * @param identifier plugin identifier
	 */
	public void deleteStagePlugin(final TStageType type, final String identifier)
	{
		dao.delete(type, identifier);
	}

	/**
	 * List stage plugins by type
	 *
	 * @param type type
	 * @return list of plugins
	 */
	public Collection<TStagePlugin> listStagePluginsByType(final TStageType type)
	{
		Collection<TStagePlugin> allPluginsByType = dao.getByType(type);

		allPluginsByType.addAll(stagePlugins.get(type).values());
		return allPluginsByType;
	}

	/**
	 * Find plugin by type and id
	 *
	 * @param type       plugin type
	 * @param identifier plugin identifier
	 * @return a stage plugin
	 */
	public TStagePlugin getStagePluginByTypeAndId(final TStageType type, final String identifier)
	{
		final TStagePlugin defaultPlugin = stagePlugins.get(type).get(identifier);

		if (defaultPlugin != null)
		{
			return defaultPlugin;
		}

		final TStagePlugin savedPlugin = dao.getByTypeAndId(type, identifier);
		if (savedPlugin == null)
		{
			throw new StagePluginNotFoundException("Could not find plugin of type: " + type +
							" with identifier: " + identifier);
		}

		return savedPlugin;
	}

	/**
	 * Fetch the default plugins
	 */
	private final void fetchPlugins()
	{
		this.stagePlugins = new EnumMap<>(TStageType.class);

		for (final DefaultStagePlugin defaultPlugin : DefaultStagePlugin.values())
		{
			Map<String, TStagePlugin> pluginByIdMap = stagePlugins.get(defaultPlugin.getType());
			if (pluginByIdMap == null)
			{
				pluginByIdMap = new HashMap<>();
			}

			pluginByIdMap.put(defaultPlugin.getIdentifier(),
							new TStagePlugin(defaultPlugin.getType(), defaultPlugin.getIdentifier(),
											defaultPlugin.getClasspath(), defaultPlugin.getDescription()));

			stagePlugins.put(defaultPlugin.getType(), pluginByIdMap);
		}
	}
}
