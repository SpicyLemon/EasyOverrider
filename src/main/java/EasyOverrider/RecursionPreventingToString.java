package EasyOverrider;

import java.util.Map;
import java.util.Set;

/**
 * Interface to implement when a class might have a recursive toString() function.<br>
 *
 * Implementation of this interface is looked for during toString calls.
 * If the class of a parameter that's being converted implements this interface,
 * the {@link #toString(Map)} method is used instead of the {@link #toString()} method.
 *
 * @see EasyOverriderPreventingRecursiveToString
 */
public interface RecursionPreventingToString {

    /**
     * A toString method that, as it generates the strings of parameters,
     * records the hashCodes of objects as it goes in order to prevent a recursive toString call.
     *
     * @param seen  the map of class to sets of hashCodes of objects that have already been toString-ified.
     * @return A string representation of this object.
     */
    String toString(final Map<Class, Set<Integer>> seen);
}
