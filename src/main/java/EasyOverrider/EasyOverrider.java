package EasyOverrider;

import java.util.Map;
import java.util.Set;

/**
 * Interface to implement when a class might have a recursive toString() function.<br>
 *
 * Implementation of this interface is looked for during toString calls.
 * If the class of a parameter that's being converted implements this interface,
 * the {@link #toString(Map)} method is used instead of the {@link #toString()} method.<br>
 *
 * Additionally, if recursion is found, the {@link #primaryToString()} method is used.
 * If that returns null (default behavior),
 * then the <code>stringForRecursionPrevented</code> from {@link EasyOverriderService} is used.
 *
 * @see EasyOverriderPreventingRecursiveToString
 */
public interface EasyOverrider {

    /**
     * A toString method that, as it generates the strings of parameters,
     * records the hashCodes of objects as it goes in order to prevent a recursive toString call.<br>
     *
     * Implementation often looks something like this:<br>
     * <pre>
     * {@code
     *
     * @Override
     * public String toString(final Map<Class, Set<Integer>> seen) {
     *     return paramList.toString(this, seen);
     * }
     * }
     * </pre>
     *
     * @param seen  the map of class to sets of hashCodes of objects that have already been toString-ified.
     * @return A String representation of this object.
     */
    String toString(final Map<Class, Set<Integer>> seen);

    /**
     * A toString method that uses only the primary parameters.<br>
     *
     * By default, this returns null.
     * That signals to the EasyOverriderService to use the <code>stringForRecursionPrevented</code>.
     * If your ParamList contains a primary parameter, though, you can implement this method.<br>
     *
     * Implementation often looks something like this:<br>
     * <pre>
     * {@code
     *
     * @Override
     * public String primaryToString() {
     *     return paramList.primaryToString();
     * }
     * }
     * </pre>
     *
     * @return A String of the object containing only primary parameter information.
     */
    default String primaryToString() {
        return null;
    }
}
