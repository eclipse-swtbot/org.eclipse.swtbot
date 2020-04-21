/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Isaac Arvestad (Ericsson) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * Recorder is a singleton which keeps track of recorder state information and
 * the generated code received from the server.
 */
public enum Recorder implements RecorderClientCodeListener, RecorderClientStatusListener {
	INSTANCE;

	private RecorderClient recorderClient;
	private List<RecorderClientCodeListener> codeListeners;
	private List<RecorderClientStatusListener> statusListeners;

	private boolean isInitialized = false;
	private boolean isRecording;
	private boolean isInsertingDirectlyInEditor;
	private ConnectionState connectionState;
	private IDocument document;
	private IMethod selectedMethod;
	private IDocument selectedMethodDocument;

	/**
	 * Initialize recorder.
	 */
	public void initialize() {
		isInitialized = true;

		codeListeners = new ArrayList<RecorderClientCodeListener>();
		statusListeners = new ArrayList<RecorderClientStatusListener>();
		codeListeners.add(this);
		statusListeners.add(this);

		document = new Document();

		reset();
	}

	/**
	 * Reset recording state.
	 */
	public void reset() {
		isRecording = false;
		isInsertingDirectlyInEditor = false;
		selectedMethod = null;
		selectedMethodDocument = null;
		connectionState = ConnectionState.DISCONNECTED;
	}

	/**
	 * Start a recorder client session.
	 */
	public void startRecorderClient(int port) {
		interruptRecorderClient();

		connectionState = ConnectionState.CONNECTING;
		recorderClient = new RecorderClient(codeListeners, statusListeners, port);
		recorderClient.start();
	}

	/**
	 * Interrupt a recorder client session and reset recording state.
	 */
	public void interruptRecorderClient() {
		if (recorderClient != null) {
			recorderClient.interrupt();
			recorderClient.closeSocket();
			recorderClient = null;
		}

		reset();
	}

	/**
	 * Takes care of new code. If a method is selected and
	 * <code>isInsertingDirectlyInEditor<code> is true, add code directly to
	 * editor. Otherwise add it to the recorder view document.
	 */
	@Override
	public void codeGenerated(String code) {
		if (isRecording == false) {
			return;
		}

		if (isInsertingDirectlyInEditor && selectedMethod != null && selectedMethodDocument != null) {
			insertInEditor(code);
		} else {
			insertInView(code);
		}
	}

	@Override
	public void connectionStarted() {
		connectionState = ConnectionState.CONNECTED;
	}

	@Override
	public void connectionEnded() {
		connectionState = ConnectionState.DISCONNECTED;

		reset();
	}

	/**
	 * Appends a row of code to the document contained in the recorder view.
	 *
	 * @param code
	 *            The code to append.
	 */
	private void insertInView(final String code) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if (document.get().length() == 0) {
					document.set(code + ";");
				} else {
					document.set(document.get() + "\n" + code + ";");
				}
			}
		});
	}

	/**
	 * Adds a new line of code to the currently selected method and the editor
	 * which contains this method.
	 *
	 * @param code
	 *            The code to append.
	 */
	private void insertInEditor(String code) {
		ICompilationUnit methodCompilationUnit = selectedMethod.getCompilationUnit();
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(methodCompilationUnit);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		ASTNode rootNode = parser.createAST(null);
		CompilationUnit compilationUnit = (CompilationUnit) rootNode;

		// Create a visitor which finds all method declarations
		MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
		compilationUnit.accept(methodDeclarationVisitor);

		// Search for the method declaration corresponding to selectedMethod
		MethodDeclaration method = methodDeclarationVisitor.findMethodDeclaration(selectedMethod);

		final ASTRewrite rewrite = ASTRewrite.create(rootNode.getAST());
		ListRewrite listRewrite = rewrite.getListRewrite(method.getBody(), Block.STATEMENTS_PROPERTY);
		Statement statement = (Statement) rewrite.createStringPlaceholder(code + ";", ASTNode.EMPTY_STATEMENT);
		listRewrite.insertLast(statement, null);

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					TextEdit edits = rewrite.rewriteAST();
					try {
						edits.apply(selectedMethodDocument);
					} catch (MalformedTreeException e) {
						e.printStackTrace();
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				} catch (JavaModelException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			};
		});
	}

	/**
	 * MethodDeclarationVisitor store all MethodDeclarations it can find and
	 * provides functionality for finding a specific MethodDeclaration with a
	 * corresponding IMethod.
	 */
	private class MethodDeclarationVisitor extends ASTVisitor {
		private List<MethodDeclaration> methodDeclarations = new ArrayList<MethodDeclaration>();

		@Override
		public boolean visit(MethodDeclaration node) {
			methodDeclarations.add(node);
			return super.visit(node);
		}

		/**
		 * Returns the MethodDeclaration from a corresponding IMethod.
		 *
		 * @param method
		 *            The IMethod to search with.
		 * @return The first corresponding MethodDeclaration found, or null if
		 *         no MethodDeclaration can be found.
		 */
		public MethodDeclaration findMethodDeclaration(IMethod method) {
			for (MethodDeclaration methodDeclaration : methodDeclarations) {
				if (methodDeclaration.resolveBinding().getJavaElement().equals(method)) {
					return methodDeclaration;
				}
			}

			return null;
		}
	}

	/**
	 * Determines if UI which is related to code in the open editor needs to be
	 * updated. The method selection viewer should be updated if the document is
	 * closed or if a method is removed/added.
	 * 
	 * @param event
	 *            The received ElementChangedEvent.
	 * @return True if method selection viewer should be updated and false if
	 *         not.
	 */
	public boolean shouldUpdateMethodSelectionViewer(ElementChangedEvent event) {
		IJavaElementDelta delta = event.getDelta();

		List<IJavaElementDelta> children = getAffectedLeafDeltas(delta);
		for (IJavaElementDelta child : children) {
			if (child.getElement() instanceof IMethod) {
				return true;
			} else if (child.getElement() instanceof ICompilationUnit) {
				if (child.getKind() == IJavaElementDelta.CHANGED) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Finds and returns the leaves of the change tree.
	 * 
	 * @param delta
	 *            The root to be iterated through.
	 * @return The leaf nodes.
	 */
	private List<IJavaElementDelta> getAffectedLeafDeltas(IJavaElementDelta delta) {
		List<IJavaElementDelta> leaves = new ArrayList<IJavaElementDelta>();

		return getAffectedLeafDeltasRecursively(delta, leaves);
	}

	/**
	 * Helper method for <code>getAffectedLeafDeltas</code> which recursively
	 * iterates over a IJavaElementDelta and returns the leaf nodes of the
	 * change tree.
	 * 
	 * @param delta
	 *            The root to be iterated through.
	 * @param leaves
	 *            The leaf nodes already found.
	 * @return The leaf nodes found.
	 */
	private List<IJavaElementDelta> getAffectedLeafDeltasRecursively(IJavaElementDelta delta,
			List<IJavaElementDelta> leaves) {
		for (IJavaElementDelta childDelta : delta.getAffectedChildren()) {
			if (childDelta.getAffectedChildren().length == 0) {
				// Found a leaf!
				leaves.add(childDelta);
			} else {
				getAffectedLeafDeltasRecursively(childDelta, leaves);
			}
		}

		return leaves;
	}

	/**
	 * ConnectionState describes the various possible connection states between
	 * the recorder client and server.
	 */
	public enum ConnectionState {
		CONNECTED, CONNECTING, DISCONNECTED;
	}

	public boolean isInsertingDirectlyInEditor() {
		return isInsertingDirectlyInEditor;
	}

	public void setInsertingDirectlyInEditor(boolean isInsertingDirectlyInEditor) {
		this.isInsertingDirectlyInEditor = isInsertingDirectlyInEditor;
	}

	public IMethod getSelectedMethod() {
		return selectedMethod;
	}

	/**
	 * Sets the selected method. It is important that this method is part of the
	 * selected method document.
	 *
	 * @param selectedMethod
	 *            The selected method.
	 */
	public void setSelectedMethod(IMethod selectedMethod) {
		this.selectedMethod = selectedMethod;
	}

	public IDocument getSelectedMethodDocument() {
		return selectedMethodDocument;
	}

	/**
	 * Sets the seletctedMethodDocument. It is important that this document
	 * contains the selected method.
	 * 
	 * @param selectedMethodDocument
	 *            The document that the selected method resides in.
	 */
	public void setSelectedMethodDocument(IDocument selectedMethodDocument) {
		this.selectedMethodDocument = selectedMethodDocument;
	}

	/**
	 * Returns the text contained in the document. Use from UI thread.
	 *
	 * @return The text contained in the document.
	 */
	public String getDocumentText() {
		return document.get();
	}

	/**
	 * Clears the document by setting the contents to an empty String. Use from
	 * UI thread.
	 */
	public void clearDocument() {
		document.set("");
	}

	/**
	 * Returns the recorder document which contains recorded code. Use from UI
	 * thread.
	 *
	 * @return The document.
	 */
	public IDocument getDocument() {
		return document;
	}

	public boolean isRecording() {
		return isRecording;
	}

	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public ConnectionState getConnectionState() {
		return connectionState;
	}

	public int getPort() {
		return recorderClient.getPort();
	}

	/**
	 * Add a code listener to the list of code listeners.
	 *
	 * @param listener
	 *            The listener to add.
	 * @return Specified by {@link Collection#add}
	 */
	public boolean addCodeListener(RecorderClientCodeListener listener) {
		return codeListeners.add(listener);
	}

	/**
	 * Removes the first occurrence of the specified listener from the list of
	 * code listeners.
	 *
	 * @param listener
	 *            The listener to remove.
	 * @return Specified by {@link Collection#remove(Object)}
	 */
	public boolean removeCodeListener(RecorderClientCodeListener listener) {
		return codeListeners.remove(listener);
	}

	/**
	 * Add a status listener to the list of status listeners.
	 *
	 * @param listener
	 *            The listener to add.
	 * @return Specified by {@link Collection#add}
	 */
	public boolean addStatusListener(RecorderClientStatusListener listener) {
		return statusListeners.add(listener);
	}

	/**
	 * Removes the first occurrence of the specified listener from the list of
	 * code listeners.
	 *
	 * @param listener
	 *            The listener to remove.
	 * @return Specified by {@link Collection#remove(Object)}
	 */
	public boolean removeStatusListener(RecorderClientStatusListener listener) {
		return statusListeners.remove(listener);
	}
}
