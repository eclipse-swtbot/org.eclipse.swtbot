package org.eclipse.swtbot.swt.finder.widgets;

import java.io.IOException;

import org.eclipse.swt.examples.dnd.DNDExample;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTShellTest;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

public class DnDTreeTest extends AbstractSWTShellTest {

	@Override
	protected void createUI(Composite parent) {
		// TODO Auto-generated method stub
		new DNDExample().open(shell);
	}

	@Test
	public void dragsAndDropsFromTreeToTree() throws Exception {
		Assume.assumeFalse("Drag and drop does not work with Xvnc", isUsingXvnc());
		bot.comboBoxInGroup("Widget", 0).setSelection("Tree");
		bot.comboBoxInGroup("Widget", 1).setSelection("Tree");
		final SWTBotTree sourceTree = bot.tree(0);
		final SWTBotTree targetTree = bot.tree(1);
		final SWTBotTreeItem sourceItem = sourceTree.getTreeItem("Drag Source name 0");
		final SWTBotTreeItem targetItem = targetTree.getTreeItem("Drop Target name 0");

		sourceItem.dragAndDrop(targetItem);

		for (SWTBotTreeItem item : sourceTree.getAllItems()) {
			if (item.getText().equals("Drag Source name 0")) {
				Assert.fail("Drag Source item should have disappeared from source tree");
			}
		}
		SWTBotTreeItem droppedNote = targetItem.getNode("Drag Source name 0");
		Assert.assertNotNull("Could not find dropped node", droppedNote);
	}

	public static boolean isUsingXvnc() throws Exception {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("nix") < 0 && os.indexOf("nux") < 0 && os.indexOf("aix") < 0) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return false;
		}
		String xdisplay = System.getenv("DISPLAY"); //$NON-NLS-1$
		// command is like    pgrep -l -f "Xvnc.*:0"
		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("/usr/bin/pgrep -l -f "); //$NON-NLS-1$
		commandBuilder.append("X.*"); //$NON-NLS-1$
		commandBuilder.append(xdisplay);
		Process proc = Runtime.getRuntime().exec(commandBuilder.toString());
		proc.waitFor();
		// If pgrep found something, it will return 0;
		return proc.exitValue() == 0;
	}


}
