package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.requireNonNull;

import java.util.ArrayList;
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
                               .orElseGet(() -> createToStringResult(obj, objClass, null,
                                                                     easyOverriderConfig.getStringForRecursionPrevented(), null));
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
     * @param paramList  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the parentClass, paramOrder, or paramDescriptionMap parameters are null
     */
    @Override
    public <O> boolean equals(final Object thisObj, final Object thatObj, final ParamList<O> paramList) {
        requireNonNull(paramList, 3, "paramList", "equals");
        if (thisObj == thatObj) {
            return true;
        }
        if (thisObj == null || thatObj == null) {
            return false;
        }
        boolean thisIsInstance = paramList.getParentClass().isInstance(thisObj);
        boolean thatIsInstance = paramList.getParentClass().isInstance(thatObj);
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
        return getFilteredParamList(ParamDescription::isEqualsInclude, paramList)
                        .stream().allMatch(pd -> paramsAreEqual(thisO, thatO, pd));
    }

    /**
     * {@inheritDoc}
     *
     * Gets all ParamDescription entries that should be included in the hashCode.
     * Then loops through them getting each parameter from the object.
     * The results are converted to an array of objects and then provided to {@link Objects#hash(Object...)}.<br>
     *
     * @param thisObj  {@inheritDoc} - cannot be null
     * @param paramList  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if any parameter is null
     */
    @Override
    public <O> int hashCode(final O thisObj, final ParamList<O> paramList) {
        requireNonNull(thisObj, 1, "thisObj", "hashCode");
        requireNonNull(paramList, 2, "paramList", "hashCode");
        return Objects.hash(getFilteredParamList(ParamDescription::isHashCodeInclude, paramList)
                                            .stream()
                                            .map(pd -> pd.getGetter().apply(thisObj))
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
     * @param paramList  {@inheritDoc} - cannot be null
     * @param seen  {@inheritDoc} - if null, a new empty HashMap is used
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if thisObj, or paramList are null
     */
    @Override
    public <O> String toString(final O thisObj, final ParamList<O> paramList, final Map<Class, Set<Integer>> seen) {
        requireNonNull(thisObj, 1, "thisObj", "toString");
        requireNonNull(paramList, 3, "paramList", "toString");
        List<ParamDescription<? super O, ?>> paramDescriptions = getFilteredParamList(ParamDescription::isToStringInclude, paramList);
        return createToStringResult(thisObj, paramList.getParentClass(), paramDescriptions,
                                    easyOverriderConfig.getStringForEmptyParamList(), seen);
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
     * @param paramList  {@inheritDoc} - cannot be null
     * @param <O>  {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if any parameter is null
     */
    @Override
    public <O> String primaryToString(final O thisObj, final ParamList<O> paramList) {
        requireNonNull(thisObj, 1, "thisObj", "primaryToString");
        requireNonNull(paramList, 2, "paramList", "primaryToString");
        Predicate<ParamDescription<? super O, ?>> filter = pd -> pd instanceof ParamDescriptionSingle
                                                                 && ((ParamDescriptionSingle)pd).isPrimary();
        List<ParamDescription<? super O, ?>> paramDescriptions = getFilteredParamList(filter, paramList);
        return createToStringResult(thisObj, paramList.getParentClass(), paramDescriptions,
                                    easyOverriderConfig.getStringForRecursionPrevented(), new HashMap<>());
    }

    /**
     * Filters the params list using the provided predicate and returns an unmodifiable list of ParamDescriptions.<br>
     *
     * @param filter  the predicate to use in the filter, e.g. ParamDescription::isToStringInclude - assumed not null
     * @param paramList  the paramList to get the ParamDescriptions from - assumed not null
     * @return A list of ParamDescription objects
     */
    private <O> List<ParamDescription<? super O, ?>> getFilteredParamList(
                    final Predicate<ParamDescription<? super O, ?>> filter, final ParamList<O> paramList) {
        Map<String, ParamDescription<? super O, ?>> paramDescriptionMap = paramList.getParamDescriptionMap();
        return paramList.getParamOrder()
                        .stream()
                        .map(paramDescriptionMap::get)
                        .filter(filter)
                        .collect(Collectors.toList());
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
     * @param paramDescription  the getter for the parameter to compare - assumed not null
     * @param <O>  the type of the object
     * @param <P>  the type of the parameter (getter return value)
     * @return True if the parameter in each of the objects are equal. False if different.
     * @throws IllegalArgumentException if the provided getter is null
     */
    private <O, P> boolean paramsAreEqual(final O thisO, final O thatO, ParamDescription<O, P> paramDescription) {
        if (thisO == thatO) {
            return true;
        }
        if (thisO == null || thatO == null) {
            return false;
        }
        P thisP = paramDescription.getGetter().apply(thisO);
        P thatP = paramDescription.getGetter().apply(thatO);
        return thisP == thatP || Objects.equals(thisP, thatP);
    }

    /**
     * Put together the pieces to create the final toString result.<br>
     *
     * Generates the object's hashCode using {@link Object#hashCode()} then converts it to a String
     * using the {@link EasyOverriderConfig#getHashCodeToString()} Function.
     * Uses the {@link EasyOverriderConfig#getClassNameGetter()} Function to create the class name String.
     * Then uses the provided <code>paramDescriptions</code> list to generate a String of the parameters joined together
     * using {@link EasyOverriderConfig#getParameterDelimiter()}.
     * If the provided <code>paramDescriptions</code> list is null or empty, the provided <code>defaultForEmpty</code> is used instead.
     * Then uses the {@link EasyOverriderConfig#getToStringFormat()} format to combine the
     * class name, hash code and parameters String into one String.<br>
     *
     * @param obj  the object being converted to a String - assumed not null
     * @param objClass  the class of the object - assumed not null
     * @param paramDescriptions  the list of parameter descriptions
     * @param defaultForEmpty  the default string for when the list is empty - assumed not null
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string - assumed not null
     * @param <O>  the type of the object
     * @return  A String.
     */
    private <O> String createToStringResult(final O obj, final Class<O> objClass,
                                            final List<ParamDescription<? super O, ?>> paramDescriptions,
                                            final String defaultForEmpty, final Map<Class, Set<Integer>> seen) {
        String hashCode = easyOverriderConfig.getHashCodeToString().apply(obj.hashCode());
        String className = easyOverriderConfig.getClassNameGetter().apply(objClass);
        String paramsString = paramDescriptions == null || paramDescriptions.isEmpty() ? defaultForEmpty :
                        paramDescriptions.stream()
                                         .map(pd -> pd.getNameValueString(obj, seen))
                                         .collect(Collectors.joining(easyOverriderConfig.getParameterDelimiter()));
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
