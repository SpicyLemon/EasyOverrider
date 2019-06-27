package EasyOverrider;

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
    Function<Class, String> classNameGetter = Class::getCanonicalName;

    private static ParamList<EasyOverriderServiceImpl> paramList;

    static ParamList<EasyOverriderServiceImpl> getParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(EasyOverriderServiceImpl.class)
                                 .withParam("stringForNull", EasyOverriderServiceImpl::getStringForNull, String.class)
                                 .withParam("stringForRecursionPrevented", EasyOverriderServiceImpl::getStringForRecursionPrevented, String.class)
                                 .withParam("stringForEmptyParamList", EasyOverriderServiceImpl::getStringForEmptyParamList, String.class)
                                 .withParam("parameterDelimiter", EasyOverriderServiceImpl::getParameterDelimiter, String.class)
                                 .withParam("nameValueFormat", EasyOverriderServiceImpl::getNameValueFormat, String.class)
                                 .withParam("parameterValueFormat", EasyOverriderServiceImpl::getParameterValueFormat, String.class)
                                 .withParam("toStringFormat", EasyOverriderServiceImpl::getToStringFormat, String.class)
                                 .withParam("classNameGetter", EasyOverriderServiceImpl::getClassNameGetter, Function.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor that just uses all the default values.
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
     * @throws IllegalArgumentException if the provided String is null
     * @see #paramValueToString(Object, Function, Map, BiFunction)
     * @see #objectToStringPreventingRecursion(Class, Object, Map)
     */
    @Override
    public void setStringForNull(String stringForNull) {
        requireNonNull(stringForNull, 1, "stringForNull", "setStringForNull");
        this.stringForNull = stringForNull;
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
     * @throws IllegalArgumentException if the provided String is null
     * @see #objectToStringPreventingRecursion(Class, Object, Map)
     */
    @Override
    public void setStringForRecursionPrevented(String stringForRecursionPrevented) {
        requireNonNull(stringForRecursionPrevented, 1, "stringForRecursionPrevented", "setStringForRecursionPrevented");
        this.stringForRecursionPrevented = stringForRecursionPrevented;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>" "</code>.<br>
     *
     * @return {@inheritDoc}
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
     * @throws IllegalArgumentException if the provided String is null
     */
    @Override
    public void setStringForEmptyParamList(String stringForEmptyParamList) {
        requireNonNull(stringForEmptyParamList, 1, "stringForEmptyParamList", "setStringForEmptyParamList");
        this.stringForEmptyParamList = stringForEmptyParamList;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>", "</code>.<br>
     *
     * @return {@inheritDoc}
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
     * @throws IllegalArgumentException if the provided String is null
     */
    @Override
    public void setParameterDelimiter(String parameterDelimiter) {
        requireNonNull(parameterDelimiter, 1, "parameterDelimiter", "setParameterDelimiter");
        this.parameterDelimiter = parameterDelimiter;
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
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     * @see #getNameValueString(Object, String, Function, Map, BiFunction)
     */
    @Override
    public void setNameValueFormat(String nameValueFormat) {
        requireNonNull(nameValueFormat, 1, "nameValueFormat", "setNameValueFormat");
        try {
            String.format(nameValueFormat, "name", "value");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setNameValueFormat is not a valid format string.", e);
        }
        this.nameValueFormat = nameValueFormat;
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
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     * @see #getNameValueString(Object, String, Function, Map, BiFunction)
     */
    @Override
    public void setParameterValueFormat(String parameterValueFormat) {
        requireNonNull(parameterValueFormat, 1, "parameterValueFormat", "setParameterValueFormat");
        try {
            String.format(parameterValueFormat, "parameter");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setParameterValueFormat is not a valid format string.", e);
        }
        this.parameterValueFormat = parameterValueFormat;
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
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     * @see #toString(Object, Map, Class, List, Map)
     */
    @Override
    public void setToStringFormat(String toStringFormat) {
        requireNonNull(toStringFormat, 1, "toStringFormat", "setToStringFormat");
        try {
            String.format(toStringFormat, "class", "hashcode", "paramslist");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setToStringFormat is not a valid format string.", e);
        }
        this.toStringFormat = toStringFormat;
    }

    /**
     * {@inheritDoc}
     *
     * Default value is <code>Class::getCanonicalName</code>.<br>
     *
     * @return {@inheritDoc}
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
     * @throws IllegalArgumentException if the provided Function is null
     */
    @Override
    public void setClassNameGetter(Function<Class, String> classNameGetter) {
        requireNonNull(classNameGetter, 1, "classNameGetter", "setClassNameGetter");
        this.classNameGetter = classNameGetter;
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
    public <O, P> P get(O obj, Function<? super O, P> getter, String name) {
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
    public <O, P> P safeGet(O obj, Function<? super O, P> getter) {
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
    public <O, P> boolean paramsAreEqual(O thisO, O thatO, Function<? super O, P> getter, String name) {
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
     * Runs the getter on the object. If that value is null, <code>stringForNull</code> is used.
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
    public <O, P> String paramValueToString(O obj, Function<? super O, P> getter, Map<Class, Set<Integer>> seen,
                                            BiFunction<P, Map<Class, Set<Integer>>, String> valueToStringPreventingRecursion) {
        requireNonNull(obj, 1, "obj", "paramValueToString");
        requireNonNull(getter, 2, "getter", "paramValueToString");
        requireNonNull(seen, 3, "seen", "paramValueToString");
        requireNonNull(getter, 4, "valueToStringPreventingRecursion", "paramValueToString");
        P value = getter.apply(obj);
        if (value == null) {
            return stringForNull;
        }
        return valueToStringPreventingRecursion.apply(value, seen);
    }

    /**
     * {@inheritDoc}
     *
     * If the provided object is null, stringForNull is returned.<br>
     * Otherwise, if the object is an instance of {@link RecursionPreventingToString}, then
     * the hashCode of the object is calculated.
     * If the hashCode is already in the seen map, <code>stringForRecursionPrevented</code> is returned.
     * Otherwise, the hashCode is added to the seen map, and the object's
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
    public <O> String objectToStringPreventingRecursion(Class<O> objClass, O obj, Map<Class, Set<Integer>> seen) {
        requireNonNull(objClass, 1, "objClass", "objectToStringPreventingRecursion");
        requireNonNull(seen, 3, "seen", "objectToStringPreventingRecursion");
        if (obj == null) {
            return stringForNull;
        }
        if (obj instanceof RecursionPreventingToString) {
            int entryHashCode = obj.hashCode();
            if (!seen.containsKey(objClass)) {
                seen.put(objClass, new HashSet<>());
            }
            if (seen.get(objClass).contains(entryHashCode)) {
                return stringForRecursionPrevented;
            }
            seen.get(objClass).add(entryHashCode);
            return ((RecursionPreventingToString)obj).toString(seen);
        }
        return obj.toString();
    }

    /**
     * {@inheritDoc}
     *
     * First, the value is converted to a String using {@link #paramValueToString(Object, Function, Map, BiFunction)}.
     * Then, if that result is not the <code>stringForNull</code> or <code>stringForRecursionPrevented</code> values,
     * the <code>parameterValueFormat</code> is applied to it.<br>
     *
     * Finally, the <code>nameValueFormat</code> is applied, being provided the <code>name</code> and value created above.<br>
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
    public <O, P> String getNameValueString(final O obj, String name, final Function<? super O, P> getter,
                                            final Map<Class, Set<Integer>> seen,
                                            BiFunction<P, Map<Class, Set<Integer>>, String> valueToStringPreventingRecursion) {
        requireNonNull(obj, 1, "obj", "getNameValueString: " + name);
        requireNonNull(getter, 3, "getter", "getNameValueString: " + name);
        requireNonNull(seen, 4, "seen", "getNameValueString: " + name);
        requireNonNull(valueToStringPreventingRecursion, 5, "valueToStringPreventingRecursion", "getNameValueString: " + name);
        String value = paramValueToString(obj, getter, seen, valueToStringPreventingRecursion);
        if (!value.equals(stringForNull) && !value.equals(stringForRecursionPrevented)) {
            value = String.format(parameterValueFormat, value);
        }
        return String.format(nameValueFormat, name, value);
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
    public <P> String valueToStringPreventingRecursionSingle(P value, Map<Class, Set<Integer>> seen, Class<P> paramClass) {
        return objectToStringPreventingRecursion(paramClass, value, seen);
    }

    /**
     * {@inheritDoc}
     *
     * If the provided value is null, <code>stringForNull</code> is returned.<br>
     *
     * Otherwise, it is looped through and all entries are converted to
     * Strings using {@link #objectToStringPreventingRecursion(Class, Object, Map)}.
     * The results are collected into a List of Strings and then finally turned into one big String using {@link List#toString()}
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
                    P value, Map<Class, Set<Integer>> seen, Class<E> entryClass) {
        requireNonNull(seen, 2, "seen", "valueToStringPreventingRecursionCollection");
        requireNonNull(entryClass, 3, "entryClass", "valueToStringPreventingRecursionCollection");
        if (value == null) {
            return stringForNull;
        }
        return value.stream()
                    .map(e -> objectToStringPreventingRecursion(entryClass, e, seen))
                    .collect(Collectors.toList())
                    .toString();
    }

    @Override
    public <K, V, P extends Map<? extends K, ? extends V>> String valueToStringPreventingRecursionMap(
                    P value, Map<Class, Set<Integer>> seen, Class<K> keyClass, Class<V> valueClass) {
        return value.entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> objectToStringPreventingRecursion(keyClass, e.getKey(), seen),
                                              e -> objectToStringPreventingRecursion(valueClass, e.getValue(), seen)))
                    .toString();
    }

    @Override
    public <O> void validateParamListConstructorOrThrow(final Class<O> parentClass,
                                                        final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap,
                                                        final List<String> paramOrder, final EasyOverriderService easyOverriderService) {
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
    @Override
    public <O> String getParamsString(O thisObj, Map<Class, Set<Integer>> seen, List<String> paramOrder,
                                      Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "getParamsString");
        requireNonNull(seen, 2, "seen", "getParamsString");
        requireNonNull(paramOrder, 3, "paramOrder", "getParamsString");
        requireNonNull(paramDescriptionMap, 4, "paramDescriptionMap", "getParamsString");
        List<ParamDescription<? super O, ?>> toStringParamDescriptions = getToStringParamDescriptions(paramOrder,
                                                                                                      paramDescriptionMap);
        if (toStringParamDescriptions.isEmpty()) {
            return stringForEmptyParamList;
        }
        return toStringParamDescriptions.stream()
                                        .map(pd -> pd.getNameValueString(thisObj, seen))
                                        .collect(Collectors.joining(parameterDelimiter));
    }

    @Override
    public <O> List<ParamDescription<? super O, ?>> getAllParamDescriptions(
                    List<String> paramOrder, Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        return getFilteredParamList((p) -> true, paramOrder, paramDescriptionMap);
    }

    @Override
    public <O> List<ParamDescription<? super O, ?>> getEqualsParamDescriptions(
                    List<String> paramOrder, Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        return getFilteredParamList(ParamDescription::isEqualsInclude, paramOrder, paramDescriptionMap);
    }

    @Override
    public <O> List<ParamDescription<? super O, ?>> getHashCodeParamDescriptions(
                    List<String> paramOrder, Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        return getFilteredParamList(ParamDescription::isHashCodeInclude, paramOrder, paramDescriptionMap);
    }

    @Override
    public <O> List<ParamDescription<? super O, ?>> getToStringParamDescriptions(
                    List<String> paramOrder, Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        return getFilteredParamList(ParamDescription::isToStringInclude, paramOrder, paramDescriptionMap);
    }

    /**
     * Filters the params list using the provided predicate and returns an unmodifiable list of ParamDescriptions.
     *
     * @param filter  the predicate to use in the filter, e.g. ParamDescription::isToStringInclude - cannot be null
     * @return An unmodifialbe list of ParamDescriptions.
     */
    private <O> List<ParamDescription<? super O, ?>> getFilteredParamList(
                    final Predicate<ParamDescription<? super O, ?>> filter, List<String> paramOrder,
                    Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        return Collections.unmodifiableList(paramOrder.stream()
                                                      .map(paramDescriptionMap::get)
                                                      .filter(filter)
                                                      .collect(Collectors.toList()));
    }

    @Override
    public <O> boolean equals(Object thisObj, Object thatObj, Class<O> parentClass, List<String> paramOrder,
                              Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
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

    @Override
    public <O> int hashCode(O thisObj, List<String> paramOrder,
                            Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "hashCode");
        requireNonNull(paramOrder, 2, "paramOrder", "hashCode");
        requireNonNull(paramDescriptionMap, 3, "paramDescriptionMap", "hashCode");
        return Objects.hash(getHashCodeParamDescriptions(paramOrder, paramDescriptionMap).stream().map(pd -> pd.get(thisObj)).toArray());
    }

    @Override
    public <O> String toString(O thisObj, Map<Class, Set<Integer>> seen, Class<O> parentClass,
                               List<String> paramOrder, Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "toString");
        requireNonNull(parentClass, 3, "parentClass", "toString");
        requireNonNull(paramOrder, 4, "paramOrder", "toString");
        requireNonNull(paramDescriptionMap, 5, "paramDescriptionMap", "toString");
        String paramsString = getParamsString(thisObj, Optional.ofNullable(seen).orElseGet(HashMap::new),
                                              paramOrder, paramDescriptionMap);
        return String.format(toStringFormat, classNameGetter.apply(parentClass), thisObj.hashCode(), paramsString);
    }

    @Override
    public void requireNonNull(final Object obj, final int position, final String paramName, final String methodName) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument " + position + " (" + paramName + ") " +
                                               "provided to " + methodName + " cannot be null.");
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
     * toString method for a EasyOverriderServiceImpl object.
     *
     * @return A string representation of this object
     */
    @Override
    public String toString() {
        return getParamList().toString(this);
    }
}
