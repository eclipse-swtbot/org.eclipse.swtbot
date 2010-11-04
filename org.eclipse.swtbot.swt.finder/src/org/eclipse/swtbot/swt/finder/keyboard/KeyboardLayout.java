package org.eclipse.swtbot.swt.finder.keyboard;

import java.util.Collection;

public interface KeyboardLayout {

	public static final char	TAB		= '\t';
	public static final char	LF		= '\n';
	public static final char	CR		= '\r';
	public static final char	BKSP	= '\b';
	public static final char	SPACE	= ' ';

	public static final char	DELETE	= 0x7F;
	public static final char	ESC		= 0x1B;

	Collection<KeyStrokeMapping> layout();
}
