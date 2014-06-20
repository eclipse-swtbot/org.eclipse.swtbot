// Generated source. DO NOT MODIFY.
// To add new new methods, please see README file in the generator plugin.
package org.eclipse.swtbot.e4.finder.matchers;

@SuppressWarnings("rawtypes")
public class WidgetMatcherFactory extends org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory {

	/**
	 * Matches a perspective with the specified label.
	 *
	 * @param label the label of the perspective.
	 * @return a matcher.
	 * @since 2.2.2
	 */
	public static org.hamcrest.Matcher withPerspectiveLabel(final java.lang.String label) {
		return org.eclipse.swtbot.e4.finder.matchers.WithPerspectiveLabel.withPerspectiveLabel(label);
	}

	/**
	 * Matches a perspective with the specified id.
	 *
	 * @param id the id of the perspective.
	 * @return a matcher.
	 * @since 2.2.2
	 */
	public static org.hamcrest.Matcher withPerspectiveId(final java.lang.String id) {
		return  org.eclipse.swtbot.e4.finder.matchers.WithPerspectiveId.withPerspectiveId(id);
	}

	/**
	 * Matches a workbench part with the specified name.
	 *
	 * @param text the label of the part.
	 * @return a matcher.
	 * @since 2.2.2
	 */
	public static <T extends org.eclipse.e4.ui.model.application.ui.basic.MPart> org.hamcrest.Matcher<T> withPartName(final java.lang.String text) {
		return org.eclipse.swtbot.e4.finder.matchers.WithPartName.withPartName(text);
	}

	/**
	 * Matches a workbench part with the specified id.
	 *
	 * @param id the id of the part.
	 * @return a matcher.
	 * @since 2.2.2
	 */
	public static <T extends org.eclipse.e4.ui.model.application.ui.basic.MPart> org.hamcrest.Matcher<T> withPartId(final java.lang.String id) {
		return org.eclipse.swtbot.e4.finder.matchers.WithPartId.withPartId(id);
	}

}
