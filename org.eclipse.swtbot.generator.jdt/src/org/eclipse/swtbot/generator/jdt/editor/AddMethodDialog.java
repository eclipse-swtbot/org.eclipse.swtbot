package org.eclipse.swtbot.generator.jdt.editor;

import java.util.List;

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
import org.eclipse.swtbot.generator.jdt.editor.document.Method;

public class AddMethodDialog extends TitleAreaDialog {

	private String title;
	private String methodName;
	private Text methodText;
	private List<Method> methods;

	public AddMethodDialog(Shell shell, List<Method> methods) {
		super(shell);
		this.methods= methods;
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER | SWT.RESIZE
				| getDefaultOrientation());
		title = "Add new method";
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setHelpAvailable(false);
		getShell().setText(title);
		setTitle(title);

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(gridData);

		final Label methodLabel = new Label(container, SWT.NONE);
		methodLabel.setText("Method name:");

		GridData dataMethodText = new GridData();
		dataMethodText.grabExcessHorizontalSpace = true;
		dataMethodText.horizontalAlignment = GridData.FILL;
		methodText = new Text(container, SWT.SINGLE | SWT.BORDER);
		methodText.setLayoutData(dataMethodText);
		methodText.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent arg0) {
				for(Method m: methods){
					if(m.getName().equals(methodText.getText())){
						getButton(IDialogConstants.OK_ID).setEnabled(false);
						setMessage("Method " + methodText.getText()+" already exists!", IMessageProvider.WARNING);
						return;
					}
				}
				if (!methodText.getText().isEmpty() && !methodText.getText().contains(" ")) {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					setMessage("");
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setMessage("Method name " + methodText.getText()
							+ " is invalid!", IMessageProvider.WARNING);
				}
				
			}
		});

		return getShell();
	}

	public String getMethodName() {
		return methodName;
	}

	@Override
	protected void okPressed() {
		methodName = methodText.getText();
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(300, 200);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent){
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

}
