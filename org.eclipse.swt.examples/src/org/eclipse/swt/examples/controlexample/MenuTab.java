/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Patrick Tasse - Support JFace MenuManager
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

class MenuTab extends Tab {
	/* Widgets added to the "Menu Style", "MenuItem Style" and "Other" groups */
	Button	barButton, dropDownButton, popUpButton, noRadioGroupButton, leftToRightButton, rightToLeftButton;
	Button	checkButton, cascadeButton, pushButton, radioButton, separatorButton;
	Button	createButton, closeAllButton;
	Button	imagesButton, acceleratorsButton, mnemonicsButton, subMenuButton, subSubMenuButton;
	Button	menuManagerButton, dynamicButton;
	Group	menuItemStyleGroup;

	/* Variables used to track the open shells */
	int		shellCount	= 0;
	Shell[]	shells		= new Shell[4];

	/* Map used to persist actions in dynamic menus */
	Map<Menu, List<IAction>> actions = new HashMap<Menu, List<IAction>>();

	/* Selection listener for menu items */
	SelectionListener selectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (e.widget instanceof MenuItem) {
				eventConsole.append("Clicked on menu item: ");
				eventConsole.append(getMenuItemPath((MenuItem) e.widget));
				eventConsole.append("\n");
			}
		}
	};

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	MenuTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Handle the Create button selection event.
	 * 
	 * @param event
	 *            org.eclipse.swt.events.SelectionEvent
	 */
	public void createButtonSelected(SelectionEvent event) {

		/*
		 * Remember the example shells so they can be disposed by the user.
		 */
		if (shellCount >= shells.length) {
			Shell[] newShells = new Shell[shells.length + 4];
			System.arraycopy(shells, 0, newShells, 0, shells.length);
			shells = newShells;
		}

		int orientation = 0;
		if (leftToRightButton.getSelection())
			orientation |= SWT.LEFT_TO_RIGHT;
		if (rightToLeftButton.getSelection())
			orientation |= SWT.RIGHT_TO_LEFT;
		int radioBehavior = 0;
		if (noRadioGroupButton.getSelection())
			radioBehavior |= SWT.NO_RADIO_GROUP;

		/* Create the shell and menu(s) */
		Shell shell = new Shell(SWT.SHELL_TRIM | orientation);
		hookListeners(shell);
		shells[shellCount] = shell;
		if (barButton.getSelection()) {
			if (!menuManagerButton.getSelection()) {
				createMenuBar(shell, radioBehavior);
			} else {
				createDynamicMenuBar(shell);
			}
		}

		if (popUpButton.getSelection()) {
			if (!menuManagerButton.getSelection()) {
				createPopupMenu(shell, radioBehavior);
			} else {
				createDynamicPopupMenu(shell);
			}
		}

		/* Set the size, title and open the shell. */
		Rectangle trim = shell.computeTrim(SWT.DEFAULT, SWT.DEFAULT, 300, 60);
		shell.setSize(trim.width, trim.height);
		shell.setText(ControlExample.getResourceString("Title") + shellCount);
		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawString(ControlExample.getResourceString("PopupMenuHere"), 20, 20);
			}
		});
		shell.open();
		shellCount++;
	}

	/**
	 * Close all the example shells.
	 */
	void closeAllShells() {
		for (int i = 0; i < shellCount; i++)
			if (shells[i] != null & !shells[i].isDisposed())
				shells[i].dispose();
		shellCount = 0;
	}

	/**
	 * Creates the "Control" group.
	 */
	void createControlGroup() {
		/*
		 * Create the "Control" group. This is the group on the right half of each example tab. For MenuTab, it consists
		 * of the Menu style group, the MenuItem style group and the 'other' group.
		 */
		controlGroup = new Group(tabFolderPage, SWT.NONE);
		controlGroup.setLayout(new GridLayout(2, true));
		controlGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		controlGroup.setText(ControlExample.getResourceString("Parameters"));

		/* Create a group for the menu style controls */
		styleGroup = new Group(controlGroup, SWT.NONE);
		styleGroup.setLayout(new GridLayout());
		styleGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		styleGroup.setText(ControlExample.getResourceString("Menu_Styles"));

		/* Create a group for the menu item style controls */
		menuItemStyleGroup = new Group(controlGroup, SWT.NONE);
		menuItemStyleGroup.setLayout(new GridLayout());
		menuItemStyleGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		menuItemStyleGroup.setText(ControlExample.getResourceString("MenuItem_Styles"));

		/* Create a group for the 'other' controls */
		otherGroup = new Group(controlGroup, SWT.NONE);
		otherGroup.setLayout(new GridLayout());
		otherGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		otherGroup.setText(ControlExample.getResourceString("Other"));
	}

	/**
	 * Creates the "Control" widget children.
	 */
	void createControlWidgets() {

		/* Create the menu style buttons */
		barButton = new Button(styleGroup, SWT.CHECK);
		barButton.setText("SWT.BAR");
		dropDownButton = new Button(styleGroup, SWT.CHECK);
		dropDownButton.setText("SWT.DROP_DOWN");
		popUpButton = new Button(styleGroup, SWT.CHECK);
		popUpButton.setText("SWT.POP_UP");
		noRadioGroupButton = new Button(styleGroup, SWT.CHECK);
		noRadioGroupButton.setText("SWT.NO_RADIO_GROUP");
		leftToRightButton = new Button(styleGroup, SWT.RADIO);
		leftToRightButton.setText("SWT.LEFT_TO_RIGHT");
		leftToRightButton.setSelection(true);
		rightToLeftButton = new Button(styleGroup, SWT.RADIO);
		rightToLeftButton.setText("SWT.RIGHT_TO_LEFT");

		/* Create the menu item style buttons */
		cascadeButton = new Button(menuItemStyleGroup, SWT.CHECK);
		cascadeButton.setText("SWT.CASCADE");
		checkButton = new Button(menuItemStyleGroup, SWT.CHECK);
		checkButton.setText("SWT.CHECK");
		pushButton = new Button(menuItemStyleGroup, SWT.CHECK);
		pushButton.setText("SWT.PUSH");
		radioButton = new Button(menuItemStyleGroup, SWT.CHECK);
		radioButton.setText("SWT.RADIO");
		separatorButton = new Button(menuItemStyleGroup, SWT.CHECK);
		separatorButton.setText("SWT.SEPARATOR");

		/* Create the 'other' buttons */
		imagesButton = new Button(otherGroup, SWT.CHECK);
		imagesButton.setText(ControlExample.getResourceString("Images"));
		acceleratorsButton = new Button(otherGroup, SWT.CHECK);
		acceleratorsButton.setText(ControlExample.getResourceString("Accelerators"));
		mnemonicsButton = new Button(otherGroup, SWT.CHECK);
		mnemonicsButton.setText(ControlExample.getResourceString("Mnemonics"));
		subMenuButton = new Button(otherGroup, SWT.CHECK);
		subMenuButton.setText(ControlExample.getResourceString("SubMenu"));
		subSubMenuButton = new Button(otherGroup, SWT.CHECK);
		subSubMenuButton.setText(ControlExample.getResourceString("SubSubMenu"));
		menuManagerButton = new Button(otherGroup, SWT.CHECK);
		menuManagerButton.setText(ControlExample.getResourceString("MenuManager"));
		dynamicButton = new Button(otherGroup, SWT.CHECK);
		dynamicButton.setText(ControlExample.getResourceString("Dynamic"));

		/* Create the "create" and "closeAll" buttons (and a 'filler' label to place them) */
		new Label(controlGroup, SWT.NONE);
		createButton = new Button(controlGroup, SWT.NONE);
		createButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		createButton.setText(ControlExample.getResourceString("Create_Shell"));
		closeAllButton = new Button(controlGroup, SWT.NONE);
		closeAllButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		closeAllButton.setText(ControlExample.getResourceString("Close_All_Shells"));

		/* Add the listeners */
		createButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				createButtonSelected(e);
			}
		});
		closeAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				closeAllShells();
			}
		});
		subMenuButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				subSubMenuButton.setEnabled(subMenuButton.getSelection());
			}
		});
		menuManagerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dynamicButton.setEnabled(menuManagerButton.getSelection());
				dynamicButton.setSelection(menuManagerButton.getSelection());
				noRadioGroupButton.setEnabled(!menuManagerButton.getSelection());
			}
		});

		/* Set the default state */
		barButton.setSelection(true);
		dropDownButton.setSelection(true);
		popUpButton.setSelection(true);
		cascadeButton.setSelection(true);
		checkButton.setSelection(true);
		pushButton.setSelection(true);
		radioButton.setSelection(true);
		separatorButton.setSelection(true);
		subSubMenuButton.setEnabled(subMenuButton.getSelection());
		dynamicButton.setEnabled(menuManagerButton.getSelection());
	}

	/* Create menu bar. */
	void createMenuBar(Shell shell, int radioBehavior) {
		Menu menuBar = new Menu(shell, SWT.BAR | radioBehavior);
		shell.setMenuBar(menuBar);
		hookListeners(menuBar);

		if (dropDownButton.getSelection() && cascadeButton.getSelection()) {
			/* Create cascade button and drop-down menu in menu bar. */
			MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
			item.setText(getMenuItemText("Cascade"));
			if (imagesButton.getSelection())
				item.setImage(instance.images[ControlExample.ciOpenFolder]);
			hookListeners(item);
			Menu dropDownMenu = new Menu(shell, SWT.DROP_DOWN | radioBehavior);
			item.setMenu(dropDownMenu);
			hookListeners(dropDownMenu);

			/* Create various menu items, depending on selections. */
			createMenuItems(dropDownMenu, radioBehavior, subMenuButton.getSelection(), subSubMenuButton.getSelection());
		}
	}

	/* Create dynamic menu bar using JFace MenuManager. */
	void createDynamicMenuBar(Shell shell) {
		MenuManager menuManager = new HookedMenuManager();
		final Menu menuBar = menuManager.createMenuBar((Decorations) shell);
		shell.setMenuBar(menuBar);
		hookListeners(menuBar);

		if (dropDownButton.getSelection() && cascadeButton.getSelection()) {
			/* Create cascade button and drop-down menu in menu bar. */
			MenuManager dropDownMenuManager = new HookedMenuManager(getMenuItemText("Cascade"), instance.images[ControlExample.ciOpenFolder]);
			dropDownMenuManager.setRemoveAllWhenShown(dynamicButton.getSelection());
			menuManager.add(dropDownMenuManager);

			if (dynamicButton.getSelection()) {
				dropDownMenuManager.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						/* Create various menu manager contribution items, depending on selections. */
						createMenuContributionItems(manager, menuBar, 0, subMenuButton.getSelection(), subSubMenuButton.getSelection());
					}
				});
			} else {
				createMenuContributionItems(dropDownMenuManager, menuBar, 0, subMenuButton.getSelection(), subSubMenuButton.getSelection());
			}
			menuManager.update(false);
		}

	}

	/* Create pop-up menu. */
	void createPopupMenu(Shell shell, int radioBehavior) {
		Menu popUpMenu = new Menu(shell, SWT.POP_UP | radioBehavior);
		shell.setMenu(popUpMenu);
		hookListeners(popUpMenu);

		/* Create various menu items, depending on selections. */
		createMenuItems(popUpMenu, radioBehavior, subMenuButton.getSelection(), subSubMenuButton.getSelection());
	}

	/* Create dynamic pop-up menu using JFace MenuManager. */
	void createDynamicPopupMenu(Shell shell) {
		MenuManager menuManager = new HookedMenuManager();
		menuManager.setRemoveAllWhenShown(dynamicButton.getSelection());
		final Menu popupMenu = menuManager.createContextMenu(shell);
		shell.setMenu(popupMenu);
		hookListeners(popupMenu);

		if (dynamicButton.getSelection()) {
			menuManager.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					/* Create various menu items, depending on selections. */
					createMenuContributionItems(manager, popupMenu, 0, subMenuButton.getSelection(), subSubMenuButton.getSelection());
				}
			});
		} else {
			createMenuContributionItems(menuManager, popupMenu, 0, subMenuButton.getSelection(), subSubMenuButton.getSelection());
		}
	}

	/* Create various menu items, depending on selections. */
	void createMenuItems(Menu menu, int radioBehavior, boolean createSubMenu, boolean createSubSubMenu) {
		MenuItem item;
		if (pushButton.getSelection()) {
			item = new MenuItem(menu, SWT.PUSH);
			item.setText(getMenuItemText("Push"));
			if (acceleratorsButton.getSelection())
				item.setAccelerator(SWT.MOD1 + SWT.MOD2 + 'P');
			if (imagesButton.getSelection())
				item.setImage(instance.images[ControlExample.ciClosedFolder]);
			hookListeners(item);
			item.addSelectionListener(selectionListener);
		}

		if (separatorButton.getSelection())
			new MenuItem(menu, SWT.SEPARATOR);

		if (checkButton.getSelection()) {
			item = new MenuItem(menu, SWT.CHECK);
			item.setText(getMenuItemText("Check"));
			if (acceleratorsButton.getSelection())
				item.setAccelerator(SWT.MOD1 + SWT.MOD2 + 'C');
			if (imagesButton.getSelection())
				item.setImage(instance.images[ControlExample.ciOpenFolder]);
			hookListeners(item);
			item.addSelectionListener(selectionListener);
		}

		if (radioButton.getSelection()) {
			item = new MenuItem(menu, SWT.RADIO);
			item.setText(getMenuItemText("1Radio"));
			if (acceleratorsButton.getSelection())
				item.setAccelerator(SWT.MOD1 + SWT.MOD2 + '1');
			if (imagesButton.getSelection())
				item.setImage(instance.images[ControlExample.ciTarget]);
			item.setSelection(true);
			hookListeners(item);
			item.addSelectionListener(selectionListener);

			item = new MenuItem(menu, SWT.RADIO);
			item.setText(getMenuItemText("2Radio"));
			if (acceleratorsButton.getSelection())
				item.setAccelerator(SWT.MOD1 + SWT.MOD2 + '2');
			if (imagesButton.getSelection())
				item.setImage(instance.images[ControlExample.ciTarget]);
			hookListeners(item);
			item.addSelectionListener(selectionListener);
		}

		if (createSubMenu && cascadeButton.getSelection()) {
			/* Create cascade button and drop-down menu for the sub-menu. */
			item = new MenuItem(menu, SWT.CASCADE);
			item.setText(getMenuItemText("Cascade"));
			if (imagesButton.getSelection())
				item.setImage(instance.images[ControlExample.ciOpenFolder]);
			hookListeners(item);
			Menu subMenu = new Menu(menu.getShell(), SWT.DROP_DOWN | radioBehavior);
			item.setMenu(subMenu);
			hookListeners(subMenu);
			item.addSelectionListener(selectionListener);

			createMenuItems(subMenu, radioBehavior, createSubSubMenu, false);
		}
	}

	/* Create various menu manager contribution items, depending on selections. */
	void createMenuContributionItems(IMenuManager menu, final Menu rootMenu, final int depth, final boolean createSubMenu, final boolean createSubSubMenu) {
		if (pushButton.getSelection()) {
			/* Creating a new action instance will force the old MenuItem to be disposed */
			Action action = new Action(getMenuItemText("Push"), IAction.AS_PUSH_BUTTON) {};
			if (acceleratorsButton.getSelection()) {
				action.setAccelerator(SWT.MOD1 + SWT.MOD2 + 'P');
			}
			if (imagesButton.getSelection()) {
				action.setImageDescriptor(ImageDescriptor.createFromImage(instance.images[ControlExample.ciClosedFolder]));
			}
			menu.add(action);
		}

		if (separatorButton.getSelection())
			menu.add(new Separator());

		if (checkButton.getSelection()) {
			IAction action = getAction(rootMenu, depth, getMenuItemText("Check"), IAction.AS_CHECK_BOX, false,
					SWT.MOD1 + SWT.MOD2 + 'C', ImageDescriptor.createFromImage(instance.images[ControlExample.ciOpenFolder]));
			menu.add(action);
		}

		if (radioButton.getSelection()) {
			IAction action = getAction(rootMenu, depth, getMenuItemText("1Radio"), IAction.AS_RADIO_BUTTON, true,
					SWT.MOD1 + SWT.MOD2 + '1', ImageDescriptor.createFromImage(instance.images[ControlExample.ciTarget]));
			menu.add(action);

			action = getAction(rootMenu, depth, getMenuItemText("2Radio"), IAction.AS_RADIO_BUTTON, false,
					SWT.MOD1 + SWT.MOD2 + '2', ImageDescriptor.createFromImage(instance.images[ControlExample.ciTarget]));
			menu.add(action);
		}

		if (createSubMenu && cascadeButton.getSelection()) {
			/* Create cascade button and drop-down menu for the sub-menu. */
			MenuManager subMenuManager = new HookedMenuManager(getMenuItemText("Cascade"), instance.images[ControlExample.ciOpenFolder]);
			subMenuManager.setRemoveAllWhenShown(dynamicButton.getSelection());
			menu.add(subMenuManager);

			if (dynamicButton.getSelection()) {
				subMenuManager.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						/* Create various menu items, depending on selections. */
						createMenuContributionItems(manager, rootMenu, depth + 1, createSubSubMenu, false);
					}
				});
			} else {
				createMenuContributionItems(subMenuManager, rootMenu, depth + 1, createSubSubMenu, false);
			}
		}
	}

	/*
	 * Creates an action and stores it in a map to preserve the action state.
	 * Returns the stored action if it already exists in the map for the same
	 * root menu, depth, text, and style.
	 */
	private IAction getAction(Menu rootMenu, int depth, String text, int style, boolean checked, int keycode, ImageDescriptor image) {
		List<IAction> actionList = actions.get(rootMenu);
		if (actionList == null) {
			actionList = new ArrayList<IAction>();
			actions.put(rootMenu, actionList);
			rootMenu.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					actions.remove(e.widget);
				}
			});
		}
		for (IAction action : actionList) {
			if (action.getText().equals(text) && action.getStyle() == style) {
				if (depth-- == 0) {
					return action;
				}
			}
		}
		Action action = new Action(text, style) {};
		actionList.add(action);
		if (checked) {
			action.setChecked(checked);
		}
		if (acceleratorsButton.getSelection()) {
			action.setAccelerator(keycode);
		}
		if (imagesButton.getSelection()) {
			action.setImageDescriptor(image);
		}
		return action;
	}

	String getMenuItemText(String item) {
		boolean cascade = item.equals("Cascade");
		boolean mnemonic = mnemonicsButton.getSelection();
		boolean accelerator = acceleratorsButton.getSelection();
		char acceleratorKey = item.charAt(0);
		if (mnemonic && accelerator && !cascade)
			return ControlExample.getResourceString(item + "WithMnemonic") + "\tCtrl+Shift+" + acceleratorKey;
		if (accelerator && !cascade)
			return ControlExample.getResourceString(item) + "\tCtrl+Shift+" + acceleratorKey;
		if (mnemonic)
			return ControlExample.getResourceString(item + "WithMnemonic");
		return ControlExample.getResourceString(item);
	}

	public String getMenuItemPath(MenuItem menuItem) {
		StringBuilder sb = new StringBuilder(menuItem.getText());
		if ((menuItem.getStyle() & SWT.SEPARATOR) != 0) {
			sb.append("|");
		}
		Menu menu = menuItem.getParent();
		while (menu != null) {
			MenuItem item = menu.getParentItem();
			if (item != null) {
				sb.insert(0, item.getText() + " > ");
			} else if ((menu.getStyle() & SWT.BAR) != 0) {
				sb.insert(0, "BAR > ");
			} else if ((menu.getStyle() & SWT.POP_UP) != 0) {
				sb.insert(0, "POP_UP > ");
			}
			menu = menu.getParentMenu();
		}
		return sb.toString();
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText() {
		return "Menu";
	}

	/* Menu manager that hooks listeners to its widgets as they are created */
	private class HookedMenuManager extends MenuManager {
		private Image image;

		public HookedMenuManager() {
			this(null, null);
		}

		public HookedMenuManager(String text, Image image) {
			super(text);
			this.image = image;
		}

		@Override
		protected void doItemFill(IContributionItem ci, int index) {
			super.doItemFill(ci, index);
			Item item = getMenuItem(index);
			if (imagesButton.getSelection() && ci instanceof HookedMenuManager) {
				item.setImage(((HookedMenuManager) ci).image);
			}
			hookListeners(item);
			if (item instanceof MenuItem) {
				MenuItem menuItem = (MenuItem) item;
				Menu menu = menuItem.getMenu();
				if (menu != null) {
					hookListeners(menu);
				}
				menuItem.addSelectionListener(selectionListener);
			}
		}
	}

}
