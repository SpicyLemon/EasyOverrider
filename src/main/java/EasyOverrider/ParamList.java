package EasyOverrider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Describes a list of parameters that describe the parameters in a class.
 * <pre>
 * {@code
 *
 * private static final ParamList<Foo> paramList =
 *                 ParamList.forClass(Foo.class)
 *                          .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
 *                          .withParam("bar", Foo::getBar, String.class)
 *                          .withParam("baz", Foo::getBaz, WeirdThing::toString, WeirdThing.class)
 *                          .withCollection("moreFoo", Foo::getMoreFoo, INCLUDED_IN_TOSTRING_ONLY, Foo::toString, List.class, Foo.class)
 *                          .andThatsIt();
 * }
 * </pre>
 *
 * The above example assumes you've got a <code>public String toString(final boolean preventingRecursion)</code> method
 * in both Foo and WeirdThing.<br>
 * Now you can set up your equals, hashCode and toString as follows in your classes that have a ParamList.
 *
 * <pre>
 *
 * {@code
 *
 * public boolean equals(Object obj) {
 *     return paramList.equals(this, obj);
 * }
 *
 * public int hashCode() {
 *     return paramList.hashCode(this);
 * }
 *
 * public String toString() {
 *     return toString(false);
 * }
 *
 * public String toString(final boolean preventingRecursion) {
 *     return paramList.toString(this, preventingRecursion);
 * }
 * }
 * </pre>
 * @param <O>  the class you're describing
 */
public class ParamList<O> {

    final private Class<O> parentClass;
    final private List<String> paramOrder;
    final private Map<String, ParamDescription<? super O, ?, ?>> paramDescriptionMap;

    private static ParamList<ParamList> paramList;

    static ParamList<ParamList> getParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(ParamList.class)
                                 .withParam("parentClass", ParamList::getParentClass, Class.class)
                                 .withCollection("paramOrder", ParamList::getParamOrder, List.class, String.class)
                                 .withMap("paramDescriptionMap", ParamList::getParamDescriptionMap, Map.class,
                                          String.class, ParamDescription.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Constructor for if you've already got a list of ParamDescription objects.<br>
     *
     * Alternatively (preferably), you can use the static {@link ParamList#forClass(Class)} method
     * combined with the {@link ParamListBuilder} methods,
     * and finished off with the {@link ParamListBuilder#andThatsIt()} method to create a new ParamList.
     * @param parentClass  the class of the object these parameters represent
     * @param paramDescriptionMap  a map of name to ParamDescription objects describing the parameters in the parent object
     * @param paramOrder  the order that the parameters should be in
     * @throws IllegalArgumentException if the sizes of the provided paramOrder list and paramDescriptionMap are different.
     * @throws IllegalArgumentException if an entry exists in the paramOrder that doesn't have a matching key in the paramDescriptionMap.
     */
     ParamList(final Class<O> parentClass, final Map<String, ParamDescription<? super O, ?, ?>> paramDescriptionMap, final List<String> paramOrder) {
        requireNonNull(parentClass, 1, "parentClass", "ParamList constructor");
         requireNonNull(paramDescriptionMap, 2, "paramDescriptionMap", "ParamList constructor");
         requireNonNull(paramOrder, 3, "paramOrder", "ParamList constructor");
        if (paramOrder.size() != paramDescriptionMap.size()) {
            throw new IllegalArgumentException("The size of the paramDescriptionMap [" + paramDescriptionMap.size() + "] " +
                                               "does not equal the size of the paramOrder list [" + paramOrder.size() + "]");
        }
        if (!paramOrder.stream().allMatch(paramDescriptionMap::containsKey)) {
            throw new IllegalArgumentException("Parameter names were found in the order list " +
                                               "that do not exist in the paramDescriptionMap: " +
                                               paramOrder.stream()
                                                         .filter(name -> !paramDescriptionMap.containsKey(name))
                                                         .collect(Collectors.joining(", ")));
        }
        this.parentClass = parentClass;
        this.paramDescriptionMap = new HashMap<>(paramDescriptionMap);
        this.paramOrder = new ArrayList<>(paramOrder);
    }

    /**
     * Getter for the parentClass parameter that records the class this ParamList applies to.
     *
     * @return A Class.
     */
    public Class<O> getParentClass() {
        return parentClass;
    }

    /**
     * Getter for the order parameter.
     *
     * @return An unmodifiable list of Strings
     */
    public List<String> getParamOrder() {
        return Collections.unmodifiableList(paramOrder);
    }

    /**
     * Getter for the paramDescriptionMap parameter.
     *
     * @return An unmodifiable Map of Strings to ParamDescription values.
     */
    public Map<String, ParamDescription<? super O, ?, ?>> getParamDescriptionMap() {
        return Collections.unmodifiableMap(paramDescriptionMap);
    }

    /**
     * Gets the list of all param descriptions in an unmodifiable state.
     *
     * @return An unmodifiable list of ParamDescription objects.
     */
    public List<ParamDescription<? super O, ?, ?>> getAllParamDescriptions() {
        return getFilteredParamList((p) -> true);
    }

    /**
     * Gets the list of all param descriptions that are to be used in an equals() method.
     *
     * @return An unmodifiable list of ParamDescription objects.
     */
    public List<ParamDescription<? super O, ?, ?>> getEqualsParamDescriptions() {
        return getFilteredParamList(ParamDescription::isEqualsInclude);
    }

    /**
     * Gets the list of all param descriptions that are to be used in a hashCode() method.
     *
     * @return An unmodifiable list of ParamDescription objects.
     */
    public List<ParamDescription<? super O, ?, ?>> getHashCodeParamDescriptions() {
        return getFilteredParamList(ParamDescription::isHashCodeInclude);
    }

    /**
     * Gets the list of all param descriptions that are to be used in a toString() method.
     *
     * @return An unmodifiable list of ParamDescription objects.
     */
    public List<ParamDescription<? super O, ?, ?>> getToStringParamDescriptions() {
        return getFilteredParamList(ParamDescription::isToStringInclude);
    }

    /**
     * Filters the params list using the provided predicate and returns an unmodifiable list of ParamDescriptions.
     *
     * @param filter  the predicate to use in the filter, e.g. ParamDescription::isToStringInclude - cannot be null
     * @return An unmodifialbe list of ParamDescriptions.
     */
    private List<ParamDescription<? super O, ?, ?>> getFilteredParamList(final Predicate<ParamDescription<? super O, ?, ?>> filter) {
        return Collections.unmodifiableList(paramOrder.stream()
                                                      .map(paramDescriptionMap::get)
                                                      .filter(filter)
                                                      .collect(Collectors.toList()));
    }

    /**
     * Checks to see if the provided objects are equal as described by this paramList.<br>
     *
     * This first checks to see if the two provided objects are equal using <code>==</code>; if so, return true.<br>
     * Then, if one is null, return false.<br>
     * Then check to see which of the objects is an instance of the parent class.
     * If neither of them are, return <code>thisObj.equals(thatObj);</code>.
     * If one of them isn't, return false.
     * Lastly, go through all the param descriptions that are to be included in the equals function
     * and make sure all the appropriate parameters are equal.
     *
     * @param thisObj  the main object you're checking against
     * @param thatObj  the other object you're wanting to test
     * @return True if both objects are equal. False otherwise.
     */
    public boolean equals(final Object thisObj, final Object thatObj) {
        if (thisObj == thatObj) {
            return true;
        }
        if (thisObj == null || thatObj == null) {
            return false;
        }
        boolean thisIsInstance = parentClass.isInstance(thisObj);
        boolean thatIsInstance = parentClass.isInstance(thatObj);
        if (!thisIsInstance && !thatIsInstance) {
            return thisObj.equals(thatObj);
        }
        if (!thisIsInstance || !thatIsInstance) {
            return false;
        }
        @SuppressWarnings("unchecked")
        O thisO = (O)thisObj;
        @SuppressWarnings("unchecked")
        O thatO = (O)thatObj;
        return getEqualsParamDescriptions().stream().allMatch(pd -> pd.paramsAreEqual(thisO, thatO));
    }

    /**
     * Generates the hashCode for the provided object using the appropriate parameters.
     *
     * @param thisObj  the object to get the parameter values from
     * @return An integer.
     */
    public int hashCode(final O thisObj) {
        requireNonNull(thisObj, 1, "thisObj", "hashCode");
        return Objects.hash(getHashCodeParamDescriptions().stream().map(pd -> pd.get(thisObj)).toArray());
    }

    /**
     * Gets a String representation of the provided object using the appropriate parameters.
     *
     * @param thisObj  the object to get the parameter values from
     * @return A string.
     */
    public String toString(final O thisObj) {
        return toString(thisObj, false);
    }

    /**
     * Gets a String representation of the provided object using the appropriate parameters and preventing recursion if needed.
     *
     * @param thisObj  the object to get the parameter values from
     * @param preventingRecursion  the flag for whether or not we should be preventing recursion
     * @return A string representation of the given object.
     */
    public String toString(final O thisObj, final boolean preventingRecursion) {
        requireNonNull(thisObj, 1, "thisObj", "hahsCode");
        return parentClass.getName() + "@" + thisObj.hashCode() + " [" + getParamsString(thisObj, preventingRecursion) + "]";
    }

    /**
     * Gets a String representation of the desired parameters in the provided object.
     *
     * @param thisObj  the object to get the parameter values from
     * @return A String of comma-space delimited name/value Strings.
     */
    public String getParamsString(final O thisObj) {
        return getParamsString(thisObj, false);
    }

    /**
     * Gets a String representation of the desired parameters in the provided object.
     *
     * @param thisObj  the object to get the parameter values from
     * @param preventingRecursion  the flag for whether or not we should be preventing recursion
     * @return A String of comma-space delimited name/value Strings.
     */
    public String getParamsString(final O thisObj, final boolean preventingRecursion) {
        requireNonNull(thisObj, 1, "thisObj", "getParamsString");
        List<ParamDescription<? super O, ?, ?>> toStringParamDescriptions = getToStringParamDescriptions();
        if (toStringParamDescriptions.size() <= 0) {
            return " ";
        }
        return toStringParamDescriptions.stream()
                                        .map(pd -> pd.getNameValueString(thisObj, preventingRecursion))
                                        .collect(Collectors.joining(", "));
    }

    /**
     * Creates a new ParamListBuilder based on this ParamList.<br>
     *
     * This allows you to extend an already created ParamList when, for example, extending a class, and adding new parameters.
     *
     * @param newParentClass  the new class that extends the class that this ParamList is for
     * @param <C>  the type of the new class
     * @return A new {@link ParamListBuilder}.
     */
    public <C extends O> ParamListBuilder<C> extendedBy(Class<C> newParentClass) {
        return new ParamListBuilder<C>(newParentClass, this);
    }

    /**
     * Creates a new ParamListBuilder based on this ParamList using the provided ParamMethodRestrictionRestriction.<br>
     *
     * This allows you to extend an already created ParamList when, for example, extending a class, and adding new parameters.
     * Previously created entries in the ParamList are grandfathered in with respectes to the ParamMethodRestrictionRestriction.
     * As such, you only need to set it when your new parameters require (somehow) an <code>__UNSAFE</code> {@link ParamMethodRestriction}.
     *
     * @param newParentClass  the new class that extends the class that this ParamList is for
     * @param paramMethodRestrictionRestriction  the {@link ParamMethodRestrictionRestriction} to use
     * @param <C>  the type of the new class
     * @return A new {@link ParamListBuilder}.
     */
    public <C extends O> ParamListBuilder<C> extendedBy(Class<C> newParentClass,
                                                        ParamMethodRestrictionRestriction paramMethodRestrictionRestriction) {
        return new ParamListBuilder<C>(newParentClass, this, paramMethodRestrictionRestriction);
    }

    /**
     * equals method for a ParamList object.
     *
     * @param obj  the object to test against
     * @return True if this ParamList equals the provided object. False otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        return getParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamList object.
     *
     * @return An int.
     */
    @Override
    public int hashCode() {
        return getParamList().hashCode(this);
    }

    /**
     * toString method for a ParamList object.
     *
     * @return A String.
     */
    @Override
    public String toString() {
        return getParamList().toString(this);
    }

    /**
     * Kicks off a ParamListBuilder for the provided class.
     *
     * @param parentClass  the class you're building the parameter list for
     * @param <C>  the class you're building the parameter list for
     * @return A {@link ParamListBuilder} for the specified class.
     */
    public static <C> ParamListBuilder<C> forClass(final Class<C> parentClass) {
        return new ParamListBuilder<>(parentClass);
    }

    /**
     * Kicks off a ParamListBuilder for the provided class, with the provided ParamMethodRestrictionRestriction.
     *
     * @param parentClass  the class you're building the parameter list for
     * @param paramMethodRestrictionRestriction  the {@link ParamMethodRestrictionRestriction} to use
     * @param <C>  the class you're building the parameter list for
     * @return A {@link ParamListBuilder} for the specified class.
     */
    public static <C> ParamListBuilder<C> forClass(final Class<C> parentClass,
                                                   ParamMethodRestrictionRestriction paramMethodRestrictionRestriction) {
        return new ParamListBuilder<C>(parentClass, paramMethodRestrictionRestriction);
    }

    /**
     * Makes sure an object is not null.<br>
     *
     * If it is, an IllegalArgumentException is thrown with a message using the rest of the parameters.
     * The message looks something like <code>"Argument 1 (paramName) provided to methodName cannot be null."</code>
     * @param obj  the object that needs to not be null
     * @param position  the position of the argument in the call to the method in question
     * @param paramName  the name of the parameter in the method call
     * @param methodName  the name of the method
     * @throws IllegalArgumentException if obj == null.
     */
    protected static void requireNonNull(final Object obj, final int position, final String paramName, final String methodName) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument " + String.valueOf(position) + " (" + paramName + ") " +
                                               "provided to " + methodName + " cannot be null.");
        }
    }
}
