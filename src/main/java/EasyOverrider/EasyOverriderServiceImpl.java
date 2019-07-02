package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The default implementation of a EasyOverriderService.<br>
 */
public class EasyOverriderServiceImpl implements EasyOverriderService {

    String stringForNull = "null";
    String stringForRecursionPrevented = "...";
    String stringForEmptyParamList = " ";
    String parameterDelimiter = ", ";
    String nameValueFormat = "%1$s=%2$s";
    String parameterValueFormat = "'%1$s'";
    String toStringFormat = "%1$s@%2$s [%3$s]";
    String illegalArgumentMessageFormat = "Argument %1$s (%2$s) provided to %3$s cannot be null.";
    Function<Class, String> classNameGetter = Class::getCanonicalName;
    Function<Integer, String> hashCodeToString = Integer::toHexString;

    private static ParamList<EasyOverriderServiceImpl> paramList;

    static ParamList<EasyOverriderServiceImpl> getParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(EasyOverriderServiceImpl.class)
                                 .withParam("stringForNull", EasyOverriderServiceImpl::getStringForNull, String.class)
                                 .withParam("stringForRecursionPrevented",
                                            EasyOverriderServiceImpl::getStringForRecursionPrevented,
                                            String.class)
                                 .withParam("stringForEmptyParamList",
                                            EasyOverriderServiceImpl::getStringForEmptyParamList,
                                            String.class)
                                 .withParam("parameterDelimiter", EasyOverriderServiceImpl::getParameterDelimiter, String.class)
                                 .withParam("nameValueFormat", EasyOverriderServiceImpl::getNameValueFormat, String.class)
                                 .withParam("parameterValueFormat", EasyOverriderServiceImpl::getParameterValueFormat, String.class)
                                 .withParam("toStringFormat", EasyOverriderServiceImpl::getToStringFormat, String.class)
                                 .withParam("illegalArgumentMessageFormat",
                                            EasyOverriderServiceImpl::getIllegalArgumentMessageFormat,
                                            String.class)
                                 .withParam("classNameGetter",
                                            EasyOverriderServiceImpl::getClassNameGetter,
                                            INCLUDED_IN_TOSTRING_ONLY,
                                            Function.class)
                                 .withParam("hashCodeToString",
                                            EasyOverriderServiceImpl::getHashCodeToString,
                                            INCLUDED_IN_TOSTRING_ONLY,
                                            Function.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor that just uses all the default values.<br>
     */
    public EasyOverriderServiceImpl() { }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>"null"</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #paramValueToString(Object, Function, Map, BiFunction)
     * @see #objectToStringPreventingRecursion(Class, Object, Map)
     * @see #valueToStringPreventingRecursionCollection(Collection, Map, Class)
     * @see #valueToStringPreventingRecursionMap(Map, Map, Class, Class)
     */
    @Override
    public String getStringForNull() {
        return stringForNull;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>"null"</code>.<br>
     *
     * @param stringForNull  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided String is null
     * @see #paramValueToString(Object, Function, Map, BiFunction)
     * @see #objectToStringPreventingRecursion(Class, Object, Map)
     * @see #valueToStringPreventingRecursionCollection(Collection, Map, Class)
     * @see #valueToStringPreventingRecursionMap(Map, Map, Class, Class)
     */
    @Override
    public EasyOverriderService setStringForNull(final String stringForNull) {
        requireNonNull(stringForNull, 1, "stringForNull", "setStringForNull");
        this.stringForNull = stringForNull;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>"..."</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #objectToStringPreventingRecursion(Class, Object, Map)
     */
    @Override
    public String getStringForRecursionPrevented() {
        return stringForRecursionPrevented;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>"..."</code>.<br>
     *
     * @param stringForRecursionPrevented  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided String is null
     * @see #objectToStringPreventingRecursion(Class, Object, Map)
     */
    @Override
    public EasyOverriderService setStringForRecursionPrevented(final String stringForRecursionPrevented) {
        requireNonNull(stringForRecursionPrevented, 1, "stringForRecursionPrevented", "setStringForRecursionPrevented");
        this.stringForRecursionPrevented = stringForRecursionPrevented;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>" "</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #getParamsString(Object, Map, List, Map)
     */
    @Override
    public String getStringForEmptyParamList() {
        return stringForEmptyParamList;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>" "</code>.<br>
     *
     * @param stringForEmptyParamList  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided String is null
     * @see #getParamsString(Object, Map, List, Map)
     */
    @Override
    public EasyOverriderService setStringForEmptyParamList(final String stringForEmptyParamList) {
        requireNonNull(stringForEmptyParamList, 1, "stringForEmptyParamList", "setStringForEmptyParamList");
        this.stringForEmptyParamList = stringForEmptyParamList;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>", "</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #getParamsString(Object, Map, List, Map)
     */
    @Override
    public String getParameterDelimiter() {
        return parameterDelimiter;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>", "</code>.<br>
     *
     * @param parameterDelimiter  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided String is null
     * @see #getParamsString(Object, Map, List, Map)
     */
    @Override
    public EasyOverriderService setParameterDelimiter(final String parameterDelimiter) {
        requireNonNull(parameterDelimiter, 1, "parameterDelimiter", "setParameterDelimiter");
        this.parameterDelimiter = parameterDelimiter;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>"%1$s=%2$s"</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #getNameValueString(Object, String, Function, Map, BiFunction)
     */
    @Override
    public String getNameValueFormat() {
        return nameValueFormat;
    }

    /**
     * {@inheritDoc}
     *
     * When using this format String, two values will be provided in this order: name, value.<br>
     *
     * Default value is <code>"%1$s=%2$s"</code>.<br>
     *
     * @param nameValueFormat  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     * @see #getNameValueString(Object, String, Function, Map, BiFunction)
     */
    @Override
    public EasyOverriderService setNameValueFormat(final String nameValueFormat) {
        requireNonNull(nameValueFormat, 1, "nameValueFormat", "setNameValueFormat");
        try {
            String.format(nameValueFormat, "name", "value");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setNameValueFormat is not a valid format string.", e);
        }
        this.nameValueFormat = nameValueFormat;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>"'%1$s'"</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #getNameValueString(Object, String, Function, Map, BiFunction)
     */
    @Override
    public String getParameterValueFormat() {
        return parameterValueFormat;
    }

    /**
     * {@inheritDoc}
     *
     * When using this format String, one value will be provided.<br>
     *
     * Default value is <code>"'%1$s'"</code>.<br>
     *
     * @param parameterValueFormat  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     * @see #getNameValueString(Object, String, Function, Map, BiFunction)
     */
    @Override
    public EasyOverriderService setParameterValueFormat(final String parameterValueFormat) {
        requireNonNull(parameterValueFormat, 1, "parameterValueFormat", "setParameterValueFormat");
        try {
            String.format(parameterValueFormat, "parameter");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setParameterValueFormat is not a valid format string.", e);
        }
        this.parameterValueFormat = parameterValueFormat;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>"%1$s@%2$s [%3$s]"</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #toString(Object, Map, Class, List, Map)
     */
    @Override
    public String getToStringFormat() {
        return toStringFormat;
    }

    /**
     * {@inheritDoc}
     *
     * When using this format String, three values will be provided in this order: class, hexed hash code, parameter name/value pairs.<br>
     *
     * Default value is <code>"%1$s@%2$s [%3$s]"</code>.<br>
     *
     * @param toStringFormat  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     * @see #toString(Object, Map, Class, List, Map)
     */
    @Override
    public EasyOverriderService setToStringFormat(final String toStringFormat) {
        requireNonNull(toStringFormat, 1, "toStringFormat", "setToStringFormat");
        try {
            String.format(toStringFormat, "class", "hashcode", "paramslist");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setToStringFormat is not a valid format string.", e);
        }
        this.toStringFormat = toStringFormat;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>"Argument %1$s (%2$s) provided to %3$s cannot be null."</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #requireNonNull(Object, int, String, String)
     */
    @Override
    public String getIllegalArgumentMessageFormat() {
        return illegalArgumentMessageFormat;
    }

    /**
     * {@inheritDoc}
     *
     * When using this format String, three values will be provided in this order: position, parameter name, method name.<br>
     *
     * Default value is <code>"Argument %1$s (%2$s) provided to %3$s cannot be null."</code>.<br>
     *
     * @param illegalArgumentMessageFormat  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     */
    @Override
    public EasyOverriderService setIllegalArgumentMessageFormat(final String illegalArgumentMessageFormat) {
        requireNonNull(illegalArgumentMessageFormat, 1, "illegalArgumentMessageFormat", "setIllegalArgumentMessageFormat");
        try {
            String.format(illegalArgumentMessageFormat, 1, "name", "method");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setIllegalArgumentMessageFormat is not a valid format string.", e);
        }
        this.illegalArgumentMessageFormat = illegalArgumentMessageFormat;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>Class::getCanonicalName</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #toString(Object, Map, Class, List, Map)
     */
    @Override
    public Function<Class, String> getClassNameGetter() {
        return classNameGetter;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>Class::getCanonicalName</code>.<br>
     *
     * @param classNameGetter  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided Function is null
     * @see #toString(Object, Map, Class, List, Map)
     */
    @Override
    public EasyOverriderService setClassNameGetter(final Function<Class, String> classNameGetter) {
        requireNonNull(classNameGetter, 1, "classNameGetter", "setClassNameGetter");
        this.classNameGetter = classNameGetter;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>Integer::toHexString</code>.<br>
     *
     * @return {@inheritDoc}
     * @see #toString(Object, Map, Class, List, Map)
     */
    @Override
    public Function<Integer, String> getHashCodeToString() {
        return hashCodeToString;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>Integer::toHexString</code>.<br>
     *
     * @param hashCodeToString  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided Function is null
     * @see #toString(Object, Map, Class, List, Map)
     */
    @Override
    public EasyOverriderService setHashCodeToString(final Function<Integer, String> hashCodeToString) {
        this.hashCodeToString = hashCodeToString;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param obj  {@inheritDoc} - cannot be null
     * @param getter  {@inheritDoc} - cannot be null
     * @param name  {@inheritDoc}
     * @param <O>  {@inheritDoc}
     * @param <P>  {@inheritDoc}
     * @return  {@inheritDoc}
     * @throws IllegalArgumentException if either the provided obj or getter is null
     */
    @Override
    public <O, P> P get(final O obj, final Function<? super O, P> getter, final String name) {
        requireNonNull(obj, 1, "obj", "get: " + name);
        requireNonNull(getter, 2, "getter", "get: " + name);
        return getter.apply(obj);
    }

    /**
     * {@inheritDoc}
     *
     * @param obj  {@inheritDoc}
     * @param getter  {@inheritDoc}
     * @param <O>  {@inheritDoc}
     * @param <P>  {@inheritDoc}
     * @return If either the object or getter are null, null is returned. Otherwise, the result of the getter on the object is returned.
     */
    @Override
    public <O, P> P safeGet(final O obj, final Function<? super O, P> getter) {
        if (obj == null || getter == null) {
            return null;
        }
        return getter.apply(obj);
    }

    /**
     * {@inheritDoc}
     *
     * If <code>thisO == thatO</code>, true is returned.<br>
     * Then, if either of them are null, false is returned.<br>
     * Then, the parameters are retrieved. If they are equal using == or equal using Objects.equals then true is returned.<br>
     * Otherwise, false is returned.<br>
     *
     * @param thisO  {@inheritDoc}
     * @param thatO  {@inheritDoc}
     * @param getter  {@inheritDoc} - cannot be null
     * @param name  {@inheritDoc}
     * @param <O>  {@inheritDoc}
     * @param <P>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided getter is null
     */
    @Override
    public <O, P> boolean paramsAreEqual(final O thisO, final O thatO, final Function<? super O, P> getter, final String name) {
        requireNonNull(getter, 3, "getter", "paramsAreEqual: " + name);
        if (thisO == thatO) {
            return true;
        }
        if (thisO == null || thatO == null) {
            return false;
        }
        P thisP = safeGet(thisO, getter);
        P thatP = safeGet(thatO, getter);
        return thisP == thatP || Objects.equals(thisP, thatP);
    }

    /**
     * {@inheritDoc}
     *
     * Runs the getter on the object. If that value is null, {@link #getStringForNull()} is used.
     * Otherwise the provided <code>valueToStringPreventingRecursion</code> method is called with the value and the provided seen map.<br>
     *
     * @param obj  {@inheritDoc} - cannot be null
     * @param getter  {@inheritDoc} - cannot be null
     * @param seen  {@inheritDoc} - cannot be null
     * @param valueToStringPreventingRecursion  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @param <P>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if any parameter is null
     */
    @Override
    public <O, P> String paramValueToString(final O obj, final Function<? super O, P> getter, final Map<Class, Set<Integer>> seen,
                                            final BiFunction<P, Map<Class, Set<Integer>>, String> valueToStringPreventingRecursion) {
        requireNonNull(obj, 1, "obj", "paramValueToString");
        requireNonNull(getter, 2, "getter", "paramValueToString");
        requireNonNull(seen, 3, "seen", "paramValueToString");
        requireNonNull(getter, 4, "valueToStringPreventingRecursion", "paramValueToString");
        P value = getter.apply(obj);
        if (value == null) {
            return getStringForNull();
        }
        return valueToStringPreventingRecursion.apply(value, seen);
    }

    /**
     * {@inheritDoc}
     *
     * If the provided object is null, {@link #getStringForNull()} is returned.<br>
     * Otherwise, if the object is an instance of {@link RecursionPreventingToString}, then
     * the hashCode of the object is calculated.
     * If the hashCode is already in the seen map, {@link RecursionPreventingToString#primaryToString()} is called.
     * If that is not null, it is returned. Otherwise, {@link #getStringForRecursionPrevented()} is returned.
     * If the hashCode is NOT already in the seen map, the hashCode is added to the seen map, the object's
     * {@link RecursionPreventingToString#toString(Map)} method is called and it's result is returned.<br>
     * If the object is NOT an instance of {@link RecursionPreventingToString},
     * then the standard {@link Object#toString()} method is called on the object and returned.<br>
     *
     * @param objClass  {@inheritDoc} - cannot be null
     * @param obj  {@inheritDoc}
     * @param seen  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if either the objClass or seen parameters are null
     * @see RecursionPreventingToString
     */
    @Override
    public <O> String objectToStringPreventingRecursion(final Class<O> objClass, final O obj, final Map<Class, Set<Integer>> seen) {
        requireNonNull(objClass, 1, "objClass", "objectToStringPreventingRecursion");
        requireNonNull(seen, 3, "seen", "objectToStringPreventingRecursion");
        if (obj == null) {
            return getStringForNull();
        }
        if (obj instanceof RecursionPreventingToString) {
            if (!seen.containsKey(objClass)) {
                seen.put(objClass, new HashSet<>());
            }
            RecursionPreventingToString recursiveObject = (RecursionPreventingToString)obj;
            int entryHashCode = obj.hashCode();
            if (seen.get(objClass).contains(entryHashCode)) {
                return Optional.ofNullable(recursiveObject.primaryToString())
                               .orElseGet(() -> createToStringResult(obj, objClass, getStringForRecursionPrevented()));
            }
            seen.get(objClass).add(entryHashCode);
            return recursiveObject.toString(seen);
        }
        return obj.toString();
    }

    /**
     * {@inheritDoc}
     *
     * First, the value is converted to a String using {@link #paramValueToString(Object, Function, Map, BiFunction)}.
     * Then, if that result is not the {@link #getStringForNull()} or {@link #getStringForRecursionPrevented()} values,
     * the {@link #getParameterValueFormat()} is applied to it.<br>
     *
     * Finally, the {@link #getNameValueFormat()} is applied, being provided the <code>name</code> and value created above.<br>
     *
     * @param obj  {@inheritDoc} - cannot be null
     * @param name  {@inheritDoc}
     * @param getter  {@inheritDoc} - cannot be null
     * @param seen  {@inheritDoc} - cannot be null
     * @param valueToStringPreventingRecursion  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @param <P>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the obj, getter, seen or valueToStringPreventingRecursion parameters are null
     */
    @Override
    public <O, P> String getNameValueString(final O obj, final String name, final Function<? super O, P> getter,
                                            final Map<Class, Set<Integer>> seen,
                                            final BiFunction<P, Map<Class, Set<Integer>>, String> valueToStringPreventingRecursion) {
        requireNonNull(obj, 1, "obj", "getNameValueString: " + name);
        requireNonNull(getter, 3, "getter", "getNameValueString: " + name);
        requireNonNull(seen, 4, "seen", "getNameValueString: " + name);
        requireNonNull(valueToStringPreventingRecursion, 5, "valueToStringPreventingRecursion", "getNameValueString: " + name);
        String value = paramValueToString(obj, getter, seen, valueToStringPreventingRecursion);
        if (!value.equals(getStringForNull()) && !value.equals(getStringForRecursionPrevented())) {
            value = String.format(getParameterValueFormat(), value);
        }
        return String.format(getNameValueFormat(), name, value);
    }

    /**
     * {@inheritDoc}
     *
     * Simply passes the provided info to {@link #objectToStringPreventingRecursion(Class, Object, Map)}.<br>
     *
     * @param value  {@inheritDoc}
     * @param seen  {@inheritDoc} - cannot be null
     * @param paramClass  {@inheritDoc} - cannot be null
     * @param <P>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the seen or paramClass parameters are null
     */
    @Override
    public <P> String valueToStringPreventingRecursionSingle(final P value, final Map<Class, Set<Integer>> seen,
                                                             final Class<P> paramClass) {
        return objectToStringPreventingRecursion(paramClass, value, seen);
    }

    /**
     * {@inheritDoc}
     *
     * If the provided value is null, {@link #getStringForNull()} is returned.<br>
     *
     * Otherwise, it is looped through and all entries are converted to
     * Strings using {@link #objectToStringPreventingRecursion(Class, Object, Map)}.
     * The results are collected into a List of Strings and then finally
     * turned into one big String using {@link List#toString()}<br>
     *
     * @param value  {@inheritDoc}
     * @param seen  {@inheritDoc} - cannot be null
     * @param entryClass  {@inheritDoc} - cannot be null
     * @param <E>  {@inheritDoc}
     * @param <P>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the seen or entryClass parameters are null
     */
    @Override
    public <E, P extends Collection<? extends E>> String valueToStringPreventingRecursionCollection(
                    final P value, final Map<Class, Set<Integer>> seen, final Class<E> entryClass) {
        requireNonNull(seen, 2, "seen", "valueToStringPreventingRecursionCollection");
        requireNonNull(entryClass, 3, "entryClass", "valueToStringPreventingRecursionCollection");
        if (value == null) {
            return getStringForNull();
        }
        return value.stream()
                    .map(e -> objectToStringPreventingRecursion(entryClass, e, seen))
                    .collect(Collectors.toList())
                    .toString();
    }

    /**
     * {@inheritDoc}
     *
     * If the provided value is null, {@link #getStringForNull()} is returned.<br>
     *
     * Otherwise, all entries are looped through and all keys and values are converted to
     * Strings using {@link #objectToStringPreventingRecursion(Class, Object, Map)}.
     * The results are collected into a Map of Strings to Strings and then finally
     * turned into one big String using {@link Map#toString()}<br>
     *
     * @param value  {@inheritDoc}
     * @param seen  {@inheritDoc} - cannot be null
     * @param keyClass  {@inheritDoc} - cannot be null
     * @param valueClass  {@inheritDoc} - cannot be null
     * @param <K>  {@inheritDoc}
     * @param <V>  {@inheritDoc}
     * @param <P>  {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public <K, V, P extends Map<? extends K, ? extends V>> String valueToStringPreventingRecursionMap(
                    final P value, final Map<Class, Set<Integer>> seen, final Class<K> keyClass, final Class<V> valueClass) {
        requireNonNull(seen, 2, "seen", "valueToStringPreventingRecursionMap");
        requireNonNull(keyClass, 2, "keyClass", "valueToStringPreventingRecursionMap");
        requireNonNull(valueClass, 2, "valueClass", "valueToStringPreventingRecursionMap");
        if (value == null) {
            return getStringForNull();
        }
        return value.entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> objectToStringPreventingRecursion(keyClass, e.getKey(), seen),
                                              e -> objectToStringPreventingRecursion(valueClass, e.getValue(), seen)))
                    .toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param parentClass  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param easyOverriderService  {@inheritDoc} - cannot be null
     * @param <O> {@inheritDoc}
     * @throws IllegalArgumentException if any of the parameters are null
     * @throws IllegalArgumentException if the paramOrder list is not the same size as the paramDescriptionMap
     * @throws IllegalArgumentException if any entry in the paramOrder list does not have a
     * corresponding entry in the paramDescriptionMap
     */
    @Override
    public <O> void validateParamListConstructorOrThrow(final Class<O> parentClass,
                                                        final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap,
                                                        final List<String> paramOrder,
                                                        final EasyOverriderService easyOverriderService) {
        requireNonNull(parentClass, 1, "parentClass", "ParamList constructor");
        requireNonNull(paramDescriptionMap, 2, "paramDescriptionMap", "ParamList constructor");
        requireNonNull(paramOrder, 3, "paramOrder", "ParamList constructor");
        requireNonNull(easyOverriderService, 4, "easyOverriderService", "ParamList constructor");
        if (paramOrder.size() != paramDescriptionMap.size()) {
            throw new IllegalArgumentException("The size of the paramDescriptionMap [" + paramDescriptionMap.size() + "] " +
                                               "does not equal the size of the paramOrder list [" + paramOrder.size() + "]");
        }
        if (!paramOrder.stream().allMatch(paramDescriptionMap::containsKey)) {
            throw new IllegalArgumentException("Parameter names were found in the order list " +
                                               "that do not exist in the paramDescriptionMap: " +
                                               paramOrder.stream()
                                                         .filter(name -> !paramDescriptionMap.containsKey(name))
                                                         .collect(Collectors.joining(", ")));
        }
    }

    /**
     * {@inheritDoc}
     *
     * This gets the list of parameters that are to be included in the toString result
     * using {@link #getToStringParamDescriptions(List, Map)}.
     * If that list is empty, {@link #getStringForEmptyParamList()} is returned.
     * Otherwise, each entry is looped through, calling {@link ParamDescription#getNameValueString(Object, Map)} on each.
     * The resulting strings are then joined together into one string using the {@link #getParameterDelimiter()}.<br>
     *
     * @param thisObj  {@inheritDoc} - cannot be null
     * @param seen  {@inheritDoc} - cannot be null
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc} If no toString() parameters are in the map, {@link #getStringForEmptyParamList()} is returned.
     * @throws IllegalArgumentException if any parameters is null
     */
    @Override
    public <O> String getParamsString(final O thisObj, final Map<Class, Set<Integer>> seen, final List<String> paramOrder,
                                      final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "getParamsString");
        requireNonNull(seen, 2, "seen", "getParamsString");
        requireNonNull(paramOrder, 3, "paramOrder", "getParamsString");
        requireNonNull(paramDescriptionMap, 4, "paramDescriptionMap", "getParamsString");
        List<ParamDescription<? super O, ?>> paramDescriptions = getToStringParamDescriptions(paramOrder,
                                                                                              paramDescriptionMap);
        return paramsToString(thisObj, paramDescriptions, getStringForEmptyParamList(), seen);
    }

    /**
     * {@inheritDoc}
     *
     * This gets the list primary parameters that are to be included in the toString result
     * using {@link #getPrimaryToStringParamDescriptions(List, Map)}.
     * If that list is empty, {@link #getStringForRecursionPrevented()} is returned.
     * Otherwise, each entry is looped through, calling {@link ParamDescription#getNameValueString(Object, Map)} on each.
     * The resulting strings are then joined together into one string using the {@link #getParameterDelimiter()}.<br>
     *
     * @param thisObj  {@inheritDoc} - cannot be null
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc} If no primary toString() parameters are in the map, {@link #getStringForRecursionPrevented()} is returned.
     * @throws IllegalArgumentException if any parameters is null
     */
    @Override
    public <O> String getPrimaryParamsString(final O thisObj, final List<String> paramOrder,
                                             final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "getPrimaryParamsString");
        requireNonNull(paramOrder, 2, "paramOrder", "getPrimaryParamsString");
        requireNonNull(paramDescriptionMap, 3, "paramDescriptionMap", "getPrimaryParamsString");
        List<ParamDescription<? super O, ?>> paramDescriptions = getPrimaryToStringParamDescriptions(paramOrder,
                                                                                                     paramDescriptionMap);
        return paramsToString(thisObj, paramDescriptions, getStringForRecursionPrevented(), new HashMap<>());
    }

    /**
     * Converts all the parameters of the object into Strings, then joins them into one String.<br>
     *
     * If the provided <code>paramDescriptions</code> map is empty, the <code>defaultForEmpty</code> is returned.
     * Otherwise, it is iterated through and {@link ParamDescription#getNameValueString(Object, Map)} is called for each.
     * Then they are all joined together using {@link #getParameterDelimiter()}.<br>
     *
     * @param thisObj  the object containing the parameters
     * @param paramDescriptions  the list of parameter descriptions
     * @param defaultForEmpty  the default string for when the list is empty
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string
     * @param <O> the type of the object in question
     * @return A String. If no parameters are in the list, <code>defaultForEmpty</code> is returned.
     */
    private <O> String paramsToString(final O thisObj, final List<ParamDescription<? super O, ?>> paramDescriptions,
                                      final String defaultForEmpty, final Map<Class, Set<Integer>> seen) {
        if (paramDescriptions.isEmpty()) {
            return defaultForEmpty;
        }
        return paramDescriptions.stream()
                                .map(pd -> pd.getNameValueString(thisObj, seen))
                                .collect(Collectors.joining(getParameterDelimiter()));
    }

    /**
     * {@inheritDoc}
     *
     * Resulting list is unmodifiable.<br>
     *
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return An unmodifiable list of ParamDescription objects
     * @throws IllegalArgumentException if either parameter is null
     */
    @Override
    public <O> List<ParamDescription<? super O, ?>> getAllParamDescriptions(
                    final List<String> paramOrder, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(paramOrder, 1, "paramOrder", "getAllParamDescriptions");
        requireNonNull(paramDescriptionMap, 1, "paramDescriptionMap", "getAllParamDescriptions");
        return getFilteredParamList((p) -> true, paramOrder, paramDescriptionMap);
    }

    /**
     * {@inheritDoc}
     *
     * Resulting list is unmodifiable.<br>
     *
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return An unmodifiable list of ParamDescription objects
     * @throws IllegalArgumentException if either parameter is null
     */
    @Override
    public <O> List<ParamDescription<? super O, ?>> getEqualsParamDescriptions(
                    final List<String> paramOrder, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(paramOrder, 1, "paramOrder", "getEqualsParamDescriptions");
        requireNonNull(paramDescriptionMap, 1, "paramDescriptionMap", "getEqualsParamDescriptions");
        return getFilteredParamList(ParamDescription::isEqualsInclude, paramOrder, paramDescriptionMap);
    }

    /**
     * {@inheritDoc}
     *
     * Resulting list is unmodifiable.<br>
     *
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return An unmodifiable list of ParamDescription objects
     * @throws IllegalArgumentException if either parameter is null
     */
    @Override
    public <O> List<ParamDescription<? super O, ?>> getHashCodeParamDescriptions(
                    final List<String> paramOrder, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(paramOrder, 1, "paramOrder", "getHashCodeParamDescriptions");
        requireNonNull(paramDescriptionMap, 1, "paramDescriptionMap", "getHashCodeParamDescriptions");
        return getFilteredParamList(ParamDescription::isHashCodeInclude, paramOrder, paramDescriptionMap);
    }

    /**
     * {@inheritDoc}
     *
     * Resulting list is unmodifiable.<br>
     *
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return An unmodifiable list of ParamDescription objects
     * @throws IllegalArgumentException if either parameter is null
     */
    @Override
    public <O> List<ParamDescription<? super O, ?>> getToStringParamDescriptions(
                    final List<String> paramOrder, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(paramOrder, 1, "paramOrder", "getToStringParamDescriptions");
        requireNonNull(paramDescriptionMap, 1, "paramDescriptionMap", "getToStringParamDescriptions");
        return getFilteredParamList(ParamDescription::isToStringInclude, paramOrder, paramDescriptionMap);
    }

    /**
     * {@inheritDoc}
     *
     * Resulting list is unmodifiable.<br>
     *
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O> {@inheritDoc}
     * @return An unmodifiable list of ParamDescription objects
     * @throws IllegalArgumentException if either parameter is null
     */
    @Override
    public <O> List<ParamDescription<? super O, ?>> getPrimaryToStringParamDescriptions(
                    final List<String> paramOrder, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(paramOrder, 1, "paramOrder", "getToStringParamDescriptions");
        requireNonNull(paramDescriptionMap, 1, "paramDescriptionMap", "getToStringParamDescriptions");
        return getFilteredParamList(pd -> pd instanceof ParamDescriptionSingle && ((ParamDescriptionSingle)pd).isPrimary(),
                                    paramOrder, paramDescriptionMap);
    }

    /**
     * Filters the params list using the provided predicate and returns an unmodifiable list of ParamDescriptions.<br>
     *
     * @param filter  the predicate to use in the filter, e.g. ParamDescription::isToStringInclude - cannot be null
     * @return An unmodifiable list of ParamDescriptions.
     */
    private <O> List<ParamDescription<? super O, ?>> getFilteredParamList(
                    final Predicate<ParamDescription<? super O, ?>> filter, final List<String> paramOrder,
                    final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        return Collections.unmodifiableList(paramOrder.stream()
                                                      .map(paramDescriptionMap::get)
                                                      .filter(filter)
                                                      .collect(Collectors.toList()));
    }

    /**
     * {@inheritDoc}
     *
     * First, tests equality of the two objects using ==. If they're equal that way, return true.<br>
     * Then, if either of the objects is null, return false.<br>
     * Then if neither object is an instance of the parentClass, return <code>thisObj.equals(thatObj)</code>.<br>
     * If only one of the objects is an instance of the parentClass, return false.<br>
     * Otherwise, cast them both to the parentClass.
     * Then use {@link #getEqualsParamDescriptions(List, Map)} to get the list of parameter applicable.
     * Loop through them and call {@link ParamDescription#paramsAreEqual} on each parameter.
     * If they are all equal, return true. Otherwise, return false.
     * There's no guarantee that {@link ParamDescription#paramsAreEqual} is called for all parameters.
     * If one false is found, the rest might be skipped.<br>
     *
     * @param thisObj  {@inheritDoc}
     * @param thatObj  {@inheritDoc}
     * @param parentClass  {@inheritDoc} - cannot be null
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the parentClass, paramOrder, or paramDescriptionMap parameters are null
     */
    @Override
    public <O> boolean equals(final Object thisObj, final Object thatObj, final Class<O> parentClass, final List<String> paramOrder,
                              final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(parentClass, 3, "parentClass", "equals");
        requireNonNull(paramOrder, 4, "paramOrder", "equals");
        requireNonNull(paramDescriptionMap, 5, "paramDescriptionMap", "equals");
        if (thisObj == thatObj) {
            return true;
        }
        if (thisObj == null || thatObj == null) {
            return false;
        }
        boolean thisIsInstance = parentClass.isInstance(thisObj);
        boolean thatIsInstance = parentClass.isInstance(thatObj);
        if (!thisIsInstance && !thatIsInstance) {
            return thisObj.equals(thatObj);
        }
        if (!thisIsInstance || !thatIsInstance) {
            return false;
        }
        @SuppressWarnings("unchecked")
        O thisO = (O)thisObj;
        @SuppressWarnings("unchecked")
        O thatO = (O)thatObj;
        return getEqualsParamDescriptions(paramOrder, paramDescriptionMap).stream().allMatch(pd -> pd.paramsAreEqual(thisO, thatO));
    }

    /**
     * {@inheritDoc}
     *
     * Uses the {@link #getHashCodeParamDescriptions(List, Map)} method to get the applicable entries.
     * Then loops through them calling {@link ParamDescription#get(Object)} on each entry.
     * The results are converted to an array of objects and then provided to {@link Objects#hash(Object...)}.<br>
     *
     * @param thisObj  {@inheritDoc} - cannot be null
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if any parameter is null
     */
    @Override
    public <O> int hashCode(final O thisObj, final List<String> paramOrder,
                            final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "hashCode");
        requireNonNull(paramOrder, 2, "paramOrder", "hashCode");
        requireNonNull(paramDescriptionMap, 3, "paramDescriptionMap", "hashCode");
        return Objects.hash(getHashCodeParamDescriptions(paramOrder, paramDescriptionMap).stream().map(pd -> pd.get(thisObj)).toArray());
    }

    /**
     * {@inheritDoc}
     *
     * First, the class name String is retrieved using the {@link #getClassNameGetter()}.<br>
     * Then, the hashCode is calculated using <code>thisObj.hashCode()</code>,
     * and converted to a string using {@link #getHashCodeToString()}.<br>
     * Then, the parameters String is created using {@link #getParamsString(Object, Map, List, Map)}.<br>
     * Lastly, {@link #getToStringFormat()} is used to create the final String.
     * Arguments are provided to the {@link #getToStringFormat()} in this order:
     * <code>class name</code>, <code>hash code String</code>, <code>parameters String</code><br>
     *
     * @param thisObj  {@inheritDoc} - cannot be null
     * @param seen  {@inheritDoc} - if null, a new empty HashMap is used
     * @param parentClass  {@inheritDoc} - cannot be null
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if thisObj, parentClass, paramOrder or paramDescriptionMap are null
     */
    @Override
    public <O> String toString(final O thisObj, final Map<Class, Set<Integer>> seen, final Class<O> parentClass,
                               final List<String> paramOrder, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "toString");
        requireNonNull(parentClass, 3, "parentClass", "toString");
        requireNonNull(paramOrder, 4, "paramOrder", "toString");
        requireNonNull(paramDescriptionMap, 5, "paramDescriptionMap", "toString");
        String paramsString = getParamsString(thisObj, Optional.ofNullable(seen).orElseGet(HashMap::new),
                                              paramOrder, paramDescriptionMap);
        return createToStringResult(thisObj, parentClass, paramsString);
    }

    /**
     * {@inheritDoc}
     *
     * First, the class name String is retrieved using the {@link #getClassNameGetter()}.<br>
     * Then, the hashCode is calculated using <code>thisObj.hashCode()</code>,
     * and converted to a string using {@link #getHashCodeToString()}.<br>
     * Then, the parameters String is created using {@link #getPrimaryParamsString(Object, List, Map)}.<br>
     * Lastly, {@link #getToStringFormat()} is used to create the final String.
     * Arguments are provided to the {@link #getToStringFormat()} in this order:
     * <code>class name</code>, <code>hash code String</code>, <code>parameters String</code><br>
     *
     * @param thisObj  {@inheritDoc} - cannot be null
     * @param parentClass  {@inheritDoc} - cannot be null
     * @param paramOrder  {@inheritDoc} - cannot be null
     * @param paramDescriptionMap  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if thisObj, parentClass, paramOrder or paramDescriptionMap are null
     */
    @Override
    public <O> String primaryToString(final O thisObj, final Class<O> parentClass, final List<String> paramOrder,
                                      final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "primaryToString");
        requireNonNull(parentClass, 2, "parentClass", "primaryToString");
        requireNonNull(paramOrder, 3, "paramOrder", "primaryToString");
        requireNonNull(paramDescriptionMap, 4, "paramDescriptionMap", "primaryToString");
        String paramsString = getPrimaryParamsString(thisObj, paramOrder, paramDescriptionMap);
        return createToStringResult(thisObj, parentClass, paramsString);
    }

    private <O> String createToStringResult(final O thisObj, final Class<O> parentClass, final String paramsString) {
        String className = getClassNameGetter().apply(parentClass);
        String hashCode = getHashCodeToString().apply(thisObj.hashCode());
        return String.format(getToStringFormat(), className, hashCode, paramsString);
    }

    /**
     * {@inheritDoc}
     *
     * If the provided obj parameter is null, an IllegalArgumentException is thrown.<br>
     *
     * The message of the exception is created using the <code>illegalArgumentMessageFormat</code>
     * being provided with the arguments in this order: <code>position</code>, <code>paramName</code>, <code>methodName</code>.<br>
     *
     * @param obj  {@inheritDoc} - cannot be null
     * @param position  {@inheritDoc}
     * @param paramName  {@inheritDoc}
     * @param methodName  {@inheritDoc}
     * @throws IllegalArgumentException if the provided obj is null
     */
    @Override
    public void requireNonNull(final Object obj, final int position, final String paramName, final String methodName) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format(getIllegalArgumentMessageFormat(), position, paramName, methodName));
        }
    }

    /**
     * equals method for a EasyOverriderServiceImpl object.<br>
     *
     * @param obj  the object to test against
     * @return True if this EasyOverriderServiceImpl is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getParamList().equals(this, obj);
    }

    /**
     * hashCode method for a EasyOverriderServiceImpl object.<br>
     *
     * @return an int
     */
    @Override
    public int hashCode() {
        return getParamList().hashCode(this);
    }

    /**
     * toString method for a EasyOverriderServiceImpl object.<br>
     *
     * @return A string representation of this object
     */
    @Override
    public String toString() {
        return getParamList().toString(this);
    }
}
