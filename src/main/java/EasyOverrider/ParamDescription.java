package EasyOverrider;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * This represents a parameter in an object and how to get and set it. <br>
 *
 * There are four pieces used to describe a parameter.<br>
 * <ul>
 * <li><code>String name</code> - This is the name of the parameter (usually the variable name).
 * <li><code>Function getter</code> - This is a reference to the getter for the parameter.
 * <li>{@link ParamMethodRestriction}<code> paramMethodRestriction</code> - This is used to describe what method calls this parameter should be included in.
 * <li><code>boolean preventToStringRecursion</code> - A flag for whether or not this field might cause recursion in a <code>paramValueToString()</code> function.
 * If set to true on a field that is included in the <code>paramValueToString()</code>, then a <code>paramValueToString(boolean)</code> method is looked for in the parameter's class.
 * The idea is that the parameter's <code>paramValueToString(boolean)</code> method will then restrict the list of parameters used in the paramValueToString
 * so as to not call back to the object being described in here.
 * </ul>
 * Example paramValueToString methods to help prevent paramValueToString recursion:
 * <pre>
 * {@code
 *
 * public string paramValueToString() {
 *     return paramValueToString(false);
 * }
 *
 * public String paramValueToString(final boolean preventingRecursion) {
 *     return paramList.paramValueToString(this, preventingRecursion);
 * }
 * }
 * </pre>
 *
 * @param <O>  the type of object in question
 * @param <P>  the type of the parameter in question
 */
public interface ParamDescription<O, P> {

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
     * Sets the service to use for injectable functionality
     *
     * @param easyOverriderService  the service you want to use
     */
    void setService(final EasyOverriderService easyOverriderService);

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
     * Get whether or not this should be ignored for the paramValueToString() method.
     *
     * @return True if it's to be ignored. False if it's to be included.
     */
    boolean isToStringIgnore();

    /**
     * Get whether or not this should be included for the paramValueToString() method.
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
     * If the parameter is null, "null" is returned.<br>
     *
     * If there's a recursionPreventingToString available, and we're not preventing recursion, the recursionPreventingToString
     * is called using a true preventingRecursion flag.
     * If there's a recursionPreventingToString available, and we ARE preventing recursion, "..." is returned.
     *
     * @param obj  the object to turn into a string - cannot be null
     * @param seen  the map of classes to sets of hashCodes of objects that have already been paramValueToString-ified.
     *
     * @return A String. Either "null", "..." or the results of paramValueToString on the parameter in the provided object.
     * @throws IllegalArgumentException if the object is null.
     */
    String paramValueToString(final O obj, final Map<Class, Set<Integer>> seen);

    /**
     * Gets the name/value string for this parameter given the provided object, and preventing recursion if needed.
     *
     * @param obj  the object to get the name/value string of
     * @param seen  the map of class to sets of hashCodes of objects that have already been paramValueToString-ified.
     * @return A string in the form of "name='value'" or "name=null" or "name=...".
     * @throws IllegalArgumentException if the object is null.
     */
    String getNameValueString(final O obj, final Map<Class, Set<Integer>> seen);
}
