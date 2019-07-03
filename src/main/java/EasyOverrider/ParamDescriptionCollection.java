package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Describes a standard {@link Collection} parameter in an object.<br>
 *
 * @param <O>  the type of the object
 * @param <P>  the type of the parameter
 * @param <E>  the type of objects contained in the Collection
 */
public class ParamDescriptionCollection<O, E, P extends Collection<? extends E>> extends ParamDescriptionBase<O, P> {

    final Class<E> entryClass;

    private static ParamList<ParamDescriptionCollection> paramList;
    private static final List<Integer> baseConstructorParamOrder = Arrays.asList(1, 2, 4, 5, 6, 7);

    static ParamList<ParamDescriptionCollection> getCollectionParamList() {
        if (paramList == null) {
            paramList = ParamDescriptionBase.getBaseParamList()
                                            .extendedBy(ParamDescriptionCollection.class)
                                            .withParam("entryClass", ParamDescriptionCollection::getEntryClass, Class.class)
                                            .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor for a parameter that is a collection of some type.<br>
     *
     * @param parentClass  the class of the object containing the parameter
     * @param paramClass  the class of the parameter (must extend Collection)
     * @param entryClass  the class of the entries in the collection parameter
     * @param name  the name of the parameter
     * @param getter  the getter for the parameter
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value for the parameter
     * @param easyOverriderService  the easyOverriderService to use for the key pieces of functionality
     */
    public ParamDescriptionCollection(final Class<O> parentClass, final Class<P> paramClass,
                                      final Class<E> entryClass, final String name,
                                      final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction,
                                      final EasyOverriderService easyOverriderService) {
        super(parentClass, paramClass, name, getter, paramMethodRestriction, easyOverriderService, baseConstructorParamOrder);
        this.entryClass = entryClass;
        requireNonNull(entryClass, 3, "entryClass", "ParamDescriptionMap constructor");
    }

    /**
     * Gets the class of the entries in this collection parameter.<br>
     *
     * @return  the class of the entries in this collection parameter
     */
    public Class<E> getEntryClass() {
        return entryClass;
    }

    /**
     * {@inheritDoc}
     *
     * Uses the {@link EasyOverriderService#valueToStringPreventingRecursionCollection(Collection, Map, Class)} method.<br>
     *
     * @param value  {@inheritDoc}
     * @param seen  {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    String valueToStringPreventingRecursion(final P value, final Map<Class, Set<Integer>> seen) {
        return easyOverriderService.valueToStringPreventingRecursionCollection(value, seen, entryClass);
    }

    /**
     * equals method for a ParamDescriptionCollection object.<br>
     *
     * @param obj  the object to test against
     * @return True if this ParamDescriptionCollection is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getCollectionParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionCollection object.<br>
     *
     * @return an int.
     */
    @Override
    public int hashCode() {
        return getCollectionParamList().hashCode(this);
    }

    /**
     * paramValueToString method for a ParamDescriptionCollection object.<br>
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getCollectionParamList().toString(this);
    }
}
