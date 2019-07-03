package EasyOverrider;

import java.util.Map;
import java.util.Set;

/**
 * This interface describes a class that will house the interesting functionality used to
 * generate toString, hashCode, and equals results using ParamList objects.<br>
 */
public interface ParamListService {

    /**
     * Gets the config for the service.<br>
     *
     * @return
     */
    ParamListServiceConfig getConfig();

    /**
     * Sets the config for the service.<br>
     *
     * @param paramListServiceConfig  the config to use
     * @return This ParamListService
     */
    ParamListService setConfig(ParamListServiceConfig paramListServiceConfig);

    /**
     * Tests if two objects are the same given the provided param description map.<br>
     *
     * @param thisObj  the first object in the comparison
     * @param thatObj  the second object in the comparison
     * @param paramList  the ParamList to operate on
     * @return true if the objects are equal, false if not
     */
    <O> boolean equals(final Object thisObj, final Object thatObj, final ParamList<O> paramList);

    /**
     * Creates a hashCode for an object.<br>
     *
     * @param thisObj  the object to get the hashCode for
     * @param paramList  the ParamList to operate on
     * @param <O>  the class of the object
     * @return a hashcode
     */
    <O> int hashCode(final O thisObj, final ParamList<O> paramList);

    /**
     * Creates a String representation of the provided object.<br>
     *
     * @param thisObj  the object to convert
     * @param paramList  the ParamList to operate on
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param <O>  the class of the object
     * @return A String.
     */
    <O> String toString(final O thisObj, final ParamList<O> paramList, final Map<Class, Set<Integer>> seen);

    /**
     * Creates a String representation of the provided object using only primary parameters.<br>
     *
     * @param thisObj  the object to convert
     * @param paramList  the ParamList to operate on
     * @param <O>  the class of the object
     * @return A String.
     */
    <O> String primaryToString(final O thisObj, final ParamList<O> paramList);
}
