package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

    private EasyOverriderConfig easyOverriderConfig = null;

    private static ParamList<EasyOverriderServiceImpl> paramList;

    static ParamList<EasyOverriderServiceImpl> getParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(EasyOverriderServiceImpl.class)
                                 .withParam("easyOverriderConfig",
                                            EasyOverriderServiceImpl::getConfig,
                                            EasyOverriderConfig.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor that just uses the default config.<br>
     */
    public EasyOverriderServiceImpl() {
        easyOverriderConfig = new EasyOverriderConfig();
    }

    /**
     * Constructor that takes in a config.<br>
     *
     * @param easyOverriderConfig  the config to use - cannot be null
     * @throws IllegalArgumentException if the provided config is null.
     */
    public EasyOverriderServiceImpl(EasyOverriderConfig easyOverriderConfig) {
        requireNonNull(easyOverriderConfig, 1, "easyOverriderConfig", "EasyOverriderServiceImpl constructor");
        this.easyOverriderConfig = easyOverriderConfig;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public EasyOverriderConfig getConfig() {
        return easyOverriderConfig;
    }

    /**
     * {@inheritDoc}
     *
     * @param easyOverriderConfig  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided parameter is null.
     */
    public EasyOverriderService setConfig(EasyOverriderConfig easyOverriderConfig) {
        requireNonNull(easyOverriderConfig, 1, "easyOverriderConfig", "setConfig");
        this.easyOverriderConfig = easyOverriderConfig;
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
     * Checks to see if the corresponding parameters defined by the getter are the same in both objects.<br>
     *
     * If <code>thisO == thatO</code>, true is returned.<br>
     * Then, if either of them are null, false is returned.<br>
     * Then, the parameters are retrieved. If they are equal using == or equal using Objects.equals then true is returned.<br>
     * Otherwise, false is returned.<br>
     *
     * @param thisO  the first object to get the parameter from
     * @param thatO  the second object to get the parameter from
     * @param getter  the getter for the parameter to compare - cannot be null
     * @param name  the name of the parameter (for error messages)
     * @param <O>  the type of the object
     * @param <P>  the type of the parameter (getter return value)
     * @return True if the parameter in each of the objects are equal. False if different.
     * @throws IllegalArgumentException if the provided getter is null
     */
    private <O, P> boolean paramsAreEqual(final O thisO, final O thatO, final Function<? super O, P> getter, final String name) {
        requireNonNull(getter, 3, "getter", "paramsAreEqual: " + name);
        if (thisO == thatO) {
            return true;
        }
        if (thisO == null || thatO == null) {
            return false;
        }
        P thisP = get(thisO, getter, name);
        P thatP = get(thatO, getter, name);
        return thisP == thatP || Objects.equals(thisP, thatP);
    }

    /**
     * Converts an object to a String.<br>
     *
     * If the provided object is null, {@link EasyOverriderConfig#getStringForNull()} is returned.<br>
     * Otherwise, if the object is an instance of {@link RecursionPreventingToString}, then
     * the hashCode of the object is calculated.
     * If the hashCode is already in the seen map, {@link RecursionPreventingToString#primaryToString()} is called.
     * If that is not null, it is returned. Otherwise, {@link EasyOverriderConfig#getStringForRecursionPrevented()} is returned.
     * If the hashCode is NOT already in the seen map, the hashCode is added to the seen map, the object's
     * {@link RecursionPreventingToString#toString(Map)} method is called and it's result is returned.<br>
     * If the object is NOT an instance of {@link RecursionPreventingToString},
     * then the standard {@link Object#toString()} method is called on the object and returned.<br>
     *
     * @param objClass  the class of the object being converted - cannot be null
     * @param obj  the object to convert
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string - cannot be null
     * @param <O>  the type of the object
     * @return A String
     * @throws IllegalArgumentException if either the objClass or seen parameters are null
     * @see RecursionPreventingToString
     */
    private <O> String objectToStringPreventingRecursion(final Class<O> objClass, final O obj, final Map<Class, Set<Integer>> seen) {
        requireNonNull(objClass, 1, "objClass", "objectToStringPreventingRecursion");
        requireNonNull(seen, 3, "seen", "objectToStringPreventingRecursion");
        if (obj == null) {
            return easyOverriderConfig.getStringForNull();
        }
        if (obj instanceof RecursionPreventingToString) {
            if (!seen.containsKey(objClass)) {
                seen.put(objClass, new HashSet<>());
            }
            RecursionPreventingToString recursiveObject = (RecursionPreventingToString)obj;
            int entryHashCode = obj.hashCode();
            if (seen.get(objClass).contains(entryHashCode)) {
                return Optional.ofNullable(recursiveObject.primaryToString())
                               .orElseGet(() -> createToStringResult(obj, objClass,
                                                                     easyOverriderConfig.getStringForRecursionPrevented()));
            }
            seen.get(objClass).add(entryHashCode);
            return recursiveObject.toString(seen);
        }
        return obj.toString();
    }

    /**
     * {@inheritDoc}
     *
     * First gets the parameter from the object.
     * If it's null, {@link EasyOverriderConfig#getStringForNull()} is used for the String of the value.
     * Otherwise, the provided BiFunction is used to convert it to a String in a recursion-safe way.
     * Then, if that result is not the {@link EasyOverriderConfig#getStringForNull()}
     * or {@link EasyOverriderConfig#getStringForRecursionPrevented()} values,
     * the {@link EasyOverriderConfig#getParameterValueFormat()} is applied to it.<br>
     *
     * Finally, the {@link EasyOverriderConfig#getNameValueFormat()} is applied,
     * being provided the <code>name</code> and value created above.<br>
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
        P param = getter.apply(obj);
        String value = Optional.ofNullable(param)
                               .map(p -> valueToStringPreventingRecursion.apply(p, seen))
                               .orElseGet(() -> easyOverriderConfig.getStringForNull());
        if (!value.equals(easyOverriderConfig.getStringForNull())
            && !value.equals(easyOverriderConfig.getStringForRecursionPrevented())) {
            value = String.format(easyOverriderConfig.getParameterValueFormat(), value);
        }
        return String.format(easyOverriderConfig.getNameValueFormat(), name, value);
    }

    /**
     * {@inheritDoc}
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
     * If the provided value is null, {@link EasyOverriderConfig#getStringForNull()} is returned.<br>
     *
     * Otherwise, it is looped through and all entries are converted to
     * Strings in a recursion-safe way.
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
            return easyOverriderConfig.getStringForNull();
        }
        return value.stream()
                    .map(e -> objectToStringPreventingRecursion(entryClass, e, seen))
                    .collect(Collectors.toList())
                    .toString();
    }

    /**
     * {@inheritDoc}
     *
     * If the provided value is null, {@link EasyOverriderConfig#getStringForNull()} is returned.<br>
     *
     * Otherwise, all entries are looped through and all keys and values are converted to
     * Strings using in a recursion-safe way.
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
            return easyOverriderConfig.getStringForNull();
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
     * Makes sure that none of the parameters are null.
     * Also makes sure that the <code>paramOrder</code> list and <code>paramDescriptionMap</code> map have the same number of entries.
     * Additionally, a check is made to make sure that all entries in the <code>paramOrder</code> list have corresponding keys in
     * the <code>paramDescriptionMap</code> map.<br>
     *
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
     * Gets a string of all the parameters in the provided object.<br>
     *
     * This gets the list of parameters that are to be included in the toString result.
     * If that list is empty, {@link EasyOverriderConfig#getStringForEmptyParamList()} is returned.
     * Otherwise, each entry is looped through, calling {@link ParamDescription#getNameValueString(Object, Map)} on each.
     * The resulting strings are then joined together into one string using
     * the {@link EasyOverriderConfig#getParameterDelimiter()}.<br>
     *
     * @param thisObj  the object to get the parameters from - cannot be null
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string - cannot be null
     * @param paramOrder  the list of parameter names in the order they should be used - cannot be null
     * @param paramDescriptionMap  the map of names to ParamDescriptions - cannot be null
     * @param <O>  the type of the object in question
     * @return A String. If no toString() parameters are in the map,
     * {@link EasyOverriderConfig#getStringForEmptyParamList()} is returned.
     * @throws IllegalArgumentException if any parameters is null
     */
    private <O> String getParamsString(final O thisObj, final Map<Class, Set<Integer>> seen, final List<String> paramOrder,
                                      final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "getParamsString");
        requireNonNull(seen, 2, "seen", "getParamsString");
        requireNonNull(paramOrder, 3, "paramOrder", "getParamsString");
        requireNonNull(paramDescriptionMap, 4, "paramDescriptionMap", "getParamsString");
        List<ParamDescription<? super O, ?>> paramDescriptions = getToStringParamDescriptions(paramOrder,
                                                                                              paramDescriptionMap);
        return paramsToString(thisObj, paramDescriptions, easyOverriderConfig.getStringForEmptyParamList(), seen);
    }

    /**
     * Gets a string of all the primary parameters in the provided object.<br>
     *
     * This gets the list primary parameters that are to be included in the toString result,
     * If that list is empty, {@link EasyOverriderConfig#getStringForRecursionPrevented()} is returned.
     * Otherwise, each entry is looped through, calling {@link ParamDescription#getNameValueString(Object, Map)} on each.
     * The resulting strings are then joined together into one string
     * using the {@link EasyOverriderConfig#getParameterDelimiter()}.<br>
     *
     * @param thisObj  the object to get the parameters from - cannot be null
     * @param paramOrder  the list of parameter names in the order they should be used - cannot be null
     * @param paramDescriptionMap  the map of names to ParamDescriptions - cannot be null
     * @param <O>  the type of the object in question
     * @return A String. If no primary toString() parameters are in the map,
     * {@link EasyOverriderConfig#getStringForRecursionPrevented()} is returned.
     * @throws IllegalArgumentException if any parameters is null
     */
    private <O> String getPrimaryParamsString(final O thisObj, final List<String> paramOrder,
                                             final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(thisObj, 1, "thisObj", "getPrimaryParamsString");
        requireNonNull(paramOrder, 2, "paramOrder", "getPrimaryParamsString");
        requireNonNull(paramDescriptionMap, 3, "paramDescriptionMap", "getPrimaryParamsString");
        List<ParamDescription<? super O, ?>> paramDescriptions = getPrimaryToStringParamDescriptions(paramOrder,
                                                                                                     paramDescriptionMap);
        return paramsToString(thisObj, paramDescriptions, easyOverriderConfig.getStringForRecursionPrevented(), new HashMap<>());
    }

    /**
     * Converts all the parameters of the object into Strings, then joins them into one String.<br>
     *
     * If the provided <code>paramDescriptions</code> map is empty, the <code>defaultForEmpty</code> is returned.
     * Otherwise, it is iterated through and {@link ParamDescription#getNameValueString(Object, Map)} is called for each.
     * Then they are all joined together using {@link EasyOverriderConfig#getParameterDelimiter()}.<br>
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
                                .collect(Collectors.joining(easyOverriderConfig.getParameterDelimiter()));
    }

    /**
     * Gets a list of all the parameter descriptions that should be used in an equals method.<br>
     *
     * @param paramOrder  the list of parameter names in the order they should be used - cannot be null
     * @param paramDescriptionMap  the map of names to ParamDescriptions - cannot be null
     * @param <O>  the type of the object in question
     * @return An unmodifiable list of ParamDescription objects
     * @throws IllegalArgumentException if either parameter is null
     */
    private <O> List<ParamDescription<? super O, ?>> getEqualsParamDescriptions(
                    final List<String> paramOrder, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(paramOrder, 1, "paramOrder", "getEqualsParamDescriptions");
        requireNonNull(paramDescriptionMap, 1, "paramDescriptionMap", "getEqualsParamDescriptions");
        return getFilteredParamList(ParamDescription::isEqualsInclude, paramOrder, paramDescriptionMap);
    }

    /**
     * Gets a list of all the parameter descriptions that should be in a hashCode method.<br>
     *
     * @param paramOrder  the list of parameter names in the order they should be used - cannot be null
     * @param paramDescriptionMap  the map of names to ParamDescriptions - cannot be null
     * @param <O>  the type of the object in question
     * @return An unmodifiable list of ParamDescription objects
     * @throws IllegalArgumentException if either parameter is null
     */
    private <O> List<ParamDescription<? super O, ?>> getHashCodeParamDescriptions(
                    final List<String> paramOrder, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(paramOrder, 1, "paramOrder", "getHashCodeParamDescriptions");
        requireNonNull(paramDescriptionMap, 1, "paramDescriptionMap", "getHashCodeParamDescriptions");
        return getFilteredParamList(ParamDescription::isHashCodeInclude, paramOrder, paramDescriptionMap);
    }

    /**
     * Gets a list of all the parameter descriptions that should be in a toString method.<br>
     *
     * @param paramOrder  the list of parameter names in the order they should be used - cannot be null
     * @param paramDescriptionMap  the map of names to ParamDescriptions - cannot be null
     * @param <O>  the type of the object in question
     * @return An unmodifiable list of ParamDescription objects
     * @throws IllegalArgumentException if either parameter is null
     */
    private <O> List<ParamDescription<? super O, ?>> getToStringParamDescriptions(
                    final List<String> paramOrder, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap) {
        requireNonNull(paramOrder, 1, "paramOrder", "getToStringParamDescriptions");
        requireNonNull(paramDescriptionMap, 1, "paramDescriptionMap", "getToStringParamDescriptions");
        return getFilteredParamList(ParamDescription::isToStringInclude, paramOrder, paramDescriptionMap);
    }

    /**
     * Gets a list of just the primary parameter descriptions that should be in a toString method.<br>
     *
     * Resulting list is unmodifiable.<br>
     *
     * @param paramOrder  the list of parameter names in the order they should be used - cannot be null
     * @param paramDescriptionMap  the map of names to ParamDescriptions - cannot be null
     * @param <O>  the type of the object in question
     * @return An unmodifiable list of ParamDescription objects
     * @throws IllegalArgumentException if either parameter is null
     */
    private <O> List<ParamDescription<? super O, ?>> getPrimaryToStringParamDescriptions(
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
        return paramOrder.stream()
                         .map(paramDescriptionMap::get)
                         .filter(filter)
                         .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     * First, tests equality of the two objects using ==. If they're equal that way, return true.<br>
     * Then, if either of the objects is null, return false.<br>
     * Then if neither object is an instance of the parentClass, return <code>thisObj.equals(thatObj)</code>.<br>
     * If only one of the objects is an instance of the parentClass, return false.<br>
     * Otherwise, cast them both to the parentClass.
     * Then get a list of all ParameterDescription objects that are to be included in an equals comparison.
     * For each entry, call the getter on both objects and compare the results using {@link Objects#equals(Object, Object)}.
     * If they are all equal, return true. Otherwise, return false.
     * As soon as one unequal parameter is found, the rest are skipped.
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
        return getEqualsParamDescriptions(paramOrder, paramDescriptionMap)
                        .stream().allMatch(pd -> paramsAreEqual(thisO, thatO, pd.getGetter(), pd.getName()));
    }

    /**
     * {@inheritDoc}
     *
     * Gets all ParamDescription entries that should be included in the hashCode.
     * Then loops through them getting each parameter from the object.
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
        return Objects.hash(getHashCodeParamDescriptions(paramOrder, paramDescriptionMap)
                                            .stream()
                                            .map(pd -> get(thisObj, pd.getGetter(), pd.getName()))
                                            .toArray());
    }

    /**
     * {@inheritDoc}
     *
     * First, the class name String is retrieved using the {@link EasyOverriderConfig#getClassNameGetter()}.<br>
     * Then, the hashCode is calculated using <code>thisObj.hashCode()</code>,
     * and converted to a string using {@link EasyOverriderConfig#getHashCodeToString()}.<br>
     * Then, the parameters String is created in a recursion-safe way.<br>
     * Lastly, {@link EasyOverriderConfig#getToStringFormat()} is used to create the final String.
     * Arguments are provided to the {@link EasyOverriderConfig#getToStringFormat()} in this order:
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
     * First, the class name String is retrieved using the {@link EasyOverriderConfig#getClassNameGetter()}.<br>
     * Then, the hashCode is calculated using <code>thisObj.hashCode()</code>,
     * and converted to a string using {@link EasyOverriderConfig#getHashCodeToString()}.<br>
     * Then, the parameters String is created using only the primary parameters.<br>
     * Lastly, {@link EasyOverriderConfig#getToStringFormat()} is used to create the final String.
     * Arguments are provided to the {@link EasyOverriderConfig#getToStringFormat()} in this order:
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
        String className = easyOverriderConfig.getClassNameGetter().apply(parentClass);
        String hashCode = easyOverriderConfig.getHashCodeToString().apply(thisObj.hashCode());
        return String.format(easyOverriderConfig.getToStringFormat(), className, hashCode, paramsString);
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
