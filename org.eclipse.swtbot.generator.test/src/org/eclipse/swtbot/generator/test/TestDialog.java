package org.eclipse.swtbot.generator.test;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class TestDialog extends TitleAreaDialog{

	public TestDialog(Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(false);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText("test shell");
		setTitle("test shell");
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		new Combo(container, SWT.DROP_DOWN);
		return container;
	}

}
