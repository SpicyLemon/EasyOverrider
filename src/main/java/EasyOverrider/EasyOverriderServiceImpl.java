package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.requireNonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
        requireNonNull(paramList, 2, "paramList", "toString");
        List<ParamDescription<? super O, ?>> paramDescriptions = getFilteredParamList(ParamDescription::isToStringInclude, paramList);
        return createToStringResult(thisObj, paramList.getParentClass(), paramDescriptions,
                                    easyOverriderConfig.getStringForEmptyParamList(),
                                    Optional.ofNullable(seen).orElseGet(HashMap::new));
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
                                         .map(pd -> getNameValueString(obj, pd, seen))
                                         .collect(Collectors.joining(easyOverriderConfig.getParameterDelimiter()));
        return String.format(easyOverriderConfig.getToStringFormat(), className, hashCode, paramsString);
    }

    /**
     * Creates a name/value String for a parameter in an object.<br>
     *
     * First gets the parameter from the object and passes that to
     * {@link #anyParamToString(Object, ParamDescription, Map)} to get the string of that parameter.
     * Then, if that result is not the {@link EasyOverriderConfig#getStringForNull()}
     * or {@link EasyOverriderConfig#getStringForRecursionPrevented()} values,
     * the {@link EasyOverriderConfig#getParameterValueFormat()} is applied to it.<br>
     *
     * Finally, the {@link EasyOverriderConfig#getNameValueFormat()} is applied,
     * being provided the <code>name</code> and value created above.<br>
     *
     * @param obj  the object to get the parameter from - assumed not null
     * @param paramDescription  the ParamDescription with the info on the param to get - assumed not null
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string - assumed not null
     * @param <O>  the type of the object
     * @param <P>  the type of the parameter (getter return value)
     * @return A String
     */
    private <O, P> String getNameValueString(final O obj, final ParamDescription<O, P> paramDescription,
                                             final Map<Class, Set<Integer>> seen) {
        String value = anyParamToString(paramDescription.getGetter().apply(obj), paramDescription, seen);
        if (!value.equals(easyOverriderConfig.getStringForNull())
            && !value.equals(easyOverriderConfig.getStringForRecursionPrevented())) {
            value = String.format(easyOverriderConfig.getParameterValueFormat(), value);
        }
        return String.format(easyOverriderConfig.getNameValueFormat(), paramDescription.getName(), value);
    }

    /**
     * Converts any parameter to a String in a recursion-safe way.<br>
     *
     * If the provided parameter is null, {@link EasyOverriderConfig#getStringForNull()} is returned.<br>
     * If the provided <code>paramDescription</code> is a <code>ParamDescriptionMap</code>,
     * {@link #mapParamToString(Map, ParamDescriptionMap, Map)} is returned.<br>
     * If the provided <code>paramDescription</code> is a <code>ParamDescriptionCollection</code>,
     * {@link #collectionParamToString(Collection, ParamDescriptionCollection, Map)} is returned.<br>
     * Otherwise, {@link #paramToString(Object, Class, Map)} is returned.<br>
     *
     * @param param  the parameter to convert to a String
     * @param paramDescription  the description of the parameter - assumed not null
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string - assumed not null
     * @param <P>  the type of the parameter (getter return value)
     * @return A String
     */
    private <P> String anyParamToString(final P param, final ParamDescription<?, P> paramDescription,
                                        final Map<Class, Set<Integer>> seen) {
        if (param == null) {
            return easyOverriderConfig.getStringForNull();
        }
        if (paramDescription instanceof ParamDescriptionMap) {
            return mapParamToString((Map)param, (ParamDescriptionMap<?, ?, ?, ?>)paramDescription, seen);
        }
        if (paramDescription instanceof ParamDescriptionCollection) {
            return collectionParamToString((Collection)param, (ParamDescriptionCollection<?, ?, ?>)paramDescription, seen);
        }
        return paramToString(param, paramDescription.getParamClass(), seen);
    }

    /**
     * Converts a map parameter to a String in a recursion-safe way.<br>
     *
     * @param map  the map to convert to a String - assumed not null
     * @param paramDescription  the description of the map parameter - assumed not null
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string - assumed not null
     * @param <K>  the type of the keys in the map
     * @param <V>  the type of the values in the map
     * @param <P>  the type of the parameter
     * @return A String
     */
    private <K, V, P extends Map<? extends K, ? extends V>> String mapParamToString(
                    final P map, final ParamDescriptionMap<?, K, V, P> paramDescription, final Map<Class, Set<Integer>> seen) {
        return map.entrySet()
                  .stream()
                  .collect(Collectors.toMap(e -> paramToString(e.getKey(), paramDescription.getKeyClass(), seen),
                                            e -> paramToString(e.getValue(), paramDescription.getValueClass(), seen)))
                  .toString();
    }

    /**
     * Converts a collection parameter to a String in a recursion-safe way.<br>
     *
     * @param collection  the collection to convert to a String - assumed not null
     * @param paramDescription  the description of the collection parameter - assumed not null
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string - assumed not null
     * @param <E>  the type of entries in the collection
     * @param <P>  the type of the parameter
     * @return A String
     */
    private <E, P extends Collection<? extends E>> String collectionParamToString(
                    final P collection, final ParamDescriptionCollection<?, E, P> paramDescription,
                    final Map<Class, Set<Integer>> seen) {
        return collection.stream()
                         .map(e -> paramToString(e, paramDescription.getEntryClass(), seen))
                         .collect(Collectors.toList())
                         .toString();
    }

    /**
     * Converts a parameter to a String in a recursion-safe way.<br>
     *
     * If the provided parameter is null, {@link EasyOverriderConfig#getStringForNull()} is returned.<br>
     *
     * If the provided parameter does not implement {@link RecursionPreventingToString},
     * then the standard {@link Object#toString()} method is returned.<br>
     *
     * If the provided parameter DOES implement {@link RecursionPreventingToString},
     * the hashCode of the object is calculated.<br>
     *
     * If the hashCode is not already in the seen map, it is added, and the parameter's
     * {@link RecursionPreventingToString#toString(Map)} method is called and returned.<br>
     *
     * Otherwise, {@link RecursionPreventingToString#primaryToString()} is called.
     * If that is not null, it is returned.
     * Otherwise, {@link #createToStringResult(Object, Class, List, String, Map)} is called with an empty list
     * supplying {@link EasyOverriderConfig#getStringForRecursionPrevented()} for the value.<br>
     *
     * @param param  the parameter to convert
     * @param paramClass  the class of the parameter being converted - assumed not null
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string - assumed not null
     * @param <P>  the type of the parameter
     * @return A String
     */
    private <P> String paramToString(final P param, final Class<P> paramClass, final Map<Class, Set<Integer>> seen) {
        if (param == null) {
            return easyOverriderConfig.getStringForNull();
        }
        if (!(param instanceof RecursionPreventingToString)) {
            return param.toString();
        }
        if (!seen.containsKey(paramClass)) {
            seen.put(paramClass, new HashSet<>());
        }
        RecursionPreventingToString recursiveParam = (RecursionPreventingToString)param;
        int entryHashCode = param.hashCode();
        if (!seen.get(paramClass).contains(entryHashCode)) {
            seen.get(paramClass).add(entryHashCode);
            return recursiveParam.toString(seen);
        }
        return Optional.ofNullable(recursiveParam.primaryToString())
                       .orElseGet(() -> createToStringResult(param, paramClass, null,
                                                             easyOverriderConfig.getStringForRecursionPrevented(), null));
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
