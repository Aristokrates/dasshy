package com.kromatik.dasshy.server.dao;

import com.kromatik.dasshy.thrift.model.TStagePlugin;
import com.kromatik.dasshy.thrift.model.TStageType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * In-memory plugin dao
 */
public class InMemoryStagePluginDao implements StagePluginDao
{

	@Override
	public void saveJar(TStagePlugin plugin, InputStream file)
	{

	}

	@Override
	public void dropJar(TStagePlugin plugin)
	{

	}

	@Override
	public void create(TStageType type, String identifier, TStagePlugin plugin)
	{

	}

	@Override
	public void update(TStageType type, String identifier, TStagePlugin plugin)
	{

	}

	@Override
	public void delete(TStageType type, String identifier)
	{

	}

	@Override
	public TStagePlugin getByTypeAndId(TStageType type, String identifier)
	{
		return null;
	}

	@Override
	public Collection<TStagePlugin> getByType(TStageType type)
	{
		return new ArrayList<>();
	}
}
