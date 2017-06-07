/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Lorenzo Bettini - (Bug 426869) mark new methods with since annotation
 *     Patrick Tasse - Improve SWTBot menu API and implementation (Bug 479091)
 *                   - Implement Drag and Drop using DNDEvent (Bug 516017)
 *     Aparna Argade(Cadence Design Systems, Inc.) - Bug 489179
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.widgetIsEnabled;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.ContextMenuHelper;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.keyboard.Keyboard;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.matchers.WithId;
import org.eclipse.swtbot.swt.finder.results.ArrayResult;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.StringResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.SWTBotEvents;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.utils.Traverse;
import org.eclipse.swtbot.swt.finder.utils.WidgetTextDescription;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

/**
 * Helper to find SWT {@link Widget}s and perform operations on them.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Joshua Gosse &lt;jlgosse [at] ca [dot] ibm [dot] com&gt;
 * @author Lorenzo Bettini - (Bug 426869) mark new methods with since annotation
 * @version $Id$
 */
public abstract class AbstractSWTBot<T extends Widget> {

	/** The logger. */
	protected final Logger			log;
	/** With great power comes great responsibility, use carefully. */
	public final T					widget;
	/** With great power comes great responsibility, use carefully. */
	public final Display			display;
	/** The description of the widget. */
	protected final SelfDescribing	description;
	/** The keyboard to use to type on the widget. */
	private Keyboard				keyboard;

	/**
	 * Constructs a new instance with the given widget.
	 *
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public AbstractSWTBot(T w) throws WidgetNotFoundException {
		this(w, new WidgetTextDescription(w));
	}

	/**
	 * Constructs a new instance with the given widget.
	 *
	 * @param w the widget.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public AbstractSWTBot(T w, SelfDescribing description) throws WidgetNotFoundException {
		if (w == null)
			throw new WidgetNotFoundException("The widget was null."); //$NON-NLS-1$

		this.widget = w;
		if (description == null)
			this.description = new WidgetTextDescription(w);
		else
			this.description = description;

		if (w.isDisposed())
			throw new WidgetNotFoundException("The widget {" + description + "} was disposed." + SWTUtils.toString(w)); //$NON-NLS-1$ //$NON-NLS-2$

		display = w.getDisplay();
		log = Logger.getLogger(getClass());
	}

	/**
	 * Sends a non-blocking notification of the specified type to the widget.
	 *
	 * @param eventType the event type.
	 * @see Widget#notifyListeners(int, Event)
	 */
	protected void notify(final int eventType) {
		notify(eventType, createEvent());
	}

	/**
	 * Sends a non-blocking notification of the specified type to the {@link #widget}.
	 *
	 * @param eventType the type of event.
	 * @param createEvent the event to be sent to the {@link #widget}.
	 */
	protected void notify(final int eventType, final Event createEvent) {
		notify(eventType, createEvent, widget);
	}

	/**
	 * Sends a non-blocking notification of the specified type to the widget.
	 *
	 * @param eventType the type of event.
	 * @param createEvent the event to be sent to the {@link #widget}.
	 * @param widget the widget to send the event to.
	 */
	protected void notify(final int eventType, final Event createEvent, final Widget widget) {
		createEvent.type = eventType;
		final Object[] result = syncExec(new ArrayResult<Object>() {
			public Object[] run() {
				return new Object[] { SWTBotEvents.toString(createEvent), AbstractSWTBot.this.toString() };
			}
		});

		log.trace(MessageFormat.format("Enquing event {0} on {1}", result)); //$NON-NLS-1$
		asyncExec(new VoidResult() {
			public void run() {
				if ((widget == null) || widget.isDisposed()) {
					log.trace(MessageFormat.format("Not notifying {0} is null or has been disposed", AbstractSWTBot.this)); //$NON-NLS-1$
					return;
				}
				if (!isEnabledInternal()) {
					log.warn(MessageFormat.format("Widget is not enabled: {0}", AbstractSWTBot.this)); //$NON-NLS-1$
					return;
				}
				log.trace(MessageFormat.format("Sending event {0} to {1}", result)); //$NON-NLS-1$
				widget.notifyListeners(eventType, createEvent);
				log.debug(MessageFormat.format("Sent event {0} to {1}", result)); //$NON-NLS-1$
			}
		});

		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				// do nothing, just wait for sync.
			}
		});

		long playbackDelay = SWTBotPreferences.PLAYBACK_DELAY;
		if (playbackDelay > 0)
			sleep(playbackDelay);
	}

	/**
	 * Sleeps for millis milliseconds. Delegate to {@link SWTUtils#sleep(long)}
	 *
	 * @param millis the time in milli seconds
	 */
	protected static void sleep(long millis) {
		SWTUtils.sleep(millis);
	}

	/**
	 * Creates an event.
	 *
	 * @return an event that encapsulates {@link #widget} and {@link #display}. Subclasses may override to set other
	 *         event properties.
	 */
	protected Event createEvent() {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = widget;
		event.display = display;
		return event;
	}

	/**
	 * Create a mouse event at the center of this widget
	 *
	 * @param button the mouse button that was clicked.
	 * @param stateMask the state of the keyboard modifier keys.
	 * @param count the number of times the mouse was clicked.
	 * @return an event that encapsulates {@link #widget} and {@link #display}
	 * @since 2.6
	 */
	protected Event createMouseEvent(int button, int stateMask, int count) {
		Rectangle bounds = getBounds();
		int x = bounds.x + (bounds.width / 2);
		int y = bounds.y + (bounds.height / 2);
		return createMouseEvent(x, y, button, stateMask, count);
	}

	/**
	 * Create a mouse event
	 *
	 * @param x the x co-ordinate of the mouse event.
	 * @param y the y co-ordinate of the mouse event.
	 * @param button the mouse button that was clicked.
	 * @param stateMask the state of the keyboard modifier keys.
	 * @param count the number of times the mouse was clicked.
	 * @return an event that encapsulates {@link #widget} and {@link #display}
	 * @since 1.2
	 */
	protected Event createMouseEvent(int x, int y, int button, int stateMask, int count) {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = widget;
		event.display = display;
		event.x = x;
		event.y = y;
		event.button = button;
		event.stateMask = stateMask;
		event.count = count;
		return event;
	}

	/**
	 * Create a selection event with a particular state mask
	 *
	 * @param stateMask the state of the keyboard modifier keys.
	 */
	protected Event createSelectionEvent(int stateMask) {
		Event event = createEvent();
		event.stateMask = stateMask;
		return event;
	}

	/**
	 * Get the bounds of this widget relative to its parent
	 *
	 * @since 2.6
	 */
	protected Rectangle getBounds() {
		throw new UnsupportedOperationException("This operation is not supported by this widget.");
	}

	/**
	 * Click on the widget at given coordinates
	 *
	 * @param x the x co-ordinate of the click
	 * @param y the y co-ordinate of the click
	 * @since 2.0
	 */
	protected void clickXY(int x, int y) {
		log.debug(MessageFormat.format("Clicking on {0}", this)); //$NON-NLS-1$
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.NONE, 1));
		notify(SWT.MouseUp, createMouseEvent(x, y, 1, SWT.BUTTON1, 1));
		notify(SWT.Selection, createSelectionEvent(SWT.BUTTON1));
		notify(SWT.MouseHover);
		notify(SWT.MouseMove);
		notify(SWT.MouseExit);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
		log.debug(MessageFormat.format("Clicked on {0}", this)); //$NON-NLS-1$
	}

	/**
	 * Right click on the widget at given coordinates
	 *
	 * @param x the x co-ordinate of the click
	 * @param y the y co-ordinate of the click
	 * @since 2.0
	 */
	private void rightClickXY(int x, int y) {
		log.debug(MessageFormat.format("Right clicking on {0}", this)); //$NON-NLS-1$
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown, createMouseEvent(x, y, 3, SWT.NONE, 1));
		notify(SWT.MouseUp, createMouseEvent(x, y, 3, SWT.BUTTON3, 1));
		notify(SWT.Selection, createSelectionEvent(SWT.BUTTON3));
		notify(SWT.MouseHover);
		notify(SWT.MouseMove);
		notify(SWT.MouseExit);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
		log.debug(MessageFormat.format("Right clicked on {0}", this)); //$NON-NLS-1$
	}

	/**
	 * Double-click on the widget at given coordinates
	 *
	 * @param x the x co-ordinate of the click
	 * @param y the y co-ordinate of the click
	 * @since 2.0
	 */
	protected void doubleClickXY(int x, int y) {
		log.debug(MessageFormat.format("Double-clicking on {0}", widget)); //$NON-NLS-1$
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.NONE, 1));
		notify(SWT.Selection, createSelectionEvent(SWT.NONE));
		notify(SWT.MouseUp, createMouseEvent(x, y, 1, SWT.BUTTON1, 1));
		notify(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.NONE, 2));
		notify(SWT.Selection, createSelectionEvent(SWT.NONE));
		notify(SWT.MouseDoubleClick, createMouseEvent(x, y, 1, SWT.NONE, 2));
		notify(SWT.DefaultSelection);
		notify(SWT.MouseUp, createMouseEvent(x, y, 1, SWT.BUTTON1, 2));
		notify(SWT.MouseHover);
		notify(SWT.MouseMove);
		notify(SWT.MouseExit);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
		log.debug(MessageFormat.format("Double-clicked on {0}", widget)); //$NON-NLS-1$
	}

	@Override
	public String toString() {
		return StringDescription.toString(description);
	}

	// /**
	// * Finds a menu matching the current {@link Matcher}.
	// *
	// * @param matcher the matcher used to find menus.
	// * @return all menus that match the matcher.
	// */
	// protected List findMenus(Matcher<?> matcher) {
	// return finder.findMenus(matcher);
	// }

	// /**
	// * Finds the menu on the main menu bar matching the given information.
	// *
	// * @param menuName the name of the menu.
	// * @param matcher the matcher used to find the menu.
	// * @return the first menuItem that matches the matcher
	// * @throws WidgetNotFoundException if the widget is not found.
	// */
	// protected Widget findMenu(Matcher<?> matcher, String menuName) throws WidgetNotFoundException {
	// return findMenu(getMenuMatcher(menuName), 0);
	// }

	// /**
	// * Gets the menu matcher for the given name.
	// *
	// * @param menuName the name of the menuitem that the matcher must match.
	// * @return {@link WidgetMatcherFactory#menuMatcher(String)}
	// */
	// protected Matcher getMenuMatcher(String menuName) {
	// return WidgetMatcherFactory.menuMatcher(menuName);
	// }

	// /**
	// * Finds the menu on the main menu bar matching the given information.
	// *
	// * @param matcher the matcher used to find the menu.
	// * @param index the index in the list of the menu items that match the matcher.
	// * @return the index(th) menuItem that matches the matcher
	// * @throws WidgetNotFoundException if the widget is not found.
	// */
	// protected Widget findMenu(Matcher<?> matcher, int index) throws WidgetNotFoundException {
	// List findMenus = findMenus(matcher);
	// if (!findMenus.isEmpty())
	// return (MenuItem) findMenus.get(index);
	// throw new WidgetNotFoundException("Could not find menu using matcher " + matcher);
	// }

	/**
	 * Gets the text of this object's widget.
	 *
	 * @return the text on the widget.
	 */
	public String getText() {
		return SWTUtils.getText(widget);
	}

	/**
	 * Gets the value of {@link Widget#getData(String))} for the key {@link SWTBotPreferences#DEFAULT_KEY} of this
	 * object's widget.
	 *
	 * @return the id that SWTBot may use to search this widget.
	 * @see WithId
	 */
	public String getId() {
		return syncExec(new StringResult() {
			public String run() {
				return (String) widget.getData(SWTBotPreferences.DEFAULT_KEY);
			}
		});
	}

	/**
	 * Gets the tooltip of this object's widget.
	 *
	 * @return the tooltip on the widget.
	 * @since 1.0
	 */
	public String getToolTipText() {
		return syncExec(new StringResult() {
			public String run() {
				return SWTUtils.getToolTipText(widget);
			}
		});
	}

	/**
	 * Check if this widget has a style attribute.
	 *
	 * @param w the widget.
	 * @param style the style bits, one of the constants in {@link SWT}.
	 * @return <code>true</code> if style is set on the widget.
	 */
	protected boolean hasStyle(Widget w, int style) {
		return SWTUtils.hasStyle(w, style);
	}

	/**
	 * Gets the context menu of this widget.
	 * <p>
	 * The context menu is invoked at the center of this widget.
	 *
	 * @return the context menu.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 2.4
	 */
	public SWTBotRootMenu contextMenu() throws WidgetNotFoundException {
		if (widget instanceof Control) {
			return contextMenu((Control) widget);
		}
		throw new WidgetNotFoundException("Could not find context menu for widget: " + widget); //$NON-NLS-1$
	}

	/**
	 * Gets the context menu of the given control.
	 * <p>
	 * The context menu is invoked at the center of this widget.
	 *
	 * @param control the control.
	 * @return the context menu.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 2.4
	 */
	protected SWTBotRootMenu contextMenu(final Control control) throws WidgetNotFoundException {
		ContextMenuHelper.notifyMenuDetect(control, widget);

		WaitForObjectCondition<Menu> waitForMenu = Conditions.waitForPopupMenu(control);
		new SWTBot().waitUntilWidgetAppears(waitForMenu);
		return new SWTBotRootMenu(waitForMenu.get(0));
	}

	/**
	 * Gets the context menu item matching the given text. It will attempt to
	 * find the menu item recursively in each of the sub-menus that are found.
	 * <p>
	 * This is equivalent to calling contextMenu().menu(text, true, 0);
	 *
	 * @param text the text on the context menu item.
	 * @return the context menu item that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu contextMenu(final String text) throws WidgetNotFoundException {
		return contextMenu().menu(text, true, 0);
	}

	/**
	 * Gets the context menu item matching the given text on the given control.
	 * It will attempt to find the menu item recursively in each of the
	 * sub-menus that are found.
	 * <p>
	 * This is equivalent to calling contextMenu(control).menu(text, true, 0);
	 *
	 * @param control the control.
	 * @param text the text on the context menu item.
	 * @return the context menu item that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 2.0
	 */
	protected SWTBotMenu contextMenu(final Control control, final String text) {
		return contextMenu(control).menu(text, true, 0);
	}

	/**
	 * Gets if the object's widget is enabled.
	 *
	 * @return <code>true</code> if the widget is enabled.
	 * @see Control#isEnabled()
	 */
	public boolean isEnabled() {
		if (widget instanceof Control)
			return syncExec(new BoolResult() {
				public Boolean run() {
					return isEnabledInternal();
				}
			});
		return false;
	}

	/**
	 * Gets if the widget is enabled.
	 * <p>
	 * This method is not thread safe, and must be called from the UI thread.
	 * </p>
	 *
	 * @return <code>true</code> if the widget is enabled.
	 * @since 1.0
	 */
	protected boolean isEnabledInternal() {
		try {
			return ((Boolean) SWTUtils.invokeMethod(widget, "isEnabled")).booleanValue(); //$NON-NLS-1$
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * Invokes {@link ArrayResult#run()} on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the array returned by toExecute.
	 */
	protected <E> E[] syncExec(ArrayResult<E> toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link VoidResult#run()} on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 */
	protected void syncExec(VoidResult toExecute) {
		UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link ListResult#run()} on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the list returned by toExecute
	 */
	protected <E> List<E> syncExec(ListResult<E> toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link BoolResult#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the boolean returned by toExecute
	 */
	protected boolean syncExec(BoolResult toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link BoolResult#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the boolean returned by toExecute
	 */

	protected String syncExec(StringResult toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link Result#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the boolean returned by toExecute
	 */
	protected <E> E syncExec(Result<E> toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link WidgetResult#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the Widget returned by toExecute
	 */
	protected T syncExec(WidgetResult<T> toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link IntResult#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the integer returned by toExecute
	 */

	protected int syncExec(IntResult toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link BoolResult#run()} asynchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 */
	protected void asyncExec(VoidResult toExecute) {
		UIThreadRunnable.asyncExec(display, toExecute);
	}

	/**
	 * Gets the foreground color of the widget.
	 *
	 * @return the foreground color on the widget, or <code>null</code> if the widget is not an instance of
	 *         {@link Control}.
	 * @since 1.0
	 */
	public Color foregroundColor() {
		return syncExec(new Result<Color>() {
			public Color run() {
				if (widget instanceof Control)
					return ((Control) widget).getForeground();
				return null;
			}
		});
	}

	/**
	 * Gets the background color of the widget.
	 *
	 * @return the background color on the widget, or <code>null</code> if the widget is not an instance of
	 *         {@link Control}.
	 * @since 1.0
	 */
	public Color backgroundColor() {
		return syncExec(new Result<Color>() {
			public Color run() {
				if (widget instanceof Control)
					return ((Control) widget).getBackground();
				return null;
			}
		});
	}

	/**
	 * Check if the widget is enabled, throws if the widget is disabled.
	 *
	 * @since 1.3
	 */
	protected void assertEnabled() {
		Assert.isTrue(isEnabled(), MessageFormat.format("Widget {0} is not enabled.", this)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Wait until the widget is enabled.
	 *
	 * @since 2.0
	 */
	protected void waitForEnabled() {
		new SWTBot().waitUntil(widgetIsEnabled(this));
	}

	/**
	 * Checks if the widget is visible.
	 *
	 * @return <code>true</code> if the widget is visible, <code>false</code> otherwise.
	 * @since 1.0
	 */
	public boolean isVisible() {
		return syncExec(new BoolResult() {
			public Boolean run() {
				if (widget instanceof Control)
					return ((Control) widget).isVisible();
				return true;
			}
		});
	}

	/**
	 * Sets the focus on this control.
	 *
	 * @since 1.2
	 */
	public void setFocus() {
		waitForEnabled();
		log.debug(MessageFormat.format("Attempting to set focus on {0}", this));
		syncExec(new VoidResult() {
			public void run() {
				if (widget instanceof Control) {
					Control control = (Control) widget;
					if (hasFocus(control))
						return;
					control.getShell().forceActive();
					control.getShell().forceFocus();
					control.forceFocus();
				}
			}

			private boolean hasFocus(Control control) {
				return control.isFocusControl();
			}
		});
	}

	/**
	 * @param traverse the kind of traversal to perform.
	 * @return <code>true</code> if the traversal succeeded.
	 * @see Control#traverse(int)
	 */
	public boolean traverse(final Traverse traverse) {
		waitForEnabled();
		setFocus();

		if (!(widget instanceof Control))
			throw new UnsupportedOperationException("Can only traverse widgets of type Control. You're traversing a widget of type: " //$NON-NLS-1$
					+ widget.getClass().getName());

		return syncExec(new BoolResult() {
			public Boolean run() {
				return ((Control) widget).traverse(traverse.type);
			}
		});
	}

	/**
	 * @return <code>true</code> if this widget has focus.
	 * @see Display#getFocusControl()
	 */
	public boolean isActive() {
		return syncExec(new BoolResult() {
			public Boolean run() {
				return display.getFocusControl() == widget;
			}
		});
	}

	/**
	 * Clicks on this widget.
	 *
	 * @return itself.
	 */
	protected AbstractSWTBot<T> click() {
		throw new UnsupportedOperationException("This operation is not supported by this widget.");
	}

	/**
	 * Empty method stub, since it should be overridden by subclass#rightClick
	 *
	 * @return itself.
	 */
	protected AbstractSWTBot<T> rightClick() {
		throw new UnsupportedOperationException("This operation is not supported by this widget.");
	}

	/**
	 * Perform a click action at the given coordinates
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param post Whether or not {@link Display#post} should be used
	 * @return itself.
	 */
	protected AbstractSWTBot<T> click(final int x, final int y, final boolean post) {
		if (post) {
			syncExec(new VoidResult() {
				public void run() {
					Point cursorLocation = display.getCursorLocation();
					moveMouse(x, y);
					mouseDown(x, y, 1);
					mouseUp(x, y, 1);
					moveMouse(cursorLocation.x, cursorLocation.y);
				}
			});
		} else
			clickXY(x, y);
		return this;
	}

	/**
	 * Perform a right-click action at the given coordinates
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param post Whether or not {@link Display#post} should be used
	 * @return itself.
	 */
	protected AbstractSWTBot<T> rightClick(final int x, final int y, final boolean post) {
		if (post) {
			syncExec(new VoidResult() {
				public void run() {
					Point cursorLocation = display.getCursorLocation();
					moveMouse(x, y);
					mouseDown(x, y, 3);
					mouseUp(x, y, 3);
					moveMouse(cursorLocation.x, cursorLocation.y);
				}
			});
		} else
			rightClickXY(x, y);
		return this;
	}

	/**
	 * Post an SWT.MouseMove event
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	void moveMouse(final int x, final int y) {
		asyncExec(new VoidResult() {
			public void run() {
				Event event = createMouseEvent(x, y, 0, 0, 0);
				event.type = SWT.MouseMove;
				display.post(event);
			}
		});
	}

	/**
	 * Post an SWT.MouseDown event
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param button the mouse button to be pressed
	 */
	private void mouseDown(final int x, final int y, final int button) {
		asyncExec(new VoidResult() {
			public void run() {
				Event event = createMouseEvent(x, y, button, 0, 0);
				event.type = SWT.MouseDown;
				display.post(event);
			}
		});
	}

	/**
	 * Post an SWT.MouseUp event.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param button the mouse button to be pressed
	 */
	private void mouseUp(final int x, final int y, final int button) {
		asyncExec(new VoidResult() {
			public void run() {
				Event event = createMouseEvent(x, y, button, 0, 0);
				event.type = SWT.MouseUp;
				display.post(event);
			}
		});
	}

	/**
	 * @return the absolute location of the widget relative to the display.
	 */
	protected Rectangle absoluteLocation() {
		throw new UnsupportedOperationException("This operation is not supported by this widget.");
	}

	/**
	 * @return the keyboard to use to type on this widget.
	 */
	protected Keyboard keyboard() {
		if (keyboard == null)
			keyboard = KeyboardFactory.getDefaultKeyboard(widget, description);
		return keyboard;
	}

	/**
	 * Presses the shortcut specified by the given keys.
	 *
	 * @param modificationKeys the combination of {@link SWT#ALT} | {@link SWT#CTRL} | {@link SWT#SHIFT} |
	 *            {@link SWT#COMMAND}.
	 * @param c the character
	 * @return the same instance
	 * @see Keystrokes#toKeys(int, char)
	 */
	public AbstractSWTBot<T> pressShortcut(int modificationKeys, char c) {
		waitForEnabled();
		setFocus();
		keyboard().pressShortcut(modificationKeys, c);
		return this;
	}

	/**
	 * Presses the shortcut specified by the given keys.
	 *
	 * @param modificationKeys the combination of {@link SWT#ALT} | {@link SWT#CTRL} | {@link SWT#SHIFT} | {@link SWT#COMMAND}.
	 * @param keyCode the keyCode, these may be special keys like F1-F12, or navigation keys like HOME, PAGE_UP
	 * @param c the character
	 * @return the same instance
	 * @see Keystrokes#toKeys(int, char)
	 */
	public AbstractSWTBot<T> pressShortcut(int modificationKeys, int keyCode, char c) {
		waitForEnabled();
		setFocus();
		keyboard().pressShortcut(modificationKeys, keyCode, c);
		return this;
	}

	/**
	 * Presses the shortcut specified by the given keys.
	 *
	 * @param keys the keys to press
	 * @return the same instance
	 * @see Keyboard#pressShortcut(KeyStroke...)
	 * @see Keystrokes
	 */
	public AbstractSWTBot<T> pressShortcut(KeyStroke... keys) {
		waitForEnabled();
		setFocus();
		keyboard().pressShortcut(keys);
		return this;
	}

	/**
	 * Returns the control used for Drag and Drop operations.
	 *
	 * @return the control
	 * @since 2.6
	 */
	protected Control getDNDControl() {
		throw new UnsupportedOperationException("This operation is not supported by this widget.");
	}

	/**
	 * Start a drag operation, by making sure the source control has focus and
	 * the source widget is selected, if applicable.
	 *
	 * @since 2.6
	 */
	protected void dragStart() {
		throw new UnsupportedOperationException("This operation is not supported by this widget.");
	}

	/**
	 * @since 2.2
	 */
	public void dragAndDrop(final AbstractSWTBot<? extends Widget> target) {

		DragSource dragSource = syncExec(new Result<DragSource>() {
			public DragSource run() {
				Control control = getDNDControl();
				return control == null ? null : (DragSource) control.getData(DND.DRAG_SOURCE_KEY);
			}
		});
		if (dragSource == null) {
			log.error(MessageFormat.format("Widget {0} is not configured for drag support.", this));
			return;
		}

		DropTarget dropTarget = syncExec(new Result<DropTarget>() {
			public DropTarget run() {
				Control control = target.getDNDControl();
				return control == null ? null : (DropTarget) control.getData(DND.DROP_TARGET_KEY);
			}
		});
		if (dropTarget == null) {
			log.error(MessageFormat.format("Widget {0} is not configured for drop support.", target));
			return;
		}

		dragStart();

		// SWT.DragDetect
		Event dragDetectEvent = createMouseEvent(1, SWT.NONE, 0);
		notifyDragDetect(dragDetectEvent);

		// DND.DragStart -> DragSource
		Event dragStartEvent = createDNDEvent();
		dragStartEvent.x = dragDetectEvent.x;
		dragStartEvent.y = dragDetectEvent.y;
		notify(DND.DragStart, dragStartEvent, dragSource);
		if (!dragStartEvent.doit) {
			return;
		}

		List<TransferData> dataTypeList = new ArrayList<TransferData>();
		for (Transfer sourceTransfer : dragSource.getTransfer()) {
			for (TransferData sourceDataType : sourceTransfer.getSupportedTypes()) {
				for (Transfer targetTransfer : dropTarget.getTransfer()) {
					if (targetTransfer.isSupportedType(sourceDataType)) {
						dataTypeList.add(sourceDataType);
						break;
					}
				}
			}
		}
		TransferData[] dataTypes = dataTypeList.toArray(new TransferData[dataTypeList.size()]);
		if (dataTypes.length == 0) {
			// DND.DragEnd -> DragSource
			Event dragEndEvent = createDNDEvent();
			dragEndEvent.doit = false;
			notify(DND.DragEnd, dragEndEvent, dragSource);
			return;
		}

		// DND.DragEnter -> DropTarget
		Event dragEnterEvent = createDNDEvent();
		Point targetAbsoluteLocation = Geometry.centerPoint(target.absoluteLocation());
		dragEnterEvent.x = targetAbsoluteLocation.x;
		dragEnterEvent.y = targetAbsoluteLocation.y;
		setDNDEventField(dragEnterEvent, "dataType", dataTypes[0]);
		setDNDEventField(dragEnterEvent, "dataTypes", dataTypes);
		setDNDEventField(dragEnterEvent, "operations", DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
		setDNDEventField(dragEnterEvent, "feedback", DND.FEEDBACK_SELECT);
		dragEnterEvent.detail = DND.DROP_MOVE;
		dragEnterEvent.item = target.widget;
		notify(DND.DragEnter, dragEnterEvent, dropTarget);

		// DND.DragOver -> DropTarget
		Event dragOverEvent = createDNDEvent(dragEnterEvent);
		notify(DND.DragOver, dragOverEvent, dropTarget);

		// DND.DragLeave -> DropTarget
		Event dragLeaveEvent = createDNDEvent();
		dragLeaveEvent.item = target.widget;
		notify(DND.DragLeave, dragLeaveEvent, dropTarget);

		// DND.DropAccept -> DropTarget
		Event dropAcceptEvent = createDNDEvent(dragOverEvent);
		notify(DND.DropAccept, dropAcceptEvent, dropTarget);
		if (dropAcceptEvent.detail == DND.DROP_NONE) {
			// DND.DragEnd -> DragSource
			Event dragEndEvent = createDNDEvent();
			dragEndEvent.doit = false;
			notify(DND.DragEnd, dragEndEvent, dragSource);
			return;
		}

		// DND.DragSetData -> DragSource
		Event dragSetDataEvent = createDNDEvent();
		setDNDEventField(dragSetDataEvent, "dataType", getDNDEventField(dropAcceptEvent, "dataType"));
		notify(DND.DragSetData, dragSetDataEvent, dragSource);
		if (!dragSetDataEvent.doit) {
			// DND.DragEnd -> DragSource
			Event dragEndEvent = createDNDEvent();
			dragEndEvent.doit = false;
			notify(DND.DragEnd, dragEndEvent, dragSource);
			return;
		}

		// DND.Drop -> DropTarget
		Event dropEvent = createDNDEvent(dropAcceptEvent);
		dropEvent.data = dragSetDataEvent.data;
		notify(DND.Drop, dropEvent, dropTarget);

		// DND.DragEnd -> DragSource
		Event dragEndEvent = createDNDEvent();
		dragEndEvent.detail = dropEvent.detail;
		dragEndEvent.doit = dragEndEvent.detail != DND.DROP_NONE;
		notify(DND.DragEnd, dragEndEvent, dragSource);
	}

	private Event createDNDEvent() {
		try {
			Class<?> clazz = Class.forName("org.eclipse.swt.dnd.DNDEvent");
			Constructor<?> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			Event dndEvent = (Event) constructor.newInstance();
			dndEvent.time = (int) System.currentTimeMillis();
			dndEvent.widget = widget;
			dndEvent.display = display;
			return dndEvent;
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private Event createDNDEvent(Event other) {
		Event dndEvent = createDNDEvent();
		dndEvent.x = other.x;
		dndEvent.y = other.y;
		dndEvent.item = other.item;
		dndEvent.detail = other.detail;
		setDNDEventField(dndEvent, "dataType", getDNDEventField(other, "dataType"));
		setDNDEventField(dndEvent, "dataTypes", getDNDEventField(other, "dataTypes"));
		setDNDEventField(dndEvent, "operations", getDNDEventField(other, "operations"));
		setDNDEventField(dndEvent, "feedback", DND.FEEDBACK_SELECT);
		setDNDEventField(dndEvent, "image", getDNDEventField(other, "image"));
		setDNDEventField(dndEvent, "offsetX", getDNDEventField(other, "offsetX"));
		setDNDEventField(dndEvent, "offsetY", getDNDEventField(other, "offsetY"));
		return dndEvent;
	}

	private void setDNDEventField(Event dndEvent, String fieldName, Object value) {
		try {
			Class<?> clazz = Class.forName("org.eclipse.swt.dnd.DNDEvent");
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(dndEvent, value);
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private Object getDNDEventField(Event dndEvent, String fieldName) {
		try {
			Class<?> clazz = Class.forName("org.eclipse.swt.dnd.DNDEvent");
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(dndEvent);
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private void notifyDragDetect(Event dragDetectEvent) {
		// Don't send SWT.DragDetect to the DragSource listener,
		// otherwise the cursor gets stuck in drag mode
		final Control control = getDNDControl();
		final Listener dragSourceListener = syncExec(new Result<Listener>() {
			public Listener run() {
				// The DragSource listener is an anonymous class of DragSource
				for (Listener listener : control.getListeners(SWT.DragDetect)) {
					if (DragSource.class.equals(listener.getClass().getEnclosingClass())) {
						return listener;
					}
				}
				return null;
			}
		});
		try {
			if (dragSourceListener != null) {
				syncExec(new VoidResult() {
					public void run() {
						control.removeListener(SWT.DragDetect, dragSourceListener);
					}
				});
			}
			notify(SWT.DragDetect, dragDetectEvent, control);
		} finally {
			if (dragSourceListener != null) {
				syncExec(new VoidResult() {
					public void run() {
						control.addListener(SWT.DragDetect, dragSourceListener);
					}
				});
			}
		}
	}
}

