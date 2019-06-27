package EasyOverrider;

import java.util.Map;
import java.util.Set;

/**
 * Interface to implement when a class might have a recursive toString() function.
 */
public interface RecursionPreventingToString {

    /**
     * A toString method indicating whether or not we need to prevent recursion when creating the String.
     *
     * @param seen  the map of class to sets of hashCodes of objects that have already been toString-ified.
     * @return A string representation of this object.
     */
    String toString(final Map<Class, Set<Integer>> seen);
}
