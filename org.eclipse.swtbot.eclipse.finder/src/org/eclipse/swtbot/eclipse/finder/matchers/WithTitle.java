package org.eclipse.swtbot.eclipse.finder.matchers;

import static org.hamcrest.Matchers.equalTo;

import org.eclipse.swtbot.swt.finder.matchers.AbstractMatcher;
import org.eclipse.ui.IWorkbenchPartReference;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * @author michal [at] greenpath [dot] pl
 * @author Michal Borek
 * @version $Id$
 * @since 2.1.2
 */
public class WithTitle<T extends IWorkbenchPartReference> extends
		AbstractMatcher<T> {

	private final Matcher<String> titleMatcher;

	/**
	 * @param titleMatcher
	 *            the part name matcher.
	 */
	public WithTitle(Matcher<String> titleMatcher) {
		this.titleMatcher = titleMatcher;
	}

	public boolean doMatch(Object item) {
		if (item instanceof IWorkbenchPartReference) {
			IWorkbenchPartReference part = (IWorkbenchPartReference) item;
			return titleMatcher.matches(part.getTitle());
		}
		return false;
	}

	public void describeTo(Description description) {
		description.appendText("with title '").appendDescriptionOf(titleMatcher)
				.appendText("'");
	}

	/**
	 * Matches a workbench part (view/editor) with the specified title.
	 * 
	 * @param text
	 *            the title of the part.
	 * @return a matcher.
	 * @since 2.1.2
	 */
	@Factory
	public static <T extends IWorkbenchPartReference> Matcher<T> withTitle(
			String text) {
		return withTitle(equalTo(text));
	}

	/**
	 * Matches a workbench part (view/editor) with the specified title.
	 * 
	 * @param titleMatcher
	 *            the part title matcher.
	 * @return a matcher.
	 * @since 2.1.2
	 */
	@Factory
	public static <T extends IWorkbenchPartReference> Matcher<T> withTitle(
			Matcher<String> titleMatcher) {
		return new WithTitle<T>(titleMatcher);
	}
}
