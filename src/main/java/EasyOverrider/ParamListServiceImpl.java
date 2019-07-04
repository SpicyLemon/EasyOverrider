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
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The default implementation of a ParamListService.<br>
 */
public class ParamListServiceImpl implements ParamListService {

    private ParamListServiceConfig config;

    private static ParamList<ParamListServiceImpl> paramList;

    private static ParamList<ParamListServiceImpl> getParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(ParamListServiceImpl.class)
                                 .withParam("config", ParamListServiceImpl::getConfig, ParamListServiceConfig.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor that just uses the default config.<br>
     */
    public ParamListServiceImpl() {
        config = new ParamListServiceConfig();
    }

    /**
     * Constructor that takes in a config.<br>
     *
     * @param config  the config to use - cannot be null
     * @throws IllegalArgumentException if the provided config is null.
     */
    public ParamListServiceImpl(ParamListServiceConfig config) {
        requireNonNull(config, 1, "config", "ParamListServiceImpl constructor");
        this.config = config;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public ParamListServiceConfig getConfig() {
        return config;
    }

    /**
     * {@inheritDoc}
     *
     * @param paramListServiceConfig  {@inheritDoc} - cannot be null
     * @return {@inheritDoc}
     * @throws IllegalArgumentException if the provided parameter is null.
     */
    public ParamListService setConfig(ParamListServiceConfig paramListServiceConfig) {
        requireNonNull(paramListServiceConfig, 1, "config", "setConfig");
        this.config = paramListServiceConfig;
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
     * First, the class name String is retrieved using the {@link ParamListServiceConfig#getClassNameGetter()}.<br>
     * Then, the hashCode is calculated using <code>thisObj.hashCode()</code>,
     * and converted to a string using {@link ParamListServiceConfig#getHashCodeToString()}.<br>
     * Then, the parameters String is created in a recursion-safe way.<br>
     * Lastly, {@link ParamListServiceConfig#getToStringFormat()} is used to create the final String.
     * Arguments are provided to the {@link ParamListServiceConfig#getToStringFormat()} in this order:
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
                                    config.getStringForEmptyParamList(),
                                    Optional.ofNullable(seen).orElseGet(HashMap::new));
    }

    /**
     * {@inheritDoc}
     *
     * First, the class name String is retrieved using the {@link ParamListServiceConfig#getClassNameGetter()}.<br>
     * Then, the hashCode is calculated using <code>thisObj.hashCode()</code>,
     * and converted to a string using {@link ParamListServiceConfig#getHashCodeToString()}.<br>
     * Then, the parameters String is created using only the primary parameters.<br>
     * Lastly, {@link ParamListServiceConfig#getToStringFormat()} is used to create the final String.
     * Arguments are provided to the {@link ParamListServiceConfig#getToStringFormat()} in this order:
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
        Predicate<ParamDescription<? super O, ?>> filter = pd -> pd.isToStringInclude()
                                                                 && ParamDescriptionSingle.class.isAssignableFrom(pd.getClass())
                                                                 && ((ParamDescriptionSingle)pd).isPrimary();
        List<ParamDescription<? super O, ?>> paramDescriptions = getFilteredParamList(filter, paramList);
        return createToStringResult(thisObj, paramList.getParentClass(), paramDescriptions,
                                    config.getStringForRecursionPrevented(), new HashMap<>());
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
     * Put together the pieces to create the final toString result.<br>
     *
     * Generates the object's hashCode using {@link Object#hashCode()} then converts it to a String
     * using the {@link ParamListServiceConfig#getHashCodeToString()} Function.
     * Uses the {@link ParamListServiceConfig#getClassNameGetter()} Function to create the class name String.
     * Then uses the provided <code>paramDescriptions</code> list to generate a String of the parameters joined together
     * using {@link ParamListServiceConfig#getParameterDelimiter()}.
     * If the provided <code>paramDescriptions</code> list is null or empty, the provided <code>defaultForEmpty</code> is used instead.
     * Then uses the {@link ParamListServiceConfig#getToStringFormat()} format to combine the
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
        String hashCode = config.getHashCodeToString().apply(obj.hashCode());
        String className = config.getClassNameGetter().apply(objClass);
        String paramsString = paramDescriptions == null || paramDescriptions.isEmpty() ? defaultForEmpty :
                        paramDescriptions.stream()
                                         .map(pd -> getNameValueString(obj, pd, seen))
                                         .collect(Collectors.joining(config.getParameterDelimiter()));
        return String.format(config.getToStringFormat(), className, hashCode, paramsString);
    }

    /**
     * Creates a name/value String for a parameter in an object.<br>
     *
     * Makes a call to {@link ParamDescription#getParamString(Object, BiFunction)} with the object and
     * a reference to {@link #objectToString(Object, Class, Map)}.
     * Then, if that result is not the {@link ParamListServiceConfig#getStringForNull()}
     * or {@link ParamListServiceConfig#getStringForRecursionPrevented()} values,
     * the {@link ParamListServiceConfig#getParameterValueFormat()} is applied to it.<br>
     *
     * Finally, the {@link ParamListServiceConfig#getNameValueFormat()} is applied,
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
        String value = paramDescription.getParamString(obj, (p, c) -> objectToString(p, c, seen));
        if (!value.equals(config.getStringForNull())
            && !value.equals(config.getStringForRecursionPrevented())) {
            value = String.format(config.getParameterValueFormat(), value);
        }
        return String.format(config.getNameValueFormat(), paramDescription.getName(), value);
    }

    /**
     * Converts an object to a String in a recursion-safe way.<br>
     *
     * If the provided object is null, {@link ParamListServiceConfig#getStringForNull()} is returned.<br>
     *
     * If the provided object does not implement {@link RecursionPreventingToString},
     * then the standard {@link Object#toString()} method is returned.<br>
     *
     * If the provided object DOES implement {@link RecursionPreventingToString},
     * the hashCode of the object is calculated.<br>
     *
     * If the hashCode is not already in the seen map, it is added, and the parameter's
     * {@link RecursionPreventingToString#toString(Map)} method is called and returned.<br>
     *
     * Otherwise, recursion has been detected.
     * The object's {@link RecursionPreventingToString#primaryToString()} method is called.
     * If that is not null, it is returned.
     * Otherwise, {@link #createToStringResult(Object, Class, List, String, Map)} is called with an empty list
     * and supplying {@link ParamListServiceConfig#getStringForRecursionPrevented()} for the value.<br>
     *
     * @param obj  the parameter to convert
     * @param objClass  the class of the parameter being converted - assumed not null
     * @param seen  the map of classes to sets of hashCodes indicating objects that have already been converted to a string - assumed not null
     * @param <P>  the type of the parameter
     * @return A String
     */
    private <P> String objectToString(final P obj, final Class<P> objClass, final Map<Class, Set<Integer>> seen) {
        if (obj == null) {
            return config.getStringForNull();
        }
        if (!RecursionPreventingToString.class.isAssignableFrom(objClass)) {
            return obj.toString();
        }
        int objHashCode = obj.hashCode();
        RecursionPreventingToString recursiveObject = (RecursionPreventingToString)obj;
        if (!seen.containsKey(objClass)) {
            seen.put(objClass, new HashSet<>());
        }
        if (!seen.get(objClass).contains(objHashCode)) {
            seen.get(objClass).add(objHashCode);
            return recursiveObject.toString(seen);
        }
        return Optional.ofNullable(recursiveObject.primaryToString())
                       .orElseGet(() -> createToStringResult(obj, objClass, null,
                                                             config.getStringForRecursionPrevented(), null));
    }

    /**
     * equals method for a ParamListServiceImpl object.<br>
     *
     * @param obj  the object to test against
     * @return True if this ParamListServiceImpl is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamListServiceImpl object.<br>
     *
     * @return an int
     */
    @Override
    public int hashCode() {
        return getParamList().hashCode(this);
    }

    /**
     * toString method for a ParamListServiceImpl object.<br>
     *
     * @return A string representation of this object
     */
    @Override
    public String toString() {
        return getParamList().toString(this);
    }
}
