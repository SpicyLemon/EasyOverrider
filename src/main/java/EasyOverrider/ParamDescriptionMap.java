package EasyOverrider;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Describes a standard {@link Map} parameter in an object.
 *
 * @param <O>  the type of the object
 * @param <P>  the type of the parameter
 * @param <K>  the type of the keys in the Map
 * @param <E>  the type of the values in the Map
 */
public class ParamDescriptionMap<O, K, E, P extends Map<K, E>> extends ParamDescriptionBase<O, P, E> {

    private final Class<K> keyClass;

    private static ParamList<ParamDescriptionMap> paramList;

    static ParamList<ParamDescriptionMap> getMapParamList() {
        if (paramList == null) {
            paramList = ParamDescriptionBase.getBaseParamList()
                                            .extendedBy(ParamDescriptionMap.class)
                                            .withParam("keyClass", ParamDescriptionMap::getKeyClass, Class.class)
                                            .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor for a parameter that is a map of some type.
     *
     * @param parentClass  the class of the object containing the parameter
     * @param paramClass  the class of the parameter (must extend Map)
     * @param keyClass  the class of the keys in the map parameter
     * @param entryClass  the class of the entries in the map parameter
     * @param name  the name of the parameter
     * @param getter  the getter for the parameter
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value for the parameter
     */
    public ParamDescriptionMap(final Class<O> parentClass, final Class<P> paramClass,
                               final Class<K> keyClass, final Class<E> entryClass, final String name,
                               final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction) {
        super(parentClass, paramClass, entryClass, name, getter, paramMethodRestriction);
        this.keyClass = keyClass;
    }

    public Class<K> getKeyClass() {
        return keyClass;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    String valueToStringPreventingRecursion(final P value, final Map<Class, Set<Integer>> seen) {
        return value.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                                              e -> entryToStringPreventingRecursion(e.getValue(), seen)))
                    .toString();
    }

    /**
     * equals method for a ParamDescriptionMap object.
     *
     * @param obj  the object to test against
     * @return True if this ParamDescriptionMap is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getMapParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionMap abstract object.
     *
     * @return an int.
     */
    @Override
    public int hashCode() {
        return getMapParamList().hashCode(this);
    }

    /**
     * toString method for a ParamDescriptionMap object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getMapParamList().toString(this);
    }
}
