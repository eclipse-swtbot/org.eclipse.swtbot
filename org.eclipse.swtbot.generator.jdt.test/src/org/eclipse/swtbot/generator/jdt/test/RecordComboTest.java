package org.eclipse.swtbot.generator.jdt.test;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.junit.Assert;
import org.junit.Test;

public class RecordComboTest extends AbstractJDTGeneratorTest {

	private String combo = "bot.comboBox().setText(\"kikoo\");";
	private String text = "import org.eclipse.swtbot.eclipse.finder.SWTBotEclipseTestCase;" + 
	 "public class FirstClass extends SWTBotEclipseTestCase {public void firstMethod() {";


	@Override
	protected void contributeToDialog(Composite container) {
		new Combo(container, SWT.DROP_DOWN);
	}

	@Test
	public void testModifyCombo() {
		bot.waitUntil(shellIsActive("test shell"),2000);
		this.bot.comboBox().setText("kikoo");
		flushEvents();
		String expectedText = (text + combo + "}}").replaceAll("\\s", "");
		String fixedText = this.bot.shell("SWTBot Test Recorder").bot().styledText().getText().replaceAll("\\s", "");
		Assert.assertEquals(expectedText, fixedText);
	}

}
