package com.kromatik.dasshy.server.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import java.io.Closeable;

/**
 * Zookeeper client that wraps the Curator Framework. There should be only one instance of ZookeeperClient
 * object per Zookeeper cluster and that should be shared by other instances.
 */
public interface IZookeeperClient extends Closeable
{
	/**
	 * @return underlying Curator Framework Client
	 */
	CuratorFramework	getCuratorFramework();
}
