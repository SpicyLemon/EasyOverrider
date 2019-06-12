package EasyOverrider;

/**
 * Interface to implement when a class might have a recursive toString() function.
 */
public interface RecursionPreventingToString {

    /**
     * A toString method indicating whether or not we need to prevent recursion when creating the String.
     *
     * @param preventingRecursion  the flag for whether or not to try to stop recursion
     * @return A string representation of this object.
     */
    public String toString(boolean preventingRecursion);
}
