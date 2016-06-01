package com.kromatik.dasshy.server.service;

import com.kromatik.dasshy.server.dao.StagePluginDao;
import com.kromatik.dasshy.server.exception.StagePluginNotFoundException;
import com.kromatik.dasshy.thrift.model.TStagePlugin;
import com.kromatik.dasshy.thrift.model.TStageType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Stage plugins service
 */
public class StagePluginService
{
	/** stage plugin dao */
	private final StagePluginDao						dao;

	/** available plugins */
	private Map<TStageType, Map<String, TStagePlugin>>	stagePlugins;

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
	 * @param type plugin type
	 * @param identifier plugin identifier
	 */
	public void registerStagePlugin(final TStageType type, final String identifier, final TStagePlugin plugin)
	{
		dao.create(type, identifier, plugin);
	}

	/**
	 * Updates a given stage plugin
	 *
	 * @param type plugin type
	 * @param identifier plugin identifier
	 * @param plugin plugin
	 */
	public void updateStagePlugin(final TStageType type, final String identifier, final TStagePlugin plugin)
	{
		dao.update(type, identifier, plugin);
	}

	/**
	 * Deletes a stage plugin
	 *
	 * @param type plugin type
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
	 *
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
	 * @param type plugin type
	 * @param identifier plugin identifier
	 *
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
			throw new StagePluginNotFoundException(
							"Could not find plugin of type: " + type +
											" with identifier: " + identifier);
		}

		return savedPlugin;
	}

	/**
	 * Fetch the default plugins
	 */
	private final void fetchPlugins()
	{
		this.stagePlugins = new HashMap<>();

		for (final DefaultStagePlugin defaultPlugin : DefaultStagePlugin.values())
		{
			Map<String, TStagePlugin> pluginByIdMap = stagePlugins.get(defaultPlugin.getType());
			if (pluginByIdMap == null)
			{
				pluginByIdMap = new HashMap<>();
			}

			pluginByIdMap.put(defaultPlugin.getIdentifier(), new TStagePlugin(
							defaultPlugin.getType(), defaultPlugin.getIdentifier(),
							defaultPlugin.getClasspath(), defaultPlugin.getDescription()));

			stagePlugins.put(defaultPlugin.getType(), pluginByIdMap);
		}
	}
}
