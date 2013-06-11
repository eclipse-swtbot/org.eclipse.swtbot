package org.eclipse.swtbot.generator.test;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.junit.Assert;
import org.junit.Test;

public class RecordComboTest extends AbstractGeneratorTest {

	@Override
	public void populateTestArea(Composite composite) {
		new Combo(composite, SWT.DROP_DOWN);
	}

	@Test
	public void testModifyCombo() {
		this.bot.comboBox().setText("kikoo");
		flushEvents();
		Assert.assertEquals("bot.comboBox().setText(\"kikoo\");",
				this.bot.shell("SWTBot Test Recorder").bot().text().getText().trim());
	}

}
