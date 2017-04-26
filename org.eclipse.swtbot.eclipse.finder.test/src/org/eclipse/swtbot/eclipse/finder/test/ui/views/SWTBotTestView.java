/*******************************************************************************
 * Copyright (c) 2008, 2016 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Frank Schuerer - https://bugs.eclipse.org/bugs/show_bug.cgi?id=424238
 *     Patrick Tasse - SWTBotView does not support dynamic view menus (Bug 489325)
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.test.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swtbot.eclipse.finder.test.ui.data.internal.SWTBotTestContentProvider;
import org.eclipse.swtbot.eclipse.finder.test.ui.data.internal.SWTBotTestData;
import org.eclipse.swtbot.eclipse.finder.test.ui.data.internal.SWTBotTestLabelProvider;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * <p>
 * This is a simple view that displays some strings in a view and provides a few actions.
 * </p>
 * <p>
 * Its purpose is for providing a fixed view for testing the SWTBot Eclipse Finder Bundle.
 * </p>
 * 
 * @author Stephen Paulin &lt;paulin [at] spextreme [dot] com&gt;
 * @version $Id$
 */
public class SWTBotTestView extends ViewPart {
	/**
	 * The unique view identifier.
	 */
	public static String	VIEW_ID	= "org.eclipse.swtbot.eclipse.finder.test.ui.views.SWTBotTestView";

	private TableViewer		viewer;
	private Action			iActionTypeAction;
	private Action			doubleClickAction;
	private Action			iToggleTypeAction;
	private Action			iRadioTypeAction;
	private Action			iDropDownTypeAction;
	private Action			iActionTypeActionWithId;
	
	/**
	 * The constructor.
	 */
	public SWTBotTestView() {
		// Do nothing.
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new SWTBotTestContentProvider());
		viewer.setLabelProvider(new SWTBotTestLabelProvider());
		viewer.setSorter(new ViewerSorter());

		List<SWTBotTestData> data = new ArrayList<SWTBotTestData>();
		data.add(new SWTBotTestData("Test Data 1"));
		data.add(new SWTBotTestData("Test Data 2"));
		data.add(new SWTBotTestData("Test Data 3"));

		viewer.setInput(data);

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SWTBotTestView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(iActionTypeAction);
		manager.add(iActionTypeActionWithId);
		manager.add(iToggleTypeAction);
		manager.add(iRadioTypeAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.remove(iDropDownTypeAction.getId());
				manager.insertBefore(IWorkbenchActionConstants.MB_ADDITIONS, iDropDownTypeAction);
			}
		});
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(iActionTypeAction);
		manager.add(iActionTypeActionWithId);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(iActionTypeAction);
		manager.add(iToggleTypeAction);
		manager.add(iRadioTypeAction);
		manager.add(iDropDownTypeAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {
		iActionTypeAction = new Action() {
			@Override
			public void run() {
				showMessage("iAction executed.");
			}
		};
		iActionTypeAction.setText("IAction Type Command");
		iActionTypeAction.setToolTipText("This represents an IAction command.");
		iActionTypeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		iActionTypeActionWithId = new Action() {
			@Override
			public void run() {
				showMessage("iAction executed.");
			}
		};
		iActionTypeActionWithId.setId("myActionId");
		iActionTypeActionWithId.setText("IAction with ID Type Command");
		iActionTypeActionWithId.setToolTipText("This represents an IAction with ID command.");
		
		doubleClickAction = new Action() {
			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
		
		iToggleTypeAction = new Action("Toggle", Action.AS_CHECK_BOX) {
			@Override
			public void run() {
				showMessage("iAction executed.");
			}
		};
		iToggleTypeAction.setToolTipText("This represents a toggle IAction command.");
		
		iRadioTypeAction = new Action("Radio", Action.AS_RADIO_BUTTON) {
			@Override
			public void run() {
				showMessage("iAction executed.");
			}
		};
		iRadioTypeAction.setToolTipText("This represents a radio IAction command.");
		
		iDropDownTypeAction = new Action("DropDown", Action.AS_DROP_DOWN_MENU) {
			@Override
			public void run() {
				showMessage("iAction executed.");
			}
		};
		iDropDownTypeAction.setId("DropDownId");
		iDropDownTypeAction.setToolTipText("This represents a drop down IAction command.");
		iDropDownTypeAction.setMenuCreator(new IMenuCreator() {
			Menu toolBarSubMenu = null;
			Menu viewSubMenu = null;
			boolean toolBarChecked = false;
			public void dispose() {
				if (toolBarSubMenu != null) {
					toolBarSubMenu.dispose();
					toolBarSubMenu = null;
				}
				if (viewSubMenu != null) {
					viewSubMenu.dispose();
					viewSubMenu = null;
				}
			}

			public Menu getMenu(Control parent) {
				if (toolBarSubMenu != null) {
					toolBarSubMenu.dispose();
				}
				toolBarSubMenu = new Menu(parent);
				Action action = new Action("DropDown Toggle", IAction.AS_CHECK_BOX) {
					@Override
					public void runWithEvent(Event event) {
						toolBarChecked = !toolBarChecked;
					}
				};
				action.setChecked(toolBarChecked);
				new ActionContributionItem(action).fill(toolBarSubMenu, -1);
				return toolBarSubMenu;
			}

			public Menu getMenu(Menu parent) {
				if (viewSubMenu != null) {
					viewSubMenu.dispose();
				}
				viewSubMenu = new Menu(parent);
				Action action = new Action("DropDown Toggle", IAction.AS_CHECK_BOX) {
					@Override
					public void runWithEvent(Event event) {
					}
				};
				new ActionContributionItem(action).fill(viewSubMenu, -1);
				return viewSubMenu;
			}
		});
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Sample View", message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
