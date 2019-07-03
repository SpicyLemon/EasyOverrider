package EasyOverrider;

import java.util.Map;
import java.util.Set;

/**
 * Interface to implement for easy overriding.<br>
 *
 * This interface has four methods, only one of which does not have a default implementation:
 * <ul>
 * <li>{@link #getParamList()} - must be implemented
 * <li>{@link #toString(Map)}
 * <li>{@link #primaryToString()}
 * <li>{@link #getThis()}
 * </ul>
 *
 * @param <B> the type of thing you're implementing this on
 * @see EasyOverriderMethods
 */
public interface EasyOverrider<B> {

    /**
     * A method that returns this class's ParamList.<br>
     *
     * It's best practice to do something like this:
     * <pre>
     * {@code
     *
     * private static ParamList<Foo> paramList = null;
     * public paramList<Foo> getParamList() {
     *     if (paramList == null) {
     *         paramList = ParamList.forClass(Foo.class)
     *                              .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY)
     *                              .withParam("name", Foo::getName)
     *                              .withParam("bar", Foo::getBar)
     *                              .andThatsIt();
     *     }
     *     return paramList;
     * }
     * }
     * </pre>
     *
     * @return A {@link ParamList} for the extending class.
     */
    ParamList<B> getParamList();

    /**
     * A toString method that, as it generates the strings of parameters,
     * records the hashCodes of objects as it goes in order to prevent a recursive toString call.<br>
     *
     * Implementation often looks something like this:<br>
     * <pre>
     * {@code
     *
     * public String toString(final Map<Class, Set<Integer>> seen) {
     *     return getParamList().toString(this, seen);
     * }
     * }
     * </pre>
     *
     * @param seen  the map of class to sets of hashCodes of objects that have already been toString-ified.
     * @return A String representation of this object.
     */
    default String toString(final Map<Class, Set<Integer>> seen) {
        return getParamList().toString(getThis(), seen);
    }

    /**
     * A toString method that uses only the primary parameters.<br>
     *
     * Implementation often looks something like this:<br>
     * <pre>
     * {@code
     *
     * @Override
     * public String primaryToString() {
     *     return getParamList().primaryToString(this);
     * }
     * }
     * </pre>
     *
     * @return A String of the object containing only primary parameter information.
     */
    default String primaryToString() {
        return getParamList().primaryToString(getThis());
    }

    /**
     * Casts this into a B as expected by the ParamList methods.
     *
     * @return A B.
     */
    @SuppressWarnings("unchecked")
    default B getThis() {
        return (B)this;
    }
}
