package EasyOverrider;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Describes a standard parameter in an object.<br>
 *
 * @param <O>  the type of the object
 * @param <P>  the type of the parameter
 */
public class ParamDescriptionSingle<O, P> extends ParamDescriptionBase<O, P> {

    private boolean isPrimary;

    private static ParamList<ParamDescriptionSingle> paramList;
    private static final List<Integer> baseConstructorParamOrder = Arrays.asList(1, 2, 3, 4, 5, 6);

    static ParamList<ParamDescriptionSingle> getSingleParamList() {
        if (paramList == null) {
            paramList = ParamDescriptionBase.getBaseParamList()
                                            .extendedBy(ParamDescriptionSingle.class)
                                            .withParam("isPrimary", ParamDescriptionSingle::isPrimary, Boolean.class)
                                            .andThatsIt();
        }
        return paramList;
    }

    /**
     * Standard constructor for a generic parameter.<br>
     *
     * @param parentClass The class of the object containing the parameter.
     * @param paramClass The class of the parameter.
     * @param name The name of the parameter.
     * @param getter The getter for the parameter.
     * @param paramMethodRestriction The {@link ParamMethodRestriction} value for the parameter.
     * @param easyOverriderService  the easyOverriderService to use for the key pieces of functionality
     * @param isPrimary  whether or not this parameter is a primary one
     * @throws IllegalArgumentException If any parameter is null.
     * @see ParamDescriptionCollection
     * @see ParamDescriptionMap
     */
    public ParamDescriptionSingle(final Class<O> parentClass, final Class<P> paramClass, final String name,
                                  final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction,
                                  final EasyOverriderService easyOverriderService, final boolean isPrimary) {
        super(parentClass, paramClass, name, getter, paramMethodRestriction, easyOverriderService, baseConstructorParamOrder);
        this.isPrimary = isPrimary;
    }

    /**
     * Returns whether or not this ParamDescription is [part of] a primary key.<br>
     *
     * @return True if it repesents a key component. False otherwise.
     */
    public boolean isPrimary() {
        return isPrimary;
    }

    /**
     * equals method for a ParamDescriptionSingle object.<br>
     *
     * @param obj  the object to test against
     * @return True if this ParamDescriptionSingle is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getSingleParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionSingle abstract object.<br>
     *
     * @return an int.
     */
    @Override
    public int hashCode() {
        return getSingleParamList().hashCode(this);
    }

    /**
     * toString method for a ParamDescriptionSingle object.<br>
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getSingleParamList().toString(this);
    }
}
