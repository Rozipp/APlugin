package ua.rozipp.example.remotesession;

import ua.rozipp.abstractplugin.main.AException;
import ua.rozipp.abstractplugin.main.APlugin;
import ua.rozipp.example.main.ExamplePlugin;
import ua.rozipp.example.main.Static;

import java.io.*;
import java.net.*;

public class ServerListenerThread implements Runnable {

	public static ServerListenerThread startServerListenerThread(APlugin plugin, String hostname, int port) throws AException {
		ServerListenerThread serverThread;
		try {
			if (hostname.equals("0.0.0.0")) {
				serverThread = new ServerListenerThread(plugin, new InetSocketAddress(port));
			} else {
				serverThread = new ServerListenerThread(plugin, new InetSocketAddress(hostname, port));
			}
			new Thread(serverThread).start();
			ExamplePlugin.getPlugin().getLogger().info("ThreadListener Started");
		} catch (Exception e) {
			throw new AException("Failed to start ThreadListener");
		}
		return serverThread;
	}

	public ServerSocket serverSocket;

	public SocketAddress bindAddress;

	public boolean running = true;

	private final APlugin plugin;

	public ServerListenerThread(APlugin plugin, SocketAddress bindAddress) throws IOException {
		this.plugin = plugin;
		this.bindAddress = bindAddress;
		serverSocket = new ServerSocket();
		serverSocket.setReuseAddress(true);
		serverSocket.bind(bindAddress);
	}

	public void run() {
		while (running) {
			try {
				Socket newConnection = serverSocket.accept();
				if (!running) return;
				Static.handleConnection(new RemoteSession(newConnection));
			} catch (Exception e) {
				// if the server thread is still running raise an error
				if (running) {
					plugin.getLogger().warning("Error creating new connection");
					e.printStackTrace();
				}
			}
		}
		try {
			serverSocket.close();
		} catch (Exception e) {
			plugin.getLogger().warning("Error closing server socket");
			e.printStackTrace();
		}
	}
}
