package EasyOverrider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Describes a standard {@link Collection} parameter in an object.
 *
 * @param <O>  the type of the object
 * @param <P>  the type of the parameter
 * @param <E>  the type of objects contained in the Collection
 */
public class ParamDescriptionCollection<O, E, P extends Collection<? extends E>> extends ParamDescriptionBase<O, P, E> {

    private static ParamList<ParamDescriptionCollection> paramList;

    static ParamList<ParamDescriptionCollection> getCollectionParamList() {
        if (paramList == null) {
            paramList = ParamDescriptionBase.getBaseParamList()
                                            .extendedBy(ParamDescriptionCollection.class)
                                            .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor for a parameter that is a collection of some type.
     *
     * @param parentClass  the class of the object containing the parameter
     * @param paramClass  the class of the parameter (must extend Collection)
     * @param entryClass  the class of the entries in the collection parameter
     * @param name  the name of the parameter
     * @param getter  the getter for the parameter
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value for the parameter
     */
    public ParamDescriptionCollection(final Class<O> parentClass, final Class<P> paramClass,
                                      final Class<E> entryClass, final String name,
                                      final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction) {
        super(parentClass, paramClass, entryClass, name, getter, paramMethodRestriction);
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    String valueToStringPreventingRecursion(final P value, final Map<Class, Set<Integer>> seen) {
        return value.stream()
                    .map(e -> entryToStringPreventingRecursion(e, seen))
                    .collect(Collectors.toList())
                    .toString();
    }

    /**
     * equals method for a ParamDescriptionCollection object.
     *
     * @param obj  the object to test against
     * @return True if this ParamDescriptionCollection is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getCollectionParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionCollection abstract object.
     *
     * @return an int.
     */
    @Override
    public int hashCode() {
        return getCollectionParamList().hashCode(this);
    }

    /**
     * toString method for a ParamDescriptionCollection object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getCollectionParamList().toString(this);
    }
}
