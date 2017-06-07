/*******************************************************************************
 * Copyright (c) 2014 SWTBot Committers and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Matt Biggs - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.View;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarPushButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarRadioButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarSeparatorButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarToggleButton;

/**
 * This represents the eclipse {@link View} item.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @author Matt biggs - Converted to E4
 * @version $Id$
 */
public class SWTBotView {

	private final SWTWorkbenchBot			bot;
	private final MPart						part;
	
	/** The logger. */
	protected final Logger					log;

	/**
	 * Creates an instance of a view part.
	 *
	 * @param part the part.
	 * @param bot the helper bot.
	 */
	public SWTBotView(final MPart part, final SWTWorkbenchBot bot) {
		this.bot = bot;
		Assert.isNotNull(part, "The part cannot be null");
		this.part = part;
		this.log = Logger.getLogger(getClass());
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
				final List<SWTBotToolbarButton> l = new ArrayList<SWTBotToolbarButton>();

				final MToolBar toolbar = part.getToolbar();
				for( final MToolBarElement toolbarElement : toolbar.getChildren() ) {				
					final ToolItem toolItem = (ToolItem) toolbarElement.getWidget();
					if( toolItem != null ) {
						try {
							if (SWTUtils.hasStyle(toolItem, SWT.PUSH)) {
								l.add(new SWTBotToolbarPushButton(toolItem));
							} else if(SWTUtils.hasStyle(toolItem, SWT.CHECK)) {
								l.add(new SWTBotToolbarToggleButton(toolItem));
							} else if(SWTUtils.hasStyle(toolItem, SWT.RADIO)) {
								l.add(new SWTBotToolbarRadioButton(toolItem));
							} else if(SWTUtils.hasStyle(toolItem, SWT.DROP_DOWN)) {
								l.add(new SWTBotToolbarDropDownButton(toolItem));
							} else if(SWTUtils.hasStyle(toolItem, SWT.SEPARATOR)) {
								l.add(new SWTBotToolbarSeparatorButton(toolItem));
							}
						} catch (WidgetNotFoundException e) {
							log.warn("Failed to find widget " + toolItem.getText(), e); //$NON-NLS-1$
						}
					} else {
						log.warn("Toolitem has not been created " + toolbarElement.getElementId()); //$NON-NLS-1$
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
	public SWTBotToolbarButton toolbarButton(final String tooltip) throws WidgetNotFoundException {
		final List<SWTBotToolbarButton> l = getToolbarButtons();

		for (int i = 0; i < l.size(); i++) {
			final SWTBotToolbarButton item = l.get(i);
			if (parseTooltipWithKeyboardModifier(item.getToolTipText()).equals(parseTooltipWithKeyboardModifier(tooltip))) {
				return item;
			}
		}

		throw new WidgetNotFoundException("Unable to find toolitem with the given tooltip '" + tooltip + "'");
	}
	
	/**
	 * Strip any ' (blah)' shortcut off the end of a toolbar tooltip.
	 */
	private String parseTooltipWithKeyboardModifier(final String tooltip) {
		
		final Pattern pattern = Pattern.compile("(.*) (\\(.*\\))");
		final Matcher matcher = pattern.matcher(tooltip);
		
		if (matcher.find()) {
			return matcher.group(1).toString();
		}
			
		return tooltip;
	}

	/**
	 * Shows the part if it is visible.
	 */
	public void show() {
		this.bot.showPart(this.part);
	}
	
	public void close() {
		if (this.part.isDirty()) {
			// If the part is dirty, we don't want the blocking dialog to block the test thread.
			UIThreadRunnable.asyncExec(new VoidResult() {
				@Override
				public void run() {
					SWTBotView.this.bot.closePart(SWTBotView.this.part);
				}
			});
		} else {		
			this.bot.closePart(this.part);
		}
	}
	
	/**
	 * The part wrapped by this bot.
	 */
	public MPart getPart() {
		return this.part;
	}

	/**
	 * Gets the title of the partReference.
	 * 
	 * @return the title of the part as visible in the tab
	 */
	public String getTitle() {
		return this.part.getLabel();
	}
	
	/**
	 * Gets the id of the partReference.
	 * 
	 * @return the id of the part as visible in the tab
	 */
	public String getId() {
		return this.part.getElementId();
	}
	
	/**
	 * Maximise the part's part-stack/sash.
	 * @since 2.4
	 */
	public void maximise() {
		this.part.getParent().getTags().add(IPresentationEngine.MAXIMIZED);
	}
	
	/**
	 * Minimise the part's part-stack/sash.
	 * @since 2.4
	 */
	public void minimise() {
		this.part.getParent().getTags().add(IPresentationEngine.MINIMIZED);
	}
	
	/**
	 * Restore the part's part-stack/sash.
	 * @since 2.4
	 */
	public void restore() {
		this.part.getParent().getTags().remove(IPresentationEngine.MAXIMIZED);
		this.part.getParent().getTags().remove(IPresentationEngine.MINIMIZED);
	}	

	/**
	 * Returns a SWTBot instance that matches the contents of this workbench part.
	 * 
	 * @return SWTBot
	 */
	public SWTBot bot() {
		return new SWTBot((Widget) part.getWidget());
	}

}
