package EasyOverrider;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This interface describes a class that will house the interesting functionality used to
 * generate toString, hashCode, and equals results using ParamList objects.<br>
 */
public interface EasyOverriderService {

    /**
     * Gets the config for the service.<br>
     *
     * @return
     */
    EasyOverriderConfig getConfig();

    /**
     * Sets the config for the service.<br>
     *
     * @param easyOverriderConfig  the config to use
     * @return This EasyOverriderService
     */
    EasyOverriderService setConfig(EasyOverriderConfig easyOverriderConfig);

    /**
     * Creates a name/value String for a parameter in an object.<br>
     *
     * @param obj  the object to get the parameter from
     * @param name  the name of the parameter
     * @param getter  the method that gets the parameter
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param valueToStringPreventingRecursion  a reference to the method to call to prevent recursive toString calls
     * @param <O>  the type of the object
     * @param <P>  the type of the parameter (getter return value)
     * @return A String
     */
    <O, P> String getNameValueString(final O obj, String name, final Function<? super O, P> getter,
                                     final Map<Class, Set<Integer>> seen,
                                     final BiFunction<P, Map<Class, Set<Integer>>, String> valueToStringPreventingRecursion);

    /**
     * Converts a single value to a String, preventing recursion.<br>
     *
     * @param value  the value to convert
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param paramClass  the class of the paremeter
     * @param <P>  the type of the parameter
     * @return A String
     */
    <P> String valueToStringPreventingRecursionSingle(final P value, final Map<Class, Set<Integer>> seen, final Class<P> paramClass);

    /**
     * Converts a Collection value to a String, preventing recursion.<br>
     *
     * @param value  the collection to convert
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param entryClass  the class of the entries in the collection
     * @param <E>  the type of the entries in the collection
     * @param <P>  the type of the parameter
     * @return A String
     */
    <E, P extends Collection<? extends E>> String valueToStringPreventingRecursionCollection(
                    final P value, final Map<Class, Set<Integer>> seen, final Class<E> entryClass);

    /**
     * Converts a Map value to a String, preventing recursion.<br>
     *
     * @param value  the map to convert
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param keyClass  the class of the keys in the map
     * @param valueClass  the class of the values in the map
     * @param <K>  the type of the keys
     * @param <V>  the type of the values
     * @param <P>  the type of the parameter
     * @return
     */
    <K, V, P extends Map<? extends K, ? extends V>> String valueToStringPreventingRecursionMap(
                    final P value, final Map<Class, Set<Integer>> seen, final Class<K> keyClass, final Class<V> valueClass);

    /**
     * Tests if two objects are the same given the provided param description map.<br>
     *
     * @param thisObj  the first object in the comparison
     * @param thatObj  the second object in the comparison
     * @param parentClass  the class of the parent object
     * @param paramOrder  the list of parameter names in the order they should be used
     * @param paramDescriptionMap  the map of names to ParamDescriptions
     * @return A list of ParamDescription objects
     */
    <O> boolean equals(final Object thisObj, final Object thatObj, final Class<O> parentClass, final List<String> paramOrder,
                       final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

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
