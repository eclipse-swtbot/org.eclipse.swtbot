/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Isaac Arvestad (Ericsson) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher.CodeGenerationListener;

/**
 * RecorderServer is a server which prints out SWTBot recorder events to all
 * connected clients.
 */
public class RecorderServer {

	private BotGeneratorEventDispatcher recorder;

	/**
	 * Creates a new RecorderServer with a SWTBot recorder.
	 *
	 * @param recorder
	 *            The recorder.
	 */
	public RecorderServer(BotGeneratorEventDispatcher recorder) {
		this.recorder = recorder;
	}

	/**
	 * Starts the server.
	 *
	 * @param port
	 *            The port the server listens for connections on.
	 */
	public void start(int port) {
		recorder.setRecording(true);

		ConnectionListener connectionListener = new ConnectionListener(port);
		connectionListener.start();
	}

	/**
	 * ConnectionListener listens for collections on a separate thread.
	 */
	private class ConnectionListener extends Thread {

		private int port;

		/**
		 * Creates a ConnectionListener with a certain port.
		 *
		 * @param port
		 *            The port to listen on.
		 */
		public ConnectionListener(int port) {
			this.port = port;
		}

		@Override
		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket(port);

				while (!interrupted()) {
					ConnectionHandler connectionHandler = new ConnectionHandler(serverSocket.accept());
					recorder.addListener(connectionHandler);
				}

				serverSocket.close();
			} catch (Exception e) {
				throw new RuntimeException(
						"Could not start server - There was a problem starting the recorder server. Try restarting using a different port number.");
			}
		}
	}

	/**
	 * ConnectionHandler handles a connection which ConnectionListener has
	 * accepted.
	 */
	private class ConnectionHandler implements CodeGenerationListener {

		private PrintWriter output;

		/**
		 * Creates a new ConnectionHandler.
		 *
		 * @param socket
		 *            The connecting socket.
		 */
		public ConnectionHandler(Socket socket) {
			try {
				output = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Sends any generated code to the client.
		 *
		 * @param code
		 *            The generated code.
		 */
		@Override
		public void handleCodeGenerated(GenerationRule code) {
			for (String text : code.getActions()) {
				output.println(text);
			}
		}
	}
}
