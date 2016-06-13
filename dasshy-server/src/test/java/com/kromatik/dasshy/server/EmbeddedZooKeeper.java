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
package com.kromatik.dasshy.server;

import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Embedded Zookeeper instance
 */
public class EmbeddedZooKeeper
{
	private static final String HOST = "localhost";

	private static final int TICK_TIME = 500;

	private static final int MAXCC = 10;

	private final int port;

	private ZooKeeperServer zooKeeperServer;

	public EmbeddedZooKeeper()
	{
		port = allocatePort();
	}

	public EmbeddedZooKeeper(final int port)
	{
		this.port = port;
	}

	public String getConnectString()
	{
		return HOST + ':' + port;
	}

	public void start() throws Exception
	{
		final Path snapshotDir = Files.createTempDirectory("zk-snapshot-dir-");
		final Path logDir = Files.createTempDirectory("zk-log-dir-");
		zooKeeperServer = new ZooKeeperServer(snapshotDir.toFile(), logDir.toFile(), TICK_TIME);

		final NIOServerCnxnFactory factory = new NIOServerCnxnFactory();
		factory.configure(new InetSocketAddress(port), MAXCC);
		factory.startup(zooKeeperServer);
	}

	public void close()
	{
		try
		{
			zooKeeperServer.shutdown();
		}
		catch (final Exception ignored)
		{
			// no-op
		}
	}

	private int allocatePort()
	{
		final InetSocketAddress inetSocketAddress = new InetSocketAddress(0);
		try (ServerSocket socket = new ServerSocket())
		{
			socket.bind(inetSocketAddress);
			return socket.getLocalPort();
		}
		catch (final IOException e)
		{
			return -1;
		}
	}
}
