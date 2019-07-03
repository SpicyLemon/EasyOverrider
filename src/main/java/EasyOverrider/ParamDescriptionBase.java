package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.getIndexOrDefault;
import static EasyOverrider.EasyOverriderUtils.requireNonNull;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;

import java.util.List;
import java.util.function.Function;

/**
 * An abstract class that implements most of the functionality of a ParamDescription.<br>
 *
 * @param <O>  the type of object in question
 * @param <P>  the type of the parameter in question
 */
public abstract class ParamDescriptionBase<O, P> implements ParamDescription<O, P> {

    final Class<O> parentClass;
    final Class<P> paramClass;
    final String name;
    final Function<? super O, P> getter;
    final ParamMethodRestriction paramMethodRestriction;
    EasyOverriderService easyOverriderService;

    private static ParamList<ParamDescriptionBase> paramList;

    /**
     * Get the ParamList for a ParamDescriptionBase.<br>
     *
     * Things that extend this abstract class should use <code>ParamDescriptionBase.getBaseParamList().extendedBy(...)</code>
     * to create their own <code>ParamList</code>.
     *
     * @return A ParamList&lt;ParamDescriptionBase&gt; object.
     */
    static ParamList<ParamDescriptionBase> getBaseParamList() {
        if (paramList == null) {
            //This will create the ParamList if it's not already set.
            //The reason it does this rather than just having it statically set when the variable is created,
            // is that it uses itself. The class code needs to be loaded before it can be used.
            paramList = ParamList.forClass(ParamDescriptionBase.class)
                                 .withParam("parentClass", ParamDescriptionBase::getParentClass, Class.class)
                                 .withParam("paramClass", ParamDescriptionBase::getParamClass, Class.class)
                                 .withParam("name", ParamDescriptionBase::getName, String.class)
                                 .withParam("getter", ParamDescriptionBase::getGetter, INCLUDED_IN_TOSTRING_ONLY, Function.class)
                                 .withParam("paramMethodRestriction",
                                            ParamDescriptionBase::getParamMethodRestriction,
                                            ParamMethodRestriction.class)
                                 .withParam("easyOverriderService", (pdb) -> pdb.easyOverriderService, EasyOverriderService.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Constructor for the extending class to use to set all the pieces pre-implemented in this abstract class.<br>
     *
     * @param parentClass  the class of the parent object - cannot be null
     * @param paramClass  the class of the parameter - cannot be null
     * @param name  the name of the parameter - cannot be null
     * @param getter  the getter for the parameter - cannot be null
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} for the parameter - cannot be null
     * @param easyOverriderService  the easyOverriderService to use for the key pieces of functionality - cannot be null
     * @param paramIndexNumbers  a list of parameter index numbers used for possible validation error messages
     * @throws IllegalArgumentException If any parameter is null.
     */
    ParamDescriptionBase(final Class<O> parentClass, final Class<P> paramClass, final String name,
                         final Function<? super O, P> getter, final ParamMethodRestriction paramMethodRestriction,
                         final EasyOverriderService easyOverriderService,
                         final List<Integer> paramIndexNumbers) {
        requireNonNull(parentClass, getIndexOrDefault(paramIndexNumbers, 1), "parentClass", "ParamDescriptionBase constructor");
        requireNonNull(paramClass, getIndexOrDefault(paramIndexNumbers, 2), "paramClass", "ParamDescriptionBase constructor");
        requireNonNull(name, getIndexOrDefault(paramIndexNumbers, 3), "name", "ParamDescriptionBase constructor");
        requireNonNull(getter, getIndexOrDefault(paramIndexNumbers, 4), "getter", "ParamDescriptionBase constructor");
        requireNonNull(paramMethodRestriction, getIndexOrDefault(paramIndexNumbers, 5), "paramMethodRestriction", "ParamDescriptionBase constructor");
        requireNonNull(easyOverriderService, getIndexOrDefault(paramIndexNumbers, 6), "easyOverriderService", "ParamDescriptionBase constructor");
        this.parentClass = parentClass;
        this.paramClass = paramClass;
        this.name = name;
        this.getter = getter;
        this.paramMethodRestriction = paramMethodRestriction;
        this.easyOverriderService = easyOverriderService;
    }

    @Override
    public Class<O> getParentClass() {
        return parentClass;
    }

    @Override
    public Class<P> getParamClass() {
        return paramClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Function<? super O, P> getGetter() {
        return getter;
    }

    @Override
    public ParamMethodRestriction getParamMethodRestriction() {
        return paramMethodRestriction;
    }

    /**
     * {@inheritDoc}
     *
     * @param easyOverriderService  {@inheritDoc} - cannot be null
     * @throws IllegalArgumentException if the provided EasyOverriderService is null.
     */
    @Override
    public void setService(final EasyOverriderService easyOverriderService) {
        requireNonNull(easyOverriderService, 1, "easyOverriderService", "setService");
        this.easyOverriderService = easyOverriderService;
    }

    /**
     * {@inheritDoc}
     *
     * Uses the {@link ParamMethodRestriction#isEqualsIgnore()} method.<br>
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isEqualsIgnore() {
        return paramMethodRestriction.isEqualsIgnore();
    }

    /**
     * {@inheritDoc}
     *
     * Uses the {@link ParamMethodRestriction#isEqualsInclude()} method.<br>
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isEqualsInclude() {
        return paramMethodRestriction.isEqualsInclude();
    }

    /**
     * {@inheritDoc}
     *
     * Uses the {@link ParamMethodRestriction#isHashCodeIgnore()} method.<br>
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isHashCodeIgnore() {
        return paramMethodRestriction.isHashCodeIgnore();
    }

    /**
     * {@inheritDoc}
     *
     * Uses the {@link ParamMethodRestriction#isHashCodeInclude()} method.<br>
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isHashCodeInclude() {
        return paramMethodRestriction.isHashCodeInclude();
    }

    /**
     * {@inheritDoc}
     *
     * Uses the {@link ParamMethodRestriction#isToStringIgnore()} method.<br>
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isToStringIgnore() {
        return paramMethodRestriction.isToStringIgnore();
    }

    /**
     * {@inheritDoc}
     *
     * Uses the {@link ParamMethodRestriction#isToStringInclude()} method.<br>
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isToStringInclude() {
        return paramMethodRestriction.isToStringInclude();
    }

    /**
     * equals method for a ParamDescriptionBase abstract object.<br>
     *
     * @param obj  the object to test against
     * @return True if this ParamDescriptionBase is equal to the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getBaseParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamDescriptionBase abstract object.<br>
     *
     * @return An int.
     */
    @Override
    public int hashCode() {
        return getBaseParamList().hashCode(this);
    }

    /**
     * toString method for a ParamDescriptionBase abstract object.<br>
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return getBaseParamList().toString(this);
    }
}
