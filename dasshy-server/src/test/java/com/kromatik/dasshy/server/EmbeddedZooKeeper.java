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
	private static final String 	HOST = "localhost";

	private static final int 		TICK_TIME = 500;

	private static final int 		MAXCC = 10;

	private final int 				port;

	private ZooKeeperServer 		zooKeeperServer;

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
