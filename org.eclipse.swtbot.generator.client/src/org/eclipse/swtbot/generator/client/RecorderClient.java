/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Isaac Arvestad (Ericsson) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;

/**
 * RecorderClient is a client for RecorderServer and collects incoming generated
 * code from a RecorderServer running on localhost. RecorderClient runs on a new
 * thread.
 */
public class RecorderClient extends Thread {

	/**
	 * Refresh time in milliseconds when attempting to connect to host.
	 */
	private static final int REFRESH_TIME = 1000;

	private List<RecorderClientCodeListener> codeListeners;
	private List<RecorderClientStatusListener> statusListeners;
	private int port;

	private Socket socket;

	/**
	 * Creates a new RecorderClient.
	 *
	 * @param port
	 *            The port to connect on.
	 */
	public RecorderClient(List<RecorderClientCodeListener> codeListeners,
			List<RecorderClientStatusListener> statusListeners, int port) {
		this.codeListeners = codeListeners;
		this.statusListeners = statusListeners;
		this.port = port;
	}

	/**
	 * Attempts to close the the socket of the recorder client. This is
	 * necessary to interrupt the thread while the thread is stuck in reading
	 * the next line of the input stream.
	 *
	 * If the socket is null or already closed, do nothing and return.
	 */
	public void closeSocket() {
		if (socket == null || socket.isClosed()) {
			return;
		}

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		socket = connect("localhost", port);

		if (socket == null) {
			for (RecorderClientStatusListener listener : statusListeners) {
				listener.connectionEnded();
			}
			throw new RuntimeException("Could not create socket for client");
		}

		for (RecorderClientStatusListener listener : statusListeners) {
			listener.connectionStarted();
		}

		BufferedReader in;
		try {
			in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			String line;

			while ((line = in.readLine()) != null && !interrupted()) {
				for (RecorderClientCodeListener listener : codeListeners) {
					listener.codeGenerated(line);
				}
			}

			for (RecorderClientStatusListener listener : statusListeners) {
				listener.connectionEnded();
			}

			socket.close();
		} catch (IOException e) {
			// Client was shut down through closeSocket() and readLine threw
			// an exception.
			return;
		}
	}

	/**
	 * Attempts to connect to host. If the host is not found, wait
	 * <code>REFRESH_TIME</code> milliseconds and try again. Once connected,
	 * return.
	 *
	 * @param host
	 *            The host name to connect to.
	 * @param port
	 *            The port to connect to.
	 * @return The host socket.
	 */
	private Socket connect(String host, int port) {
		Socket socket;

		while (true) {
			try {
				socket = new Socket(host, port);
				return socket;
			} catch (ConnectException e) {
				try {
					Thread.sleep(REFRESH_TIME);
				} catch (InterruptedException e1) {
					return null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getPort() {
		return port;
	}
}
