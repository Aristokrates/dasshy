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
	 * @param file file input stream
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
	 * @param type plugin type
	 * @param identifier plugin id
	 * @param plugin plugin info
	 */
	void create(final TStageType type, final String identifier, final TStagePlugin plugin);

	/**
	 * Updates a plugin
	 *
	 * @param type plugin type
	 * @param identifier plugin id
	 * @param plugin plugin info
	 */
	void update(final TStageType type, final String identifier, final TStagePlugin plugin);

	/**
	 * Deletes a plugin
	 *
	 * @param type plugin type
	 * @param identifier plugin id
	 */
	void delete(final TStageType type, String identifier);

	/**
	 * Gets the stage plugin
	 *
	 * @param type plugin type
	 * @param identifier plugin id
	 *
	 * @return plugin info
	 */
	TStagePlugin getByTypeAndId(final TStageType type, final String identifier);

	/**
	 * Get all plugins for the given type
	 *
	 * @param type plugin type
	 *
	 * @return list of plugins
	 */
	Collection<TStagePlugin> getByType(final TStageType type);
}
