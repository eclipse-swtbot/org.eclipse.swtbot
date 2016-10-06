/*******************************************************************************
 * Copyright (c) 2013 Dirk Fauth and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Dirk Fauth <dirk.fauth@gmail.com> - initial API and implementation
 *    Aparna Argade (Cadence Design Systems, Inc.) - Simplification for SWTBotNatTable tests
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.test2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.ExtendedReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditBindings;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsSortModel;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.filterrow.DefaultGlazedListsFilterStrategy;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowHeaderComposite;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.nebula.nattable.finder.SWTNatTableBot;
import org.junit.After;
import org.junit.Before;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TransformedList;

/**
 * The original example can be extracted from
 * http://www.eclipse.org/downloads/download.php?file=/nattable/releases/1.3.0/NatTableExamples-1.3.0.jar
 * It is located at folder org\eclipse\nebula\widgets\nattable\examples\_800_Integration
 *
 * Example showing a NatTable that contains a column header and a body layer. It
 * adds sorting, filtering, editing features. This example produces only 2
 * columns in the natTable to have simplicity of example.
 */
public class _801_VerticalCompositionWithFeaturesExample {

	public SWTNatTableBot bot;
	private Shell shell;

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
				shell = createShell(display, "Nebula NatTable Test2");
				createNatTable(shell);
				shell.open();
			}
		});
	}

	protected Shell createShell(final Display display, final String text) {
		Shell shell = new Shell(display);
		shell.setText(text);
		shell.setSize(400, 500);
		shell.setLayout(new FillLayout());
		return shell;
	}

	private Control createNatTable(final Shell shell) {
		ConfigRegistry configRegistry = new ConfigRegistry();

		Composite panel = new Composite(shell, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		panel.setLayout(layout);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(panel);

		Composite gridPanel = new Composite(panel, SWT.NONE);
		gridPanel.setLayout(layout);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(gridPanel);

		Composite buttonPanel = new Composite(panel, SWT.NONE);
		buttonPanel.setLayout(new GridLayout());
		GridDataFactory.fillDefaults().grab(false, false).applyTo(buttonPanel);

		// property names of the Person class
		String[] propertyNames = { "firstName", "lastName", "gender", "married", "birthday" };

		// mapping from property to label, needed for column header labels
		Map<String, String> propertyToLabelMap = new HashMap<String, String>();
		propertyToLabelMap.put("firstName", "Firstname");
		propertyToLabelMap.put("lastName", "Lastname");
		propertyToLabelMap.put("gender", "Gender");
		propertyToLabelMap.put("married", "Married");
		propertyToLabelMap.put("birthday", "Birthday");

		IColumnPropertyAccessor<Person> columnPropertyAccessor = new ExtendedReflectiveColumnPropertyAccessor<Person>(
				propertyNames);

		List<Person> values = PersonService.getPersons(30);
		final EventList<Person> eventList = GlazedLists.eventList(values);
		TransformedList<Person, Person> rowObjectsGlazedList = GlazedLists.threadSafeList(eventList);

		// use the SortedList constructor
		SortedList<Person> sortedList = new SortedList<Person>(rowObjectsGlazedList, null);
		// wrap the SortedList with the FilterList
		FilterList<Person> filterList = new FilterList<Person>(sortedList);

		IRowDataProvider<Person> bodyDataProvider = new ListDataProvider<Person>(filterList, columnPropertyAccessor);
		final DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		bodyDataLayer.setConfigLabelAccumulator(new ColumnLabelAccumulator());
		GlazedListsEventLayer<Person> eventLayer = new GlazedListsEventLayer<Person>(bodyDataLayer, filterList);
		final SelectionLayer selectionLayer = new SelectionLayer(eventLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
		DataLayer columnHeaderDataLayer = new DataLayer(columnHeaderDataProvider);
		ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);

		// add sorting
		SortHeaderLayer<Person> sortHeaderLayer = new SortHeaderLayer<Person>(columnHeaderLayer,
				new GlazedListsSortModel<Person>(sortedList, columnPropertyAccessor, configRegistry,
						columnHeaderDataLayer),
				false);

		// add the filter row functionality
		final FilterRowHeaderComposite<Person> filterRowHeaderLayer = new FilterRowHeaderComposite<Person>(
				new DefaultGlazedListsFilterStrategy<Person>(filterList, columnPropertyAccessor, configRegistry),
				sortHeaderLayer, columnHeaderDataProvider, configRegistry);

		// set the region labels to make default configurations work, e.g.
		// editing, selection
		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, filterRowHeaderLayer, 0, 0);
		compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);

		// add edit configurations
		compositeLayer.addConfiguration(new DefaultEditConfiguration());
		compositeLayer.addConfiguration(new DefaultEditBindings());

		final NatTable natTable = new NatTable(gridPanel, compositeLayer, false);

		natTable.setConfigRegistry(configRegistry);

		// adding this configuration adds the styles and the painters to use
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());

		// add sorting configuration
		natTable.addConfiguration(new SingleClickSortConfiguration());

		natTable.addConfiguration(new AbstractRegistryConfiguration() {

			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
						IEditableRule.ALWAYS_EDITABLE);

				// birthday is never editable
				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
						IEditableRule.NEVER_EDITABLE, DisplayMode.NORMAL,
						ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 4);

				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR,
						new ComboBoxCellEditor(Arrays.asList(Boolean.TRUE, Boolean.FALSE)), DisplayMode.NORMAL,
						ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 3);
				configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
						new DefaultBooleanDisplayConverter(), DisplayMode.NORMAL,
						ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 3);

			}
		});
		natTable.configure();
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
		return panel;
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
