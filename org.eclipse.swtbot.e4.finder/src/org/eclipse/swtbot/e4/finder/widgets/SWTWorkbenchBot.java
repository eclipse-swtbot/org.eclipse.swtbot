/*******************************************************************************
 * Copyright (c) 2014 SWTBot Committers and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Matt Biggs - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.widgets;

import static org.eclipse.swtbot.e4.finder.matchers.WidgetMatcherFactory.withPartId;
import static org.eclipse.swtbot.e4.finder.matchers.WidgetMatcherFactory.withPartName;
import static org.eclipse.swtbot.e4.finder.matchers.WidgetMatcherFactory.withPerspectiveId;
import static org.eclipse.swtbot.e4.finder.matchers.WidgetMatcherFactory.withPerspectiveLabel;
import static org.eclipse.swtbot.e4.finder.waits.Conditions.waitForPart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swtbot.e4.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.e4.finder.waits.WaitForPart;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * SWTWorkbenchBot is a {@link SWTBot} with capabilities for testing Eclipse workbench items like views, editors and
 * perspectives
 * 
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @author Matt biggs - Converted to E4
 * @version $Id$
 */
public class SWTWorkbenchBot extends SWTBot {

	private final IEclipseContext 			context;
	private final WorkbenchContentsFinder	workbenchContentsFinder;

	/**
	 * Constructs a workbench bot
	 */
	public SWTWorkbenchBot(final IEclipseContext context) {
		this.context = context;
		this.workbenchContentsFinder = new WorkbenchContentsFinder(context);
		
		/**
		 * Wait until the IEclipseContext is ready. For some reason if we don't do this as of Kepler SR2 we
		 * get issues thrown when attempting to do things such as switch perspective.
		 */
		this.waitUntil(new IEclipseContextReady());
	}

	/**
	 * Returns the perspective matching the given matcher
	 *
	 * @param matcher the matcher used to find the perspective
	 * @return a perspective matching the matcher
	 * @throws WidgetNotFoundException if the perspective is not found
	 */
	public SWTBotPerspective perspective(final Matcher<?> matcher) {
		final List<MPerspective> perspectives = workbenchContentsFinder.findPerspectives(matcher);
		return new SWTBotPerspective(perspectives.get(0), this);
	}

	/**
	 * Shortcut for perspective(withPerspectiveLabel(label))
	 *
	 * @param label the "human readable" label for the perspective
	 * @return a perspective with the specified <code>label</code>
	 * @see #perspective(Matcher)
	 * @see WidgetMatcherFactory#withPerspectiveLabel(Matcher)
	 */
	public SWTBotPerspective perspectiveByLabel(final String label) {
		return perspective(withPerspectiveLabel(label));
	}

	/**
	 * Shortcut for perspective(perspectiveById(label))
	 *
	 * @param id the perspective id
	 * @return a perspective with the specified <code>label</code>
	 * @see #perspective(Matcher)
	 * @see WidgetMatcherFactory#withPerspectiveId(Matcher)
	 */
	public SWTBotPerspective perspectiveById(final String id) {
		return perspective(withPerspectiveId(id));
	}

	/**
	 * @param matcher Matcher for IPerspectiveDescriptor
	 * @return all available matching perspectives
	 */
	public List<SWTBotPerspective> perspectives(final Matcher<?> matcher) {
		final List<MPerspective> perspectives = workbenchContentsFinder.findPerspectives(matcher);

		final List<SWTBotPerspective> perspectiveBots = new ArrayList<SWTBotPerspective>();
		for (final MPerspective perspective : perspectives) {
			perspectiveBots.add(new SWTBotPerspective(perspective, this));
		}

		return perspectiveBots;
	}

	/**
	 * @return all available perspectives
	 */
	public List<SWTBotPerspective> perspectives() {
		return perspectives(Matchers.anything());
	}
	
	/**
	 * @return the active perspective in the active workbench page
	 */
	public SWTBotPerspective activePerspective() {
		MPerspective perspective = workbenchContentsFinder.findActivePerspective();
		if (perspective == null)
			throw new WidgetNotFoundException("There is no active perspective"); //$NON-NLS-1$
		return new SWTBotPerspective(perspective, this);
	}

	/**
	 * Switches the active workbench page to this perspective.
	 */
	public void switchPerspective(final MPerspective perspective) {
		/**
		 * Wait until the IEclipseContext is ready. For some reason if we don't do this as of Kepler SR2 we
		 * get issues thrown when attempting to do things such as switch perspective.
		 */
		this.waitUntil(new IEclipseContextReady());
		
		final EPartService partService = context.get(EPartService.class);
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				partService.switchPerspective(perspective);
			}
		});
	}
	
	/**
	 * Waits for a view matching the given matcher to appear in the active workbench page and returns it
	 *
	 * @param matcher the matcher used to match views
	 * @return views that match the matcher
	 * @throws WidgetNotFoundException if the view is not found
	 */
	public SWTBotView part(final Matcher<MPart> matcher) {
		final WaitForPart waitForPart = waitForPart(context, matcher);
		waitUntilWidgetAppears(waitForPart);
		return new SWTBotView(waitForPart.get(0), this);
	}

	/**
	 * Shortcut for view(withPartName(title))
	 *
	 * @param title the "human readable" title
	 * @return the view with the specified title
	 * @see WidgetMatcherFactory#withPartName(Matcher)
	 */
	public SWTBotView partByTitle(final String title) {
		final Matcher<MPart> withPartName = withPartName(title);
		return part(withPartName);
	}

	/**
	 * Shortcut for view(withPartId(id))
	 *
	 * @param id the view id
	 * @return the view with the specified id
	 * @see WidgetMatcherFactory#withPartId(String)
	 */
	public SWTBotView partById(final String id) {
		final Matcher<MPart> withPartId = withPartId(id);
		return part(withPartId);
	}
	
	/**
	 * Returns all views which are opened currently (no waiting!) which match the given matcher
	 * 
	 * @param matcher the matcher used to find views
	 * @return the list of all matching views
	 */
	public List<SWTBotView> parts(Matcher<?> matcher) {
		List<MPart> parts = workbenchContentsFinder.findParts(matcher);

		List<SWTBotView> partBots = new ArrayList<SWTBotView>();
		for (MPart part : parts)
			partBots.add(new SWTBotView(part, this));
		return partBots;
	}
	
	/**
	 * @return true if the specified part is active.
	 */	
	public boolean isPartActive(final MPart part) {
		final EPartService partService = context.get(EPartService.class);
		return UIThreadRunnable.syncExec(new BoolResult() {
			@Override
			public Boolean run() {
				return partService.getActivePart() == part;
			}
		});			
	}

	/**
	 * @return all views which are opened currently
	 */
	public List<SWTBotView> parts() {
		return parts(Matchers.anything());
	}

	/**
	 * Returns the active workbench view part
	 * 
	 * @return the active view, if any
	 * @throws WidgetNotFoundException if there is no active view
	 */
	public SWTBotView activePart() {
		MPart part = workbenchContentsFinder.findActivePart();
		if (part == null)
			throw new WidgetNotFoundException("There is no active part"); //$NON-NLS-1$
		return new SWTBotView(part, this);
	}
	
	/**
	 * Shows the specified part
	 *
	 * @param part
	 */
	public void showPart(final MPart part) {
		/**
		 * Wait until the IEclipseContext is ready. For some reason if we don't do this as of Kepler SR2 we
		 * get issues thrown when attempting to do things such as switch perspective.
		 */
		this.waitUntil(new IEclipseContextReady());
		
		final EPartService partService = context.get(EPartService.class);		
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				partService.showPart(part, PartState.ACTIVATE);
			}
		});
	}
	
	/**
	 * Closes the specified part
	 *
	 * @param part
	 */
	public void closePart(final MPart part) {
		/**
		 * Wait until the IEclipseContext is ready. For some reason if we don't do this as of Kepler SR2 we
		 * get issues thrown when attempting to do things such as switch perspective.
		 */
		this.waitUntil(new IEclipseContextReady());
		
		final EPartService partService = context.get(EPartService.class);		
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				//
				// If the part is dirty, ensure we don't lose any unsaved changes.
				//
				if (partService.savePart(part, true)) {
					partService.hidePart(part);
				}
			}
		});
	}
	
	@Override
	public SWTBotShell activeShell() throws WidgetNotFoundException {
		final SWTBotShell swtBotShell = super.activeShell();
		
		// E4 includes a 'limbo' shell as part of the PartRenderingEngine. For some reason this shell
		// can sometimes become the 'activeShell'. As such check for it to avoid hours of wasting time trying 
		// to find out why widgets can't be found
		//
		if( swtBotShell.getText().equals("PartRenderingEngine's limbo") ) {
			throw new WidgetNotFoundException("The activeShell() returned was the Limbo shell. This is most likely NOT the shell you were expecting!");
		}
		
		return swtBotShell;
	}

	/**
	 * Returns the {@link IEclipseContext} used for DI
	 *
	 * @return
	 */
	protected IEclipseContext getContext() {
		return this.context;
	}
	
	/**
	 * Returns when the {@link IEclipseContext} has
	 * a valid getActiveChild(). This appears to indicate it has an active Window if you 
	 * follow the logic inside ApplicationPartServiceImpl.
	 */
	protected class IEclipseContextReady extends DefaultCondition {
		@Override
		public boolean test() throws Exception {
			return context.getActiveChild() != null;
		}
		@Override
		public String getFailureMessage() {
			return "The current context does not contain an active window as its activeChild()";
		}
	}

}
