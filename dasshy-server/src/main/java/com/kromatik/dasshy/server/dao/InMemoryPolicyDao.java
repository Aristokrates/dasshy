package com.kromatik.dasshy.server.dao;

import com.kromatik.dasshy.thrift.model.TPolicy;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory policy dao
 */
public class InMemoryPolicyDao implements PolicyDao
{

	private Map<String, TPolicy> memoryStore = new ConcurrentHashMap<>();

	@Override
	public void create(TPolicy policy)
	{
		memoryStore.put(policy.getId(), policy);
	}

	@Override
	public void update(TPolicy policy)
	{
		memoryStore.put(policy.getId(), policy);
	}

	@Override
	public void delete(TPolicy policy)
	{
		memoryStore.remove(policy.getId());
	}

	@Override
	public TPolicy get(String id)
	{
		return memoryStore.get(id);
	}

	@Override
	public boolean exists(String id)
	{
		return memoryStore.containsKey(id);
	}

	@Override
	public Collection<TPolicy> list()
	{
		return memoryStore.values();
	}
}
