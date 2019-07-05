package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Describes a standard {@link Collection} parameter in an object.<br>
 *
 * @param <O>  the type of the object
 * @param <P>  the type of the parameter
 * @param <E>  the type of objects contained in the Collection
 */
public class ParamDescriptionCollection<O, E, P extends Collection<? extends E>> extends ParamDescriptionBase<O, P> {

    private final Class<E> entryClass;

    private static ParamList<ParamDescriptionCollection> paramList;
    private static final List<Integer> baseConstructorParamOrder = Arrays.asList(1, 2, 4, 5, 6);

    private static ParamList<ParamDescriptionCollection> getParamListCollection() {
        if (paramList == null) {
            paramList = ParamDescriptionBase.getParamListBase()
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
     * @param paramUsage  the {@link ParamUsage} value for the parameter
     */
    public ParamDescriptionCollection(final Class<O> parentClass, final Class<P> paramClass,
                                      final Class<E> entryClass, final String name,
                                      final Function<? super O, P> getter, final ParamUsage paramUsage) {
        super(parentClass, paramClass, name, getter, paramUsage, baseConstructorParamOrder);
        requireNonNull(entryClass, 3, "entryClass", "ParamDescriptionMap constructor");
        this.entryClass = entryClass;
    }

    /**
     * {@inheritDoc}
     *
     * Calls the getter on the object.
     * If that result is null, just passes that null and the parameter class into the provided BiFunction and returns that result.
     * Otherwise, it loops through the collection and calls the BiFunction on each entry using the entry class.
     * The resulting strings are all collected into a <code>List&lt;String&gt;</code>
     * and then converted to a String using <code>List.toString()</code>.<br>
     *
     * @param obj  {@inheritDoc}
     * @param objectToString  {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getParamString(O obj, BiFunction<Object, Class, String> objectToString) {
        P collection = getter.apply(obj);
        if (collection == null) {
            return objectToString.apply(collection, paramClass);
        }
        return collection.stream()
                         .map(e -> objectToString.apply(e, entryClass))
                         .collect(Collectors.toList())
                         .toString();
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
     * equals method for a ParamDescriptionCollection object.<br>
     *
     * @param obj  the object to test against
     * @return True if this ParamDescriptionCollection is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getParamListCollection().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionCollection object.<br>
     *
     * @return an int.
     */
    @Override
    public int hashCode() {
        return getParamListCollection().hashCode(this);
    }

    /**
     * paramValueToString method for a ParamDescriptionCollection object.<br>
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getParamListCollection().toString(this);
    }
}
