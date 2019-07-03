package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Describes a standard Map parameter in an object.<br>
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
    private static final List<Integer> baseConstructorParamOrder = Arrays.asList(1, 2, 5, 6, 7);

    public ParamList<ParamDescriptionMap> getParamListMap() {
        if (paramList == null) {
            paramList = ParamDescriptionBase.getParamListBase()
                                            .extendedBy(ParamDescriptionMap.class)
                                            .withParam("keyClass", ParamDescriptionMap::getKeyClass, Class.class)
                                            .withParam("valueClass", ParamDescriptionMap::getValueClass, Class.class)
                                            .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor for a parameter that is a map of some type.<br>
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
                               final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction) {
        super(parentClass, paramClass, name, getter, paramMethodRestriction, baseConstructorParamOrder);
        requireNonNull(keyClass, 3, "keyClass", "ParamDescriptionMap constructor");
        requireNonNull(valueClass, 4, "valueClass", "ParamDescriptionMap constructor");
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    /**
     * Gets the class of the keys in this map parameter.<br>
     *
     * @return  the class of the keys in this map parameter
     */
    public Class<K> getKeyClass() {
        return keyClass;
    }

    /**
     * Gets the class of the values in this map parameter.<br>
     *
     * @return  the class of the values in this map parameter
     */
    public Class<V> getValueClass() {
        return valueClass;
    }

    /**
     * equals method for a ParamDescriptionMap object.<br>
     *
     * @param obj  the object to test against
     * @return True if this ParamDescriptionMap is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getParamListMap().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionMap abstract object.<br>
     *
     * @return an int.
     */
    @Override
    public int hashCode() {
        return getParamListMap().hashCode(this);
    }

    /**
     * toString method for a ParamDescriptionMap object.<br>
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getParamListMap().toString(this);
    }
}
