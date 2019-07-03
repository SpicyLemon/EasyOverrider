package EasyOverrider;

import java.util.Map;
import java.util.Set;

/**
 * Interface to implement when you have a possibly recursive toString() method.<br>
 *
 * This interface defines two methods:
 * <ul>
 * <li>{@link #toString(Map)} - must be implemented
 * <li>{@link #primaryToString()} - only needs to be implemented if your {@link ParamList} contains primary parameters.
 * </ul>
 */
public interface RecursionPreventingToString {

    /**
     * A toString method that, as it generates the strings of parameters,
     * records the hashCodes of objects as it goes in order to prevent a recursive toString call.<br>
     *
     * Implementation often looks something like this:<br>
     * <pre>
     * {@code
     *
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
     * The default implementation just returns null.<br>
     *
     * Implementation often looks something like this:<br>
     * <pre>
     * {@code
     *
     * @Override
     * public String primaryToString() {
     *     return paramList.primaryToString(this);
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
