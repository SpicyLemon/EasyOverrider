package EasyOverrider;

import java.util.function.BiFunction;
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
 * <li>{@link ParamUsage}<code> paramUsage</code> - This is used to describe what method calls this parameter should be included in.
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
     * Gets the ParamUsage for this parameter.<br>
     *
     * @return The {@link ParamUsage} value for this parameter.
     */
    ParamUsage getParamUsage();

    /**
     * Get whether or not this should be included for the equals() method.<br>
     *
     * Uses the {@link ParamUsage#isEqualsInclude()} method.<br>
     *
     * @return True if it's to be included. False if it's to be ignored.
     */
    default boolean isEqualsInclude() {
        return getParamUsage().isEqualsInclude();
    }

    /**
     * Get whether or not this should be included for the the hashCode() method.<br>
     *
     * Uses the {@link ParamUsage#isHashCodeInclude()} method.<br>
     *
     * @return True if it's to be included. False if it's to be ignored.
     */
    default boolean isHashCodeInclude() {
        return getParamUsage().isHashCodeInclude();
    }

    /**
     * Get whether or not this should be included for the toString() method.<br>
     *
     * Uses the {@link ParamUsage#isToStringInclude()} method.<br>
     *
     * @return True if it's to be included. False if it's to be ignored.
     */
    default boolean isToStringInclude() {
        return getParamUsage().isToStringInclude();
    }

    /**
     * Gets the parameter value from the object and converts it to a String.<br>
     *
     * @param obj  the object with the parameter
     * @param objectToString  the BiFunction to use to actually create the String (and prevent recursion)
     * @return A String.
     */
    String getParamString(O obj, BiFunction<Object, Class, String> objectToString);
}
