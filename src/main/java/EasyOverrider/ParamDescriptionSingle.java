package EasyOverrider;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Describes a standard parameter in an object.
 *
 * @param <O>  the type of the object
 * @param <P>  the type of the parameter
 */
public class ParamDescriptionSingle<O, P> extends ParamDescriptionBase<O, P> {

    private static ParamList<ParamDescriptionSingle> paramList;

    static ParamList<ParamDescriptionSingle> getSingleParamList() {
        if (paramList == null) {
            paramList = ParamDescriptionBase.getBaseParamList()
                                            .extendedBy(ParamDescriptionSingle.class)
                                            .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor for a parameter.
     * @param parentClass The class of the object containing the parameter.
     * @param paramClass The class of the parameter.
     * @param name The name of the parameter.
     * @param getter The getter for the parameter.
     * @param paramMethodRestriction The {@link ParamMethodRestriction} value for the parameter.
     */
    public ParamDescriptionSingle(final Class<O> parentClass, final Class<P> paramClass, final String name,
                                  final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction) {
        super(parentClass, paramClass, name, getter, paramMethodRestriction);
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    String valueToStringPreventingRecursion(final P value, final Map<Class, Set<Integer>> seen) {
        return objectToStringPreventingRecursion(paramClass, value, seen);
    }

    /**
     * equals method for a ParamDescriptionSingle object.
     *
     * @param obj  the object to test against
     * @return True if this ParamDescriptionSingle is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getSingleParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionSingle abstract object.
     *
     * @return an int.
     */
    @Override
    public int hashCode() {
        return getSingleParamList().hashCode(this);
    }

    /**
     * toString method for a ParamDescriptionSingle object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getSingleParamList().toString(this);
    }
}
