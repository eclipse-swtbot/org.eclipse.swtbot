package org.eclipse.swtbot.generator.test;

import org.junit.Assert;
import org.junit.Test;

public class RecordComboTest extends AbstractGeneratorTest {

	@Test
	public void testModifyCombo() {
		this.bot.comboBox().setText("kikoo");
		flushEvents();
		Assert.assertEquals("bot.comboBox().setText(\"kikoo\");",
				this.bot.shell("SWTBot Test Recorder").bot().text().getText().trim());
	}

}
