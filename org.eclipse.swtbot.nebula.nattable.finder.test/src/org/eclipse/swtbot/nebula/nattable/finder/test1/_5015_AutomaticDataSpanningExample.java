/*******************************************************************************
 * Copyright (c) 2013 Dirk Fauth and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Dirk Fauth <dirk.fauth@gmail.com> - initial API and implementation
 *    Aparna Argade (Cadence Design Systems, Inc.) - For SWTBotNatTable tests
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.test1;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.VisualRefreshCommand;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.AutomaticSpanningDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.layer.SpanningDataLayer;
import org.eclipse.nebula.widgets.nattable.persistence.command.DisplayPersistenceDialogCommandHandler;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.nebula.nattable.finder.SWTNatTableBot;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/**
 * The original example can be extracted from
 * http://www.eclipse.org/downloads/download.php?file=/nattable/releases/1.3.0/NatTableExamples-1.3.0.jar
 * It is located at folder org\eclipse\nebula\widgets\nattable\examples\_500_Layers\_501_Data
 */
@RunWith(Suite.class)
public class _5015_AutomaticDataSpanningExample {

	public SWTNatTableBot bot;
	public static NatTable nattable;
	public Shell shell;

	@Before
	public void setUp() {
		bot = new SWTNatTableBot();
		runInUIThread();
	}

	private void runInUIThread() {
		final Display display = Display.getDefault();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				shell = createShell(display, "Nebula NatTable Test1");
				nattable = createNatTable(shell);
				shell.open();
			}
		});
	}

	protected Shell createShell(final Display display, final String text) {
		Shell shell = new Shell(display);
		shell.setText(text);
		shell.setLayout(new FillLayout());
		return shell;
	}

	private NatTable createNatTable(final Shell shell) {
		/* set contents to have something to test */
		String[] propertyNames = { "columnOneNumber", "columnTwoNumber", "columnThreeNumber", "columnFourNumber",
				"columnFiveNumber" };

		IColumnPropertyAccessor<NumberValues> cpa = new ReflectiveColumnPropertyAccessor<NumberValues>(propertyNames);
		IDataProvider dataProvider = new ListDataProvider<NumberValues>(createNumberValueList(), cpa);
		AutomaticSpanningDataProvider spanningDataProvider = new AutomaticSpanningDataProvider(dataProvider, true,
				false);

		NatTable natTable = new NatTable(shell,
				new ViewportLayer(new SelectionLayer(new SpanningDataLayer(spanningDataProvider))), false);

		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());

		natTable.addConfiguration(new BodyMenuConfiguration(natTable, spanningDataProvider));

		natTable.configure();

		natTable.registerCommandHandler(new DisplayPersistenceDialogCommandHandler());

		return natTable;
	}

	private List<NumberValues> createNumberValueList() {
		List<NumberValues> result = new ArrayList<NumberValues>();

		NumberValues nv = new NumberValues();
		nv.setColumnOneNumber(5);
		nv.setColumnTwoNumber(4);
		nv.setColumnThreeNumber(3);
		nv.setColumnFourNumber(1);
		nv.setColumnFiveNumber(1);
		result.add(nv);

		nv = new NumberValues();
		nv.setColumnOneNumber(1);
		nv.setColumnTwoNumber(1);
		nv.setColumnThreeNumber(2);
		nv.setColumnFourNumber(2);
		nv.setColumnFiveNumber(3);
		result.add(nv);
		return result;
	}

	class BodyMenuConfiguration extends AbstractUiBindingConfiguration {
		private Menu bodyMenu;
		private AutomaticSpanningDataProvider dataProvider;

		public BodyMenuConfiguration(NatTable natTable, AutomaticSpanningDataProvider dataProvider) {
			this.bodyMenu = createBodyMenu(natTable).build();
			this.dataProvider = dataProvider;
		}

		protected PopupMenuBuilder createBodyMenu(final NatTable natTable) {
			return new PopupMenuBuilder(natTable).withMenuItemProvider(new IMenuItemProvider() {
				@Override
				public void addMenuItem(final NatTable natTable, Menu popupMenu) {
					MenuItem menuItem = new MenuItem(popupMenu, SWT.PUSH);
					menuItem.setText("Toggle auto spanning");
					menuItem.setEnabled(true);
					menuItem.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent event) {
							if (BodyMenuConfiguration.this.dataProvider.isAutoColumnSpan()) {
								BodyMenuConfiguration.this.dataProvider.setAutoColumnSpan(false);
								BodyMenuConfiguration.this.dataProvider.setAutoRowSpan(true);
							} else if (BodyMenuConfiguration.this.dataProvider.isAutoRowSpan()) {
								BodyMenuConfiguration.this.dataProvider.setAutoRowSpan(false);
							} else {
								BodyMenuConfiguration.this.dataProvider.setAutoColumnSpan(true);
							}
							natTable.doCommand(new VisualRefreshCommand());
						}
					});
				}
			}).withStateManagerMenuItemProvider();
		}

		@Override
		public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
			if (this.bodyMenu != null) {
				uiBindingRegistry.registerMouseDownBinding(
						new MouseEventMatcher(SWT.NONE, null, MouseEventMatcher.RIGHT_BUTTON),
						new PopupMenuAction(this.bodyMenu));
			}
		}
	}

	@After
	public void tearDown() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if (shell != null) {
					shell.dispose();
				}
			}
		});
	}
}
