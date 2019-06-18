package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
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
 * @param <E>  the type of entry contained in the parameter (if it's a collection or map)
 */
public abstract class ParamDescriptionBase<O, P, E> implements ParamDescription<O, P, E> {
    private static final String stringNull = "null";
    private static final String recursionPrevented = "...";

    final Class<O> parentClass;
    final Class<P> paramClass;
    final Class<E> entryClass;
    final String name;
    final Function<? super O, P> getter;
    final ParamMethodRestriction paramMethodRestriction;

    private static ParamList<ParamDescriptionBase> paramList;

    static ParamList<ParamDescriptionBase> getBaseParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(ParamDescriptionBase.class)
                                     .withParam("parentClass", ParamDescriptionBase::getParentClass, Class.class)
                                     .withParam("paramClass", ParamDescriptionBase::getParamClass, Class.class)
                                     .withParam("entryClass", ParamDescriptionBase::getEntryClass, Class.class)
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
     * @param entryClass  the class of the entry - used for interaction with collections and lists
     * @param name  the name of the parameter
     * @param getter  the getter for the parameter
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} for the parameter
     */
    ParamDescriptionBase(final Class<O> parentClass, final Class<P> paramClass,
                         final Class<E> entryClass, final String name,
                         final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction) {
        this.parentClass = parentClass;
        this.paramClass = paramClass;
        this.entryClass = entryClass;
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
    public Class<E> getEntryClass() {
        return entryClass;
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
        ParamList.requireNonNull(seen, 2, "seen", "toString");
        P value = getter.apply(obj);
        if (value == null) {
            return stringNull;
        }
        if (RecursionPreventingToString.class.isAssignableFrom(entryClass)) {
            return valueToStringPreventingRecursion(value, seen);
        }
        return value.toString();
    }

    /**
     * Calls entryToStringPreventingRecursion on every entry in this parameter. <br><br>
     *
     * The implementation of this method should look something like
     * <pre>
     * {@code
     *
     * String valueToStringPreventingRecursion(final P value,
     *                                         final Map<Class, Set<Integer>> seen) {
     *     //Go through every entry in the provided value,
     *     //Do something with calling entryToStringPreventingRecursion(entry, seen)
     * }
     * }
     * </pre>
     * @param value  the value to convert to a String
     * @param seen  the map of class to sets of hashCodes of objects that have already been toString-ified.
     * @return A string.
     */
    abstract String valueToStringPreventingRecursion(final P value, final Map<Class, Set<Integer>> seen);

    /**
     * This method should be used by the valueToStringPreventingRecursion in order to get the string of an entry. <br><br>
     *
     * If possible, it uses {@link RecursionPreventingToString#toString(Map)}, and properly checks and adds to that map.
     * Otherwise, it just uses the plain toString() method on the entry.
     *
     * @param entry  the entry you want the string of - if null, "null" is returned
     * @param seen  the map of class to sets of hashCodes of objects that have already been toString-ified.
     * @return A string of the provided entry.
     */
    String entryToStringPreventingRecursion(final E entry, final Map<Class, Set<Integer>> seen) {
        if (entry == null) {
            return stringNull;
        }
        if (entry instanceof RecursionPreventingToString) {
            int entryHashCode = entry.hashCode();
            if (!seen.containsKey(entryClass)) {
                seen.put(entryClass, new HashSet<>());
            }
            if (seen.get(entryClass).contains(entryHashCode)) {
                return recursionPrevented;
            }
            seen.get(entryClass).add(entryHashCode);
            return ((RecursionPreventingToString)entry).toString(seen);
        }
        return entry.toString();
    }

    @Override
    public String getNameValueString(final O obj, final Map<Class, Set<Integer>> seen) {
        ParamList.requireNonNull(obj, 1, "obj", "getNameValuePair");
        ParamList.requireNonNull(seen, 2, "seen", "getNameValuePair");
        String value = toString(obj, seen);
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
