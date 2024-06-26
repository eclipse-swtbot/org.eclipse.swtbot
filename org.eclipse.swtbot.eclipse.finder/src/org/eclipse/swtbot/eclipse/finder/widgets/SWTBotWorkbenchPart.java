/*******************************************************************************
 * Copyright (c) 2008, 2019 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Aparna Argade - Support for reading StatusLine messages (Bug 544633)
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.utils.PartLabelDescription;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.Finder;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarPushButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarRadioButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarSeparatorButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarToggleButton;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.WorkbenchPartReference;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.core.IsAnything;


/**
 * This represents the eclipse {@link IWorkbenchPartReference} item, subclasses must extend this to implement support
 * for various {@link IWorkbenchPartReference}s.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @version $Id$
 * @since 2.0
 */
public abstract class SWTBotWorkbenchPart<T extends IWorkbenchPartReference> {

	/** The IWorkbenchPartReference reference that this part encapsulates. */
	protected final T				partReference;
	/** The logger. */
	protected final Logger			log;
	/** A helper swtbot instance. */
	protected final SWTWorkbenchBot	bot;
	private final SelfDescribing	description;
	private Widget widget;
	private final Matcher<Widget>   anyWidget = new IsAnything<Widget>();

	/**
	 * Creates an instance of a workbench part.
	 *
	 * @param partReference the part reference.
	 * @param bot the helper bot.
	 */
	public SWTBotWorkbenchPart(T partReference, SWTWorkbenchBot bot) {
		this(partReference, bot, new PartLabelDescription<T>(partReference));
	}

	/**
	 * Creates an instance of a workbench part.
	 *
	 * @param partReference the part reference.
	 * @param bot the helper bot.
	 * @param description the description of the workbench part.
	 */
	public SWTBotWorkbenchPart(T partReference, SWTWorkbenchBot bot, SelfDescribing description) {
		this.bot = bot;
		if (description == null )
			description = new PartLabelDescription<T>(partReference);
		this.description = description;
		Assert.isNotNull(partReference, "The part reference cannot be null"); //$NON-NLS-1$
		this.partReference = partReference;
		log = LoggerFactory.getLogger(getClass());
	}

	/**
	 * @return the reference for this part.
	 */
	public T getReference() {
		return partReference;
	}

	/**
	 * Close the partReference.
	 */
	public abstract void close();

	/**
	 * Shows the part if it is visible.
	 */
	public void show() {
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				try {
					partReference.getPage().activate(partReference.getPart(true));
					partReference.getPage().showView(partReference.getId());
				} catch (PartInitException e) {
					throw new RuntimeException("Could not show partReference - " + partReference.getPartName(), e); //$NON-NLS-1$
				}
			}
		});
	}

	/**
	 * Gets the title of the partReference.
	 *
	 * @return the title of the part as visible in the tab
	 */
	public String getTitle() {
		return partReference.getPartName();
	}

	/**
	 * Gets the toolbar buttons currently visible.
	 *
	 * @return The set of toolbar buttons.
	 */
	public List<SWTBotToolbarButton> getToolbarButtons() {
		return UIThreadRunnable.syncExec(new ListResult<SWTBotToolbarButton>() {

			@Override
			public List<SWTBotToolbarButton> run() {
				ToolBar toolbar = null;

				IWorkbenchPartSite site = partReference.getPart(false).getSite();
				if (site instanceof IViewSite) {
					IToolBarManager t = ((IViewSite) site).getActionBars().getToolBarManager();
					if (t instanceof ToolBarManager) {
						toolbar = ((ToolBarManager)t).getControl();
					}
				}

				final List<SWTBotToolbarButton> l = new ArrayList<SWTBotToolbarButton>();

				if (toolbar == null)
					return l;

				ToolItem[] items = toolbar.getItems();
				log.debug("number of items : " + items.length);
				for (int i = 0; i < items.length; i++) {
					try {
						if (SWTUtils.hasStyle(items[i], SWT.PUSH))
							l.add(new SWTBotToolbarPushButton(items[i]));
						else if(SWTUtils.hasStyle(items[i], SWT.CHECK))
							l.add(new SWTBotToolbarToggleButton(items[i]));
						else if(SWTUtils.hasStyle(items[i], SWT.RADIO))
							l.add(new SWTBotToolbarRadioButton(items[i]));
						else if(SWTUtils.hasStyle(items[i], SWT.DROP_DOWN))
							l.add(new SWTBotToolbarDropDownButton(items[i]));
						else if(SWTUtils.hasStyle(items[i], SWT.SEPARATOR))
							l.add(new SWTBotToolbarSeparatorButton(items[i]));
					} catch (WidgetNotFoundException e) {
						log.warn("Failed to find widget " + items[i].getText(), e); //$NON-NLS-1$
					}
				}

				return l;

			}
		});
	}

	/**
	 * Gets the toolbar drop down button matching the given toolbar button.
	 *
	 * @param tooltip The tooltip to use to find the button to return.
	 * @return The toolbar button.
	 * @throws WidgetNotFoundException Thrown if the widget was not found matching the given tooltip.
	 */
	public SWTBotToolbarDropDownButton toolbarDropDownButton(String tooltip) throws WidgetNotFoundException {
		SWTBotToolbarButton abstractButton = toolbarButton(tooltip);
		if (abstractButton instanceof SWTBotToolbarDropDownButton)
			return (SWTBotToolbarDropDownButton) abstractButton;

		throw new WidgetNotFoundException("Unable to find toolitem with the given tooltip '" + tooltip + "'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Gets the toolbar radio button matching the given toolbar button.
	 *
	 * @param tooltip The tooltip to use to find the button to return.
	 * @return The toolbar button.
	 * @throws WidgetNotFoundException Thrown if the widget was not found matching the given tooltip.
	 */
	public SWTBotToolbarRadioButton toolbarRadioButton(String tooltip) throws WidgetNotFoundException {
		SWTBotToolbarButton abstractButton = toolbarButton(tooltip);
		if (abstractButton instanceof SWTBotToolbarRadioButton)
			return (SWTBotToolbarRadioButton) abstractButton;

		throw new WidgetNotFoundException("Unable to find toolitem with the given tooltip '" + tooltip + "'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Gets the toolbar push button matching the given toolbar button.
	 *
	 * @param tooltip The tooltip to use to find the button to return.
	 * @return The toolbar button.
	 * @throws WidgetNotFoundException Thrown if the widget was not found matching the given tooltip.
	 */
	public SWTBotToolbarPushButton toolbarPushButton(String tooltip) throws WidgetNotFoundException {
		SWTBotToolbarButton abstractButton = toolbarButton(tooltip);
		if (abstractButton instanceof SWTBotToolbarPushButton)
			return (SWTBotToolbarPushButton) abstractButton;

		throw new WidgetNotFoundException("Unable to find toolitem with the given tooltip '" + tooltip + "'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Gets the toggle toolbar button matching the given toolbar button.
	 *
	 * @param tooltip The tooltip to use to find the button to return.
	 * @return The toolbar button.
	 * @throws WidgetNotFoundException Thrown if the widget was not found matching the given tooltip.
	 */
	public SWTBotToolbarToggleButton toolbarToggleButton(String tooltip) throws WidgetNotFoundException {
		SWTBotToolbarButton abstractButton = toolbarButton(tooltip);
		if (abstractButton instanceof SWTBotToolbarToggleButton)
			return (SWTBotToolbarToggleButton) abstractButton;

		throw new WidgetNotFoundException("Unable to find toolitem with the given tooltip '" + tooltip + "'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Gets the toolbar button matching the given toolbar button.
	 *
	 * @param tooltip The tooltip to use to find the button to return.
	 * @return The toolbar button.
	 * @throws WidgetNotFoundException Thrown if the widget was not found matching the given tooltip.
	 */
	public SWTBotToolbarButton toolbarButton(String tooltip) throws WidgetNotFoundException {
		List<SWTBotToolbarButton> l = getToolbarButtons();

		for (int i = 0; i < l.size(); i++) {
			SWTBotToolbarButton item = l.get(i);
			if (item.getToolTipText().equals(tooltip)) {
				return item;
			}
		}

		throw new WidgetNotFoundException("Unable to find toolitem with the given tooltip '" + tooltip + "'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @param matcher a matcher.
	 * @return a widget within the parent widget that matches the specified matcher.
	 */
	protected <S extends Widget> S findWidget(Matcher<S> matcher) {
		return findWidgets(matcher).get(0);
	}

	/**
	 * @param matcher a matcher.
	 * @return a widget within the parent widget that matches the specified matcher.
	 */
	protected <S extends Widget> List<? extends S> findWidgets(Matcher<S> matcher) {
		Finder finder = bot.getFinder();
		Control control = getControl();
		boolean shouldFindInvisibleControls = finder.shouldFindInvisibleControls();
		finder.setShouldFindInvisibleControls(true);
		try {
			return bot.widgets(matcher, control);
		} catch (Exception e) {
			throw new WidgetNotFoundException("Could not find any control inside the view " + partReference.getPartName(), e); //$NON-NLS-1$
		} finally {
			finder.setShouldFindInvisibleControls(shouldFindInvisibleControls);
		}
	}

	/**
	 * Returns the workbench pane control.
	 *
	 * @return returns the workbench pane control.
	 */
	private Control getControl() {
		return ((WorkbenchPartReference) partReference).getPane().getControl();
	}

	/**
	 * Returns a SWTBot instance that matches the contents of this workbench part.
	 *
	 * @return SWTBot
	 */
	public SWTBot bot() {
		return new SWTBot(getControl());
	}

	/**
	 * Asserts that the viewpart is active.
	 *
	 */
	protected void assertActive() {
		Assert.isLegal(isActive(), MessageFormat.format("The workbench part {0}is not active", description));
	}

	/**
	 * @return <code>true</code> if the part is currently active.
	 */
	public abstract boolean isActive();

	/**
	 * Sets focus on the current part.
	 */
	public abstract void setFocus();

	/**
	 * The parent widget inside the partReference. If you want to look for a particular widget within the part, this is
	 * a good place to start searching for the widget.
	 * <p>
	 * <b>NOTE:</b> Clients must ensure that the part is active at the time of making this call. If the part is not
	 * active, then this method will throw a {@link WidgetNotFoundException}.
	 * </p>
	 *
	 * @return the parent widget in the part.
	 * @see #findWidget(org.hamcrest.Matcher)
	 * @see #assertActive()
	 * @see #show()
	 */
	public Widget getWidget() {
		show();
		if (widget == null)
			widget = findWidget(anyWidget);
		return widget;
	}

	/**
	 * Gets messages on MessageLine of StatusLine.
	 * Caller needs to ensure focus on the editor/view.
	 *
	 * @return list of strings present on Status Line.
	 * @since 2.8
	 */
	public List<String> getStatusLineMessages() {
		return UIThreadRunnable.syncExec(new ListResult<String>() {
			@Override
			public List<String> run() {
				Control statusLine = null;
				IStatusLineManager s = null;
				int messageCount = 0;
				final List<String> l = new ArrayList<String>();
				IWorkbenchPartSite site = partReference.getPart(false).getSite();

				if (site instanceof IViewSite) {
					s = ((IViewSite) site).getActionBars().getStatusLineManager();
				} else if (site instanceof IEditorSite) {
					s = ((IEditorSite) site).getActionBars().getStatusLineManager();
				}
				while (s instanceof SubStatusLineManager) {
					s = (IStatusLineManager) ((SubStatusLineManager) s).getParent();
				}
				if (s instanceof StatusLineManager) {
					statusLine = ((StatusLineManager) s).getControl();
				}
				if (statusLine == null)
					return l;
				// StatusLine messages are on CLabel controls
				SWTBot statusbot = new SWTBot(statusLine);
				try {
					@SuppressWarnings("unchecked")
					List<? extends Widget> clabels = bot.widgets(allOf(widgetOfType(CLabel.class)), statusLine);
					messageCount = clabels.size();
				} catch (Exception e) {
					log.warn("Failed to get CLable controls on Status line", e); //$NON-NLS-1$
				}
				// Get text on CLabel controls
				for (int i = 0; i < messageCount; i++) {
					try {
						l.add(statusbot.clabel(i).getText());
					} catch (Exception e) {
						log.warn("Failed to get Status line message at index " //$NON-NLS-1$
								+ Integer.toString(i), e);
					}
				}
				return l;
			}
		});
	}

}
