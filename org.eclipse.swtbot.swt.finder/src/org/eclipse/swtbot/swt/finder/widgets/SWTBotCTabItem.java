/*******************************************************************************
 * Copyright (c) 2009, 2019 SWTBot Committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Patel - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import java.lang.reflect.Field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.hamcrest.SelfDescribing;

/**
 * @author Ketan Patel
 * @author Joshua Gosse &lt;jlgosse [at] ca [dot] ibm [dot] com&gt;
 */
@SWTBotWidget(clasz = CTabItem.class, preferredName = "cTabItem", referenceBy = { ReferenceBy.MNEMONIC })
public class SWTBotCTabItem extends AbstractSWTBot<CTabItem> {

	private CTabFolder	parent;

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotCTabItem(CTabItem w) throws WidgetNotFoundException {
		this(w, null);
	}

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotCTabItem(CTabItem w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
		this.parent = syncExec(new WidgetResult<CTabFolder>() {
			@Override
			public CTabFolder run() {
				return widget.getParent();
			}
		});
	}


	/**
	 * Shows the item. If the item is already showing in the receiver, this method simply returns. Otherwise, the items
	 * are scrolled until the item is visible.
	 * 
	 * @return This {@link SWTBotCTabItem}.
	 */
	public SWTBotCTabItem show() {
		syncExec(new VoidResult() {
			@Override
			public void run() {
				parent.showItem(widget);
			}
		});
		return this;
	}

	/**
	 * Activates the tabItem.
	 * 
	 * @return itself.
	 * @throws TimeoutException if the tab does not activate
	 */
	public SWTBotCTabItem activate() throws TimeoutException {
		log.trace("Activating {}", this); //$NON-NLS-1$
		waitForEnabled();
		// this runs in sync because tabFolder.setSelection() does not send a notification, and so should not block.
		asyncExec(new VoidResult() {
			@Override
			public void run() {
				widget.getParent().setSelection(widget);
				log.debug("Activated {}", this); //$NON-NLS-1$
			}
		});

		notify(SWT.Selection, createEvent(), parent);

		new SWTBot().waitUntil(new DefaultCondition() {
			@Override
			public boolean test() throws Exception {
				return isActive();
			}

			@Override
			public String getFailureMessage() {
				return "Timed out waiting for " + SWTUtils.toString(widget) + " to activate"; //$NON-NLS-1$ //$NON-NLS-2$
			}
		});

		return this;
	}

	@Override
	protected Event createEvent() {
		Event event = super.createEvent();
		event.widget = parent;
		event.item = widget;
		return event;
	}

	@Override
	public boolean isActive() {
		return syncExec(new BoolResult() {
			@Override
			public Boolean run() {
				return parent.getSelection() == widget;
			}
		});
	}

	@Override
	public boolean isEnabled() {
		return syncExec(new BoolResult() {
			@Override
			public Boolean run() {
				return widget.getParent().isEnabled();
			}
		});
	}

	/**
	 * Closes the CTabItem.
	 *
	 * @return this CTabItem.
	 */
	public SWTBotCTabItem close() {
		waitForEnabled();
		Rectangle rectangleCloseBox = syncExec(new Result<Rectangle>() {
			@Override
			public Rectangle run() {
				try {
					Field field = CTabItem.class.getDeclaredField("closeRect");
					field.setAccessible(true);
					return  (Rectangle) field.get(widget);
				}
				catch (Exception e) {

				}
				return null;
			}
		});
		clickCloseButton(rectangleCloseBox.x + (rectangleCloseBox.width / 2), rectangleCloseBox.y + (rectangleCloseBox.height / 2));
		return this;
	}

	private void notifyParent(final int eventType, final Event createEvent) {
		notify(eventType, createEvent, widget.getParent());
	}

	private void notifyParent(int event) {
		notify(event, createEvent(), widget.getParent());
	}


	private void clickCloseButton(int x, int y) {
		log.debug("Clicking on {}", this); //$NON-NLS-1$
		notifyParent(SWT.MouseEnter);
		notifyParent(SWT.MouseMove);
		notifyParent(SWT.Activate);
		notifyParent(SWT.FocusIn);
		notifyParent(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.NONE, 1));
		// this event being button 1 is what allows CTabItem to close
		notifyParent(SWT.MouseUp, createMouseEvent(x, y, 1, SWT.BUTTON1, 1));
		notifyParent(SWT.Selection);
		notifyParent(SWT.MouseHover);
		notifyParent(SWT.MouseMove);
		notifyParent(SWT.MouseExit);
		notifyParent(SWT.Deactivate);
		notifyParent(SWT.FocusOut);
		log.debug("Clicked on {}", this); //$NON-NLS-1$
	}

	@Override
	public SWTBotRootMenu contextMenu() throws WidgetNotFoundException {
		waitForEnabled();
		activate();
		return contextMenu(parent);
	}

}
