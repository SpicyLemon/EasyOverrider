package EasyOverrider;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * This represents a parameter in an object and how to get and set it. <br>
 *
 * There are five pieces used to describe a parameter.<br>
 * <ul>
 * <li><code>Class parentClass</code> - This is the class that the parameter exists in.
 * <li><code>Class paramClass</code> - This is the raw class of the parameter.
 * <li><code>String name</code> - This is the name of the parameter (usually the variable name).
 * <li><code>Function getter</code> - This is a reference to the getter for the parameter.
 * <li>{@link ParamMethodRestriction}<code> paramMethodRestriction</code> - This is used to describe what method calls this parameter should be included in.
 * </ul>
 *
 * @param <O>  the type of object in question
 * @param <P>  the type of the parameter in question
 */
public interface ParamDescription<O, P> {

    /**
     * Gets the class of the parent object that this parameter is part of.<br>
     *
     * @return A Class of the parent object.
     */
    Class<O> getParentClass();

    /**
     * Gets the class of the parameter that this is describing.<br>
     *
     * @return A Class of the parameter.
     */
    Class<P> getParamClass();

    /**
     * Gets the name of the parameter.<br>
     *
     * @return The param name String as provided in the constructor.
     */
    String getName();

    /**
     * Gets the getter for the parameter.<br>
     *
     * @return The getter Function as provided in the constructor.
     */
    Function<? super O, P> getGetter();

    /**
     * Gets the ParamMethodRestriction for this parameter.<br>
     *
     * @return The {@link ParamMethodRestriction} value for this parameter.
     */
    ParamMethodRestriction getParamMethodRestriction();

    /**
     * Sets the service to use for injectable functionality.<br>
     *
     * @param easyOverriderService  the service you want to use
     */
    void setService(final EasyOverriderService easyOverriderService);

    /**
     * Get whether or not this should be ignored for the equals() method.<br>
     *
     * @return True if it's to be ignored. False if it's to be included.
     */
    boolean isEqualsIgnore();

    /**
     * Get whether or not this should be included for the equals() method.<br>
     *
     * @return True if it's to be included. False if it's to be ignored.
     */
    boolean isEqualsInclude();

    /**
     * Get whether or not this should be ignored for the the hashCode() method.<br>
     *
     * @return True if it's to be ignored. False if it's to be included.
     */
    boolean isHashCodeIgnore();

    /**
     * Get whether or not this should be included for the the hashCode() method.<br>
     *
     * @return True if it's to be included. False if it's to be ignored.
     */
    boolean isHashCodeInclude();

    /**
     * Get whether or not this should be ignored for the toString() method.<br>
     *
     * @return True if it's to be ignored. False if it's to be included.
     */
    boolean isToStringIgnore();

    /**
     * Get whether or not this should be included for the toString() method.<br>
     *
     * @return True if it's to be included. False if it's to be ignored.
     */
    boolean isToStringInclude();

    /**
     * Gets the name/value string for this parameter given the provided object, and preventing recursion if needed.<br>
     *
     * @param obj  the object to get the name/value string of
     * @param seen  the map of class to sets of hashCodes of objects that have already been toString-ified.
     * @return A String
     */
    String getNameValueString(final O obj, final Map<Class, Set<Integer>> seen);
}
