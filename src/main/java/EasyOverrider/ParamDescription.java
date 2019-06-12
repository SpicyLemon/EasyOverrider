package EasyOverrider;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This represents a parameter in an object and how to get and set it. <br>
 *
 * There are four pieces used to describe a parameter.<br>
 * <ul>
 * <li><code>String name</code> - This is the name of the parameter (usually the variable name).
 * <li><code>Function getter</code> - This is a reference to the getter for the parameter.
 * <li>{@link ParamMethodRestriction}<code> paramMethodRestriction</code> - This is used to describe what method calls this parameter should be included in.
 * <li><code>boolean preventToStringRecursion</code> - A flag for whether or not this field might cause recursion in a <code>toString()</code> function.
 * If set to true on a field that is included in the <code>toString()</code>, then a <code>toString(boolean)</code> method is looked for in the parameter's class.
 * The idea is that the parameter's <code>toString(boolean)</code> method will then restrict the list of parameters used in the toString
 * so as to not call back to the object being described in here.
 * </ul>
 * Example toString methods to help prevent toString recursion:
 * <pre>
 * {@code
 *
 * public string toString() {
 *     return toString(false);
 * }
 *
 * public String toString(final boolean preventingRecursion) {
 *     return paramList.toString(this, preventingRecursion);
 * }
 * }
 * </pre>
 *
 * @param <O>  the type of object in question
 * @param <P>  the type of the parameter in question
 * @param <E>  the type of entry contained in the parameter (if it's a collection or map)
 */
public interface ParamDescription<O, P, E> {

    /**
     * Gets the class of the parent object that this parameter is part of.
     *
     * @return A Class
     */
    Class<O> getParentClass();

    /**
     * Gets the class of the parameter that this is describing.
     *
     * @return A Class
     */
    Class<P> getParamClass();

    /**
     * Gets the class of the entries in this parameter (only appicable if it's a collection or map).
     *
     * @return A Class
     */
    Class<E> getEntryClass();

    /**
     * Gets the name of the parameter.
     *
     * @return The param name String as provided in the constructor.
     */
    String getName();

    /**
     * Gets the getter for the parameter.
     *
     * @return The getter Function as provided in the constructor.
     */
    Function<? super O, P> getGetter();

    /**
     * Gets the ParamMethodRestriction for this parameter.
     *
     * @return a {@link ParamMethodRestriction} value.
     */
    ParamMethodRestriction getParamMethodRestriction();

    /**
     * Getter for the recursion preventing toString method that takes in a prevent recursion boolean.
     *
     * @return A reference to the <code>toString(boolean)</code> method needed.
     */
    BiFunction<? super E, Boolean, String> getRecursionPreventingToString();

    /**
     * Gets whether or not this is a collection parameter.
     *
     * @return Whether or not this is a collection.
     */
    boolean isCollection();

    /**
     * Gets whether or not this is a map parameter.
     *
     * @return Whether or not this is a map.
     */
    boolean isMap();

    /**
     * Get whether or not this should be ignored for the equals() method.
     *
     * @return True if it's to be ignored. False if it's to be included.
     */
    boolean isEqualsIgnore();

    /**
     * Get whether or not this should be included for the equals() method.
     *
     * @return True if it's to be included. False if it's to be ignored.
     */
    boolean isEqualsInclude();

    /**
     * Get whether or not this should be ignored for the the hashCode() method.
     *
     * @return True if it's to be ignored. False if it's to be included.
     */
    boolean isHashCodeIgnore();

    /**
     * Get whether or not this should be included for the the hashCode() method.
     *
     * @return True if it's to be included. False if it's to be ignored.
     */
    boolean isHashCodeInclude();

    /**
     * Get whether or not this should be ignored for the toString() method.
     *
     * @return True if it's to be ignored. False if it's to be included.
     */
    boolean isToStringIgnore();

    /**
     * Get whether or not this should be included for the toString() method.
     *
     * @return True if it's to be included. False if it's to be ignored.
     */
    boolean isToStringInclude();

    /**
     * Runs the getter on the provided object.
     *
     * @param obj  the object you want to get the parameter from - cannot be null
     * @return The result of the getter when given the provided object.
     * @throws IllegalArgumentException if the provided obj is null.
     * @see #safeGet(Object)
     */
    P get(final O obj);

    /**
     * Runs the getter on the provided object. If the provided obj is null, null is returned.
     *
     * @param obj  the object you want to get the parameter from
     * @return The result of the getter when given the provided object.
     * @see #get(Object)
     */
    P safeGet(final O obj);

    /**
     * Tests whether or not this param is equal in the two objects.
     *
     * @param thisO  the main object we're checking on
     * @param thatO  the other object we're checking on
     * @return True if the paremeter desribed in here is the same in both objects.
     */
    boolean paramsAreEqual(final O thisO, final O thatO);

    /**
     * Get the String of this parameter from the provided object. <br>
     *
     * If the parameter is null, "null" is returned.
     * Calls {@link #toString(Object, boolean)} with a false preventingRecursion flag.
     *
     * @param obj  the object to get the parameter from - cannot be null
     * @return A String of the parameter.
     * @see #toString(Object, boolean)
     */
    String toString(final O obj);

    /**
     * Get the String of this parameter from the provided object. <br>
     *
     * If the parameter is null, "null" is returned.<br>
     *
     * If there's a recursionPreventingToString available, and we're not preventing recursion, the recursionPreventingToString
     * is called using a true preventingRecursion flag.
     * If there's a recursionPreventingToString available, and we ARE preventing recursion, "..." is returned.
     * @param obj  the object to get the parameter from - cannot be null
     * @param preventingRecursion  the flag for whether or not we're trying to prevent recursion on this parameter
     * @return A String. Either "null", "..." or the results of toString on the parameter in the provided object.
     * @throws IllegalArgumentException if the object is null.
     */
    String toString(final O obj, final boolean preventingRecursion);

    /**
     * Gets the name/value string for this parameter given the provided object. <br>
     *
     * Calls {@link #getNameValueString(Object, boolean)} with a false preventRecursion flag.
     *
     * @param obj  the object to get the parameter from
     * @return A String in the form of "name='value'" or "name=null".
     * @see #getNameValueString(Object, boolean)
     */
    String getNameValueString(final O obj);

    /**
     * Gets the name/value string for this parameter given the provided object, and preventing recursion if needed.
     *
     * @param obj  the object to get the parameter from
     * @param preventingRecursion  the flag for whether or not we're trying to prevent recursion on this parameter
     * @return A string in the form of "name='value'" or "name=null" or "name=...".
     * @throws IllegalArgumentException if the object is null.
     */
    String getNameValueString(final O obj, final boolean preventingRecursion);
}
