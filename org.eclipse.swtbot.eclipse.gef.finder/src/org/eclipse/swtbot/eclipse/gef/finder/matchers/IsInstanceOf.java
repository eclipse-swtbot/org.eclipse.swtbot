/*  Copyright (c) 2000-2009 hamcrest.org
 */
package org.eclipse.swtbot.eclipse.gef.finder.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;


/**
 * Tests whether the value is an instance of a class.
 */
public class IsInstanceOf<T> extends BaseMatcher<T> {
    private final Class<T> theClass;

    /**
     * Creates a new instance of IsInstanceOf
     *
     * @param theClass The predicate evaluates to true for instances of this class
     *                 or one of its subclasses.
     */
    public IsInstanceOf(Class<T> theClass) {
        this.theClass = theClass;
    }

    public boolean matches(Object item) {
        return theClass.isInstance(item);
    }

    public void describeTo(Description description) {
        description.appendText("an instance of ")
                .appendText(theClass.getName());
    }
    
    /**
     * Is the value an instance of a particular type?
     */
    public static <T> Matcher<T> instanceOf(Class<T> type) {
        return new IsInstanceOf<T>(type);
    }
}