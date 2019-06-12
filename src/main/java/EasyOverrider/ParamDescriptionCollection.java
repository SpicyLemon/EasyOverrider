package EasyOverrider;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Describes a standard {@link Collection} parameter in an object.
 * @param <O> The type of the object.
 * @param <P> The type of the parameter.
 * @param <E> The type of objects contained in the Collection.
 */
public class ParamDescriptionCollection<O, E, P extends Collection<E>> extends ParamDescriptionBase<O, P, E> {

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
     * @param parentClass The class of the object containing the parameter.
     * @param paramClass The class of the parameter (must extend Collection).
     * @param entryClass The class of the entries in the collection parameter.
     * @param name The name of the parameter.
     * @param getter The getter for the parameter.
     * @param paramMethodRestriction The {@link ParamMethodRestriction} value for the parameter.
     * @param recursionPreventingToString The <code>toString(boolean)</code> function that can be used to prevent recursive toString function calls.
     */
    public ParamDescriptionCollection(final Class<O> parentClass, final Class<P> paramClass,
                                      final Class<E> entryClass, final String name,
                                      final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction,
                                      final BiFunction<? super E, Boolean, String> recursionPreventingToString) {
        super(parentClass, paramClass, entryClass, name, getter, paramMethodRestriction, recursionPreventingToString);
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
    String valueToStringPreventingRecursion(final P value) {
        if (recursionPreventingToString != null) {
            return value.stream()
                        .map(e -> recursionPreventingToString.apply(e, true))
                        .collect(Collectors.toList())
                        .toString();
        }
        return value.toString();
    }

    /**
     * equals method for a ParamDescriptionCollection object.
     * @param obj The object to test against.
     * @return True if this ParamDescriptionCollection is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getCollectionParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionCollection abstract object.
     * @return an int.
     */
    @Override
    public int hashCode() {
        return getCollectionParamList().hashCode(this);
    }

    /**
     * toString method for a ParamDescriptionCollection object.
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getCollectionParamList().toString(this);
    }
}
