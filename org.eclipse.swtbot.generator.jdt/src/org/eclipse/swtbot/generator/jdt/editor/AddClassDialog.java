package org.eclipse.swtbot.generator.jdt.editor;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddClassDialog extends TitleAreaDialog{
	
	private String title;
	private String className;
	private Text classText;

	public AddClassDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER | SWT.RESIZE
				| getDefaultOrientation());
		title = "Add new class";
		setHelpAvailable(false);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(title);
		setTitle(title);

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(gridData);

		final Label classLabel = new Label(container, SWT.NONE);
		classLabel.setText("Class name: ");
		
		GridData dataClassText = new GridData();
		dataClassText.grabExcessHorizontalSpace = true;
		dataClassText.horizontalAlignment = GridData.FILL;
		classText = new Text(container, SWT.SINGLE | SWT.BORDER);
		classText.setLayoutData(dataClassText);
		classText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent arg0) {
				
				if (!classText.getText().isEmpty() && !classText.getText().contains(" ")) {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					setMessage("");
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setMessage("Class name " + classText.getText()
							+ " is invalid!", IMessageProvider.WARNING);
				}

			}
		});
		return getShell();
	}
	
	@Override
	protected void okPressed() {
		className = classText.getText();
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(300, 200);
	}

	public String getClassName() {
		return className;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent){
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

}
