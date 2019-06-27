package EasyOverrider;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Describes a standard {@link Map} parameter in an object.
 *
 * @param <O>  the type of the object
 * @param <P>  the type of the parameter
 * @param <K>  the type of the keys in the Map
 * @param <V>  the type of the values in the Map
 */
public class ParamDescriptionMap<O, K, V, P extends Map<? extends K, ? extends V>> extends ParamDescriptionBase<O, P> {

    private final Class<K> keyClass;
    private final Class<V> valueClass;

    private static ParamList<ParamDescriptionMap> paramList;

    static ParamList<ParamDescriptionMap> getMapParamList() {
        if (paramList == null) {
            paramList = ParamDescriptionBase.getBaseParamList()
                                            .extendedBy(ParamDescriptionMap.class)
                                            .withParam("keyClass", ParamDescriptionMap::getKeyClass, Class.class)
                                            .withParam("valueClass", ParamDescriptionMap::getValueClass, Class.class)
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
     * @param valueClass  the class of the values in the map parameter
     * @param name  the name of the parameter
     * @param getter  the getter for the parameter
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value for the parameter
     */
    public ParamDescriptionMap(final Class<O> parentClass, final Class<P> paramClass,
                               final Class<K> keyClass, final Class<V> valueClass, final String name,
                               final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction,
                               final EasyOverriderService easyOverriderService) {
        super(parentClass, paramClass, name, getter, paramMethodRestriction, easyOverriderService);
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public Class<K> getKeyClass() {
        return keyClass;
    }

    public Class<V> getValueClass() {
        return valueClass;
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
        return getOrMakeEasyOverriderService().valueToStringPreventingRecursionMap(value, seen, keyClass, valueClass);
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
