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
package org.eclipse.swtbot.generator.client.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swtbot.generator.client.Recorder;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * ImportClientViewCodeHandler moves generated code to the cursor position of
 * the active editor and then clears the generated code buffer.
 */
public class ImportClientCodeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		addTextToEditorAtCursor(Recorder.INSTANCE.getDocumentText());

		Recorder.INSTANCE.clearDocument();

		return null;
	}

	/**
	 * Adds a string to the currently selected location within a text editor.
	 *
	 * @param text
	 *            The text to insert.
	 */
	private void addTextToEditorAtCursor(String text) throws ExecutionException {
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = activeWindow.getActivePage();
		IEditorPart editorPart = page.getActiveEditor();

		if (editorPart instanceof AbstractTextEditor == false) {
			throw new ExecutionException("Code can only be inserted within a text editor");
		}

		ITextEditor editor = (ITextEditor) editorPart;
		IDocumentProvider documentProvider = editor.getDocumentProvider();
		IDocument document = documentProvider.getDocument(editor.getEditorInput());

		ISelection selection = editor.getSelectionProvider().getSelection();
		if (selection instanceof TextSelection == false) {
			throw new ExecutionException("Could not find text selection");
		}

		int offset = ((TextSelection) selection).getOffset();
		try {
			document.replace(offset, 0, text);
		} catch (BadLocationException e) {
			throw new ExecutionException("Could not insert code: " + e.getMessage());
		}
	}
}
