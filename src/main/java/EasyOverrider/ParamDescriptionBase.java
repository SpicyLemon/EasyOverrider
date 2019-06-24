package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * An abstract class that implements most of the functionality of a ParamDescription.<br>
 *
 * It does not implement the following:
 * <ul>
 * <li>{@link ParamDescription#isCollection()}
 * <li>{@link ParamDescription#isMap()}
 * </ul>
 *
 * The extending class is also required to implement the following:
 * <ul>
 * <li>{@link ParamDescriptionBase#valueToStringPreventingRecursion(Object, Map)}
 * </ul>
 * </pre>
 *
 * @param <O>  the type of object in question
 * @param <P>  the type of the parameter in question
 */
public abstract class ParamDescriptionBase<O, P> implements ParamDescription<O, P> {
    private static final String stringNull = "null";
    private static final String recursionPrevented = "...";

    final Class<O> parentClass;
    final Class<P> paramClass;
    final String name;
    final Function<? super O, P> getter;
    final ParamMethodRestriction paramMethodRestriction;

    private static ParamList<ParamDescriptionBase> paramList;

    static ParamList<ParamDescriptionBase> getBaseParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(ParamDescriptionBase.class)
                                     .withParam("parentClass", ParamDescriptionBase::getParentClass, Class.class)
                                     .withParam("paramClass", ParamDescriptionBase::getParamClass, Class.class)
                                     .withParam("name", ParamDescriptionBase::getName, String.class)
                                     .withParam("getter", ParamDescriptionBase::getGetter, INCLUDED_IN_TOSTRING_ONLY, Function.class)
                                     .withParam("paramMethodRestriction", ParamDescriptionBase::getParamMethodRestriction, ParamMethodRestriction.class)
                                     .andThatsIt();
        }
        return paramList;
    }

    /**
     * Constructor for the extending class to use to set all the pieces pre-implemented in this abstract class
     *
     * @param parentClass  the class of the parent object
     * @param paramClass  the class of the parameter
     * @param name  the name of the parameter
     * @param getter  the getter for the parameter
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} for the parameter
     */
    ParamDescriptionBase(final Class<O> parentClass, final Class<P> paramClass, final String name,
                         final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction) {
        this.parentClass = parentClass;
        this.paramClass = paramClass;
        this.name = name;
        this.getter = getter;
        this.paramMethodRestriction = paramMethodRestriction;
    }

    @Override
    public Class<O> getParentClass() {
        return parentClass;
    }

    @Override
    public Class<P> getParamClass() {
        return paramClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Function<? super O, P> getGetter() {
        return getter;
    }

    @Override
    public ParamMethodRestriction getParamMethodRestriction() {
        return paramMethodRestriction;
    }

    @Override
    public boolean isEqualsIgnore() {
        return paramMethodRestriction.isEqualsIgnore();
    }

    @Override
    public boolean isEqualsInclude() {
        return paramMethodRestriction.isEqualsInclude();
    }

    @Override
    public boolean isHashCodeIgnore() {
        return paramMethodRestriction.isHashCodeIgnore();
    }

    @Override
    public boolean isHashCodeInclude() {
        return paramMethodRestriction.isHashCodeInclude();
    }

    @Override
    public boolean isToStringIgnore() {
        return paramMethodRestriction.isToStringIgnore();
    }

    @Override
    public boolean isToStringInclude() {
        return paramMethodRestriction.isToStringInclude();
    }

    @Override
    public P get(final O obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Cannot get the " + name + " parameter from a null object.");
        }
        return getter.apply(obj);
    }

    @Override
    public P safeGet(final O obj) {
        if (obj == null) {
            return null;
        }
        return getter.apply(obj);
    }

    @Override
    public boolean paramsAreEqual(final O thisO, final O thatO) {
        if (thisO == thatO) {
            return true;
        }
        if (thisO == null || thatO == null) {
            return false;
        }
        P thisP = get(thisO);
        P thatP = get(thatO);
        return thisP == thatP || Objects.equals(thisP, thatP);
    }

    @Override
    public String toString(final O obj, final Map<Class, Set<Integer>> seen) {
        ParamList.requireNonNull(obj, 1, "obj", "toString");
        P value = getter.apply(obj);
        if (value == null) {
            return stringNull;
        }
        return valueToStringPreventingRecursion(value, Optional.ofNullable(seen).orElseGet(HashMap::new));
    }

    /**
     * Calls objectToStringPreventingRecursion on everything in this parameter. <br><br>
     *
     * The implementation of this method should look something like
     * <pre>
     * {@code
     *
     * String valueToStringPreventingRecursion(final P value, final Map<Class, Set<Integer>> seen) {
     *     return objectToStringPreventingRecursion(paramClass, value, seen);
     * }
     * }
     * </pre>
     * or
     * <pre>
     * {@code
     *
     * String valueToStringPreventingRecursion(final P value, final Map<Class, Set<Integer>> seen) {
     *             return value.entrySet()
     *                     .stream()
     *                     .collect(Collectors.toMap(e -> objectToStringPreventingRecursion(keyClass, e.getKey(), seen),
     *                                               e -> objectToStringPreventingRecursion(valueClass, e.getValue(), seen)))
     *                     .toString();
     * }
     * }
     * </pre>
     * @param value  the value to convert to a String
     * @param seen  the map of class to sets of hashCodes of objects that have already been toString-ified.
     * @return A string.
     */
    abstract String valueToStringPreventingRecursion(final P value, final Map<Class, Set<Integer>> seen);

    /**
     * Converts an object to a string in such a way that recursion is prevented.<br>
     *
     * If the object is null, "null" is returned.<br>
     * If the object does not implement the {@link RecursionPreventingToString} interface, the standard toString() method is called.<br>
     * If the object DOES implement the {@link RecursionPreventingToString} interface then we get the objects hashcode and check it
     * against the set of objects seen for the provided class.  If the hashCode is in the set, then "..." is returned.
     * If the hashCode is NOT in the list, it is added to the list, and then the object's
     * {@link RecursionPreventingToString#toString(Map)} method is called and returned.
     *
     * @param clazz  the class of the object that we're toStringing
     * @param object  the object to toString
     * @param seen  the map of classes to sets of Integers containing all the hashCodes of objects already converted to a String
     * @param <C>  the type of the object that's being converted to a String
     * @return a string representation of the object
     */
    static <C> String objectToStringPreventingRecursion(final Class<C> clazz, final C object, final Map<Class, Set<Integer>> seen) {
        if (object == null) {
            return stringNull;
        }
        if (object instanceof RecursionPreventingToString) {
            int entryHashCode = object.hashCode();
            Map<Class, Set<Integer>> mySeen = Optional.ofNullable(seen).orElseGet(HashMap::new);
            if (!mySeen.containsKey(clazz)) {
                mySeen.put(clazz, new HashSet<>());
            }
            if (mySeen.get(clazz).contains(entryHashCode)) {
                return recursionPrevented;
            }
            mySeen.get(clazz).add(entryHashCode);
            return ((RecursionPreventingToString)object).toString(mySeen);
        }
        return object.toString();
    }

    @Override
    public String getNameValueString(final O obj, final Map<Class, Set<Integer>> seen) {
        ParamList.requireNonNull(obj, 1, "obj", "getNameValuePair");
        String value = toString(obj, Optional.ofNullable(seen).orElseGet(HashMap::new));
        return name + "=" + (value.equals(stringNull) || value.equals(recursionPrevented) ? value : "'" + value + "'");
    }

    /**
     * equals method for a ParamDescriptionBase abstract object.
     *
     * @param obj  the object to test against
     * @return True if this ParamDescriptionBase is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getBaseParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionBase abstract object.
     *
     * @return An int.
     */
    @Override
    public int hashCode() {
        return getBaseParamList().hashCode(this);
    }

    /**
     * toString method for a ParamDescriptionBase abstract object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getBaseParamList().toString(this);
    }
}
