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
     * Getter for the String that is used in a toString when a value is null.<br>
     *
     * @return A String
     */
    String getStringForNull();

    /**
     * Setter for the String that is used when a value is null.<br>
     *
     * @param stringForNull  the string to use in a toString when a value is null
     */
    void setStringForNull(String stringForNull);

    /**
     * Getter for the String that is used in a toString when a previously seen object is seen again.<br>
     *
     * @return A String
     */
    String getStringForRecursionPrevented();

    /**
     * Setter for the String that is used when a previously seen object is seen again.<br>
     *
     * @param stringForRecursionPrevented  the string to use in a toString when a previously seen object is seen again
     */
    void setStringForRecursionPrevented(String stringForRecursionPrevented);

    /**
     * Getter for the String that is used in a toString when an empty ParamList is encountered.<br>
     *
     * @return A String
     */
    String getStringForEmptyParamList();

    /**
     * Setter for the String that is used when an empty ParamList is encountered.<br>
     *
     * @param stringForEmptyParamList  the string to use in a toString when an empty ParamList is encountered
     */
    void setStringForEmptyParamList(String stringForEmptyParamList);

    /**
     * Getter for the String that is used in a toString between parameters.<br>
     *
     * @return A String
     */
    String getParameterDelimiter();

    /**
     * Setter for the String that is used between parameters.<br>
     *
     * @param parameterDelimiter  the string to use in a toString between parameters
     */
    void setParameterDelimiter(String parameterDelimiter);

    /**
     * Getter for the format String that is used in a toString to create a name/value string.<br>
     *
     * @return A String
     */
    String getNameValueFormat();

    /**
     * Setter for the format String that is used in a toString to create a name/value strings.<br>
     *
     * @param nameValueFormat  the format string to use in a toString to create a name/value string
     */
    void setNameValueFormat(String nameValueFormat);

    /**
     * Getter for the format String that is used in a toString on each parameter value.<br>
     *
     * @return A String
     */
    String getParameterValueFormat();

    /**
     * Setter for the format String that is used in a toString on each parameter value.<br>
     *
     * @param parameterValueFormat  the format string to use in a toString on each parameter value
     */
    void setParameterValueFormat(String parameterValueFormat);

    /**
     * Getter for the format String that is used in a toString to create the final toString value.<br>
     *
     * @return A String
     */
    String getToStringFormat();

    /**
     * Setter for the format String that is used in a toString to create the final toString value.<br>
     *
     * @param toStringFormat  the format string to use in a toString to create the final toString value
     */
    void setToStringFormat(String toStringFormat);

    /**
     * Getter for the format String that is used to create the IllegalArgumentException message.<br>
     *
     * @return A String
     */
    String getIllegalArgumentMessageFormat();

    /**
     * Setter for the format String that is used to create the IllegalArgumentException message for a missing parameter.
     *
     * @param illegalArgumentMessageFormat  the format string to use to create the IllegalArgumentException message for a missing parameter
     */
    void setIllegalArgumentMessageFormat(String illegalArgumentMessageFormat);

    /**
     * Getter for the function that is used to get the class name from a Class object.<br>
     *
     * @return A Function that takes in a Class and returns a String
     */
    Function<Class, String> getClassNameGetter();

    /**
     * Setter for the function that is used to get the class name from a Class object.<br>
     *
     * @param classNameGetter  the function to use to get the class name from a Class object
     */
    void setClassNameGetter(Function<Class, String> classNameGetter);

    /**
     * Getter for the function that is used to convert the hashCode to a String for the toString method.<br>
     *
     * @return A Function that takes in an Integer and returns a String
     */
    Function<Integer, String> getHashCodeToString();

    /**
     * Setter for the function that is used to convert the hashCode to a String for the toString method.<br>
     *
     * @param hashCodeToString  the function to use to convert the hashCode to a String
     */
    void setHashCodeToString(Function<Integer, String> hashCodeToString);

    /**
     * Run the provided getter on the provided object.<br>
     *
     * @param obj  the object to run the getter on
     * @param getter  the method reference to call
     * @param name  the name of the parameter (for error messages)
     * @param <O>  the type of the object
     * @param <P>  the type of the parameter (getter return value)
     * @return The results of the getter
     */
    <O, P> P get(final O obj, final Function<? super O, P> getter, String name);

    /**
     * Run the provided getter on the provided object in such a way that exceptions are prevented.<br>
     *
     * @param obj  the object to run the getter on
     * @param getter  the method reference to call
     * @param <O>  the type of the object
     * @param <P>  the type of the parameter (getter return value)
     * @return The results of the getter
     */
    <O, P> P safeGet(final O obj, final Function<? super O, P> getter);

    /**
     * Checks to see if the corresponding parameters defined by the getter are the same in both objects.<br>
     *
     * @param thisO  the first object to get the parameter from
     * @param thatO  the second object to get the parameter from
     * @param getter  the getter for the parameter to compare
     * @param name  the name of the parameter (for error messages)
     * @param <O>  the type of the object
     * @param <P>  the type of the parameter (getter return value)
     * @return True if the parameter in each of the objects are equal. False if different.
     */
    <O, P> boolean paramsAreEqual(final O thisO, final O thatO, final Function<? super O, P> getter, String name);

    /**
     * Converts a parameter value to a String.<br>
     *
     * @param obj  the object to get the parameter value from
     * @param getter  the getter to be used to get the parameter value
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param valueToStringPreventingRecursion  a reference to the method to call to prevent recursive toString calls
     * @param <O>  the type of the object
     * @param <P>  the type of the parameter (getter return value)
     * @return A String
     */
    <O, P> String paramValueToString(O obj, Function<? super O, P> getter, Map<Class, Set<Integer>> seen,
                                     BiFunction<P, Map<Class, Set<Integer>>, String> valueToStringPreventingRecursion);

    /**
     * Converts an object to a String.<br>
     *
     * @param objClass  the class of the object being converted
     * @param obj  the object to convert
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param <O>  the type of the object
     * @return A String
     */
    <O> String objectToStringPreventingRecursion(final Class<O> objClass, final O obj, final Map<Class, Set<Integer>> seen);

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
                                     BiFunction<P, Map<Class, Set<Integer>>, String> valueToStringPreventingRecursion);

    /**
     * Converts a single value to a String, preventing recursion.<br>
     *
     * @param value  the value to convert
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param paramClass  the class of the paremeter
     * @param <P>  the type of the parameter
     * @return A String
     */
    <P> String valueToStringPreventingRecursionSingle(final P value, final Map<Class, Set<Integer>> seen, Class<P> paramClass);

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
                    final P value, final Map<Class, Set<Integer>> seen, Class<E> entryClass);

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
                    final P value, final Map<Class, Set<Integer>> seen, Class<K> keyClass, Class<V> valueClass);

    /**
     * Checks the provided parameters and makes sure there's nothing wrong with them.<br>
     *
     * @param parentClass  the class of the parent object
     * @param paramDescriptionMap  the map of names to ParamDescriptions
     * @param paramOrder  the list of parameter names in the order they should be used
     * @param easyOverriderService  the easyOverriderService to use for the key pieces of functionality
     * @param <O>  the type of the object in question
     */
    <O> void validateParamListConstructorOrThrow(final Class<O> parentClass,
                                                 final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap,
                                                 final List<String> paramOrder, final EasyOverriderService easyOverriderService);

    /**
     * Gets a string of all the parameters in the provided object.<br>
     *
     * @param thisObj  the object to get the parameters from
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param paramOrder  the list of parameter names in the order they should be used
     * @param paramDescriptionMap  the map of names to ParamDescriptions
     * @param <O>  the type of the object in question
     * @return A String
     */
    <O> String getParamsString(final O thisObj, final Map<Class, Set<Integer>> seen, final List<String> paramOrder,
                               Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    /**
     * Gets a list of all the parameter descriptions.<br>
     *
     * @param paramOrder  the list of parameter names in the order they should be used
     * @param paramDescriptionMap  the map of names to ParamDescriptions
     * @param <O>  the type of the object in question
     * @return A list of ParamDescription objects
     */
    <O> List<ParamDescription<? super O, ?>> getAllParamDescriptions(final List<String> paramOrder,
                                                                     Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    /**
     * Gets a list of all the parameter descriptions that should be used in an equals method.<br>
     *
     * @param paramOrder  the list of parameter names in the order they should be used
     * @param paramDescriptionMap  the map of names to ParamDescriptions
     * @param <O>  the type of the object in question
     * @return A list of ParamDescription objects
     */
    <O> List<ParamDescription<? super O, ?>> getEqualsParamDescriptions(final List<String> paramOrder,
                                                                        Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    /**
     * Gets a list of all the parameter descriptions that should be in a hashCode method.<br>
     *
     * @param paramOrder  the list of parameter names in the order they should be used
     * @param paramDescriptionMap  the map of names to ParamDescriptions
     * @param <O>  the type of the object in question
     * @return A list of ParamDescription objects
     */
    <O> List<ParamDescription<? super O, ?>> getHashCodeParamDescriptions(final List<String> paramOrder,
                                                                          Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    /**
     * Gets a list of all the parameter descriptions that should be in a toString method.<br>
     *
     * @param paramOrder  the list of parameter names in the order they should be used
     * @param paramDescriptionMap  the map of names to ParamDescriptions
     * @param <O>  the type of the object in question
     * @return A list of ParamDescription objects
     */
    <O> List<ParamDescription<? super O, ?>> getToStringParamDescriptions(final List<String> paramOrder,
                                                                          Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

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
    <O> boolean equals(final Object thisObj, final Object thatObj, Class<O> parentClass, final List<String> paramOrder,
                       Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    /**
     * Creates a hashCode for an object.<br>
     *
     * @param thisObj  the object to get the hashCode for
     * @param paramOrder  the list of parameter names in the order they should be used
     * @param paramDescriptionMap  the map of names to ParamDescriptions
     * @param <O>  the class of the object
     * @return a hashcode
     */
    <O> int hashCode(final O thisObj, final List<String> paramOrder, Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    /**
     * Creates a String representation of the provided objecct.<br>
     *
     * @param thisObj  the object to convert
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param parentClass  the class of the parent object
     * @param paramOrder  the list of parameter names in the order they should be used
     * @param paramDescriptionMap  the map of names to ParamDescriptions
     * @param <O>  the class of the object
     * @return
     */
    <O> String toString(final O thisObj, final Map<Class, Set<Integer>> seen, Class<O> parentClass,
                        final List<String> paramOrder, Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    /**
     * Method used to make sure provided arguments aren't null.<br>
     *
     * @param obj  the object to check up on
     * @param position  the position of the argument in the list of arguments
     * @param paramName  the name of the parameter (for error messages)
     * @param methodName  the name of the method the being called is
     */
    void requireNonNull(final Object obj, final int position, final String paramName, final String methodName);
}