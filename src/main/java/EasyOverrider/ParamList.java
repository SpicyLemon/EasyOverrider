package EasyOverrider;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Describes a list of parameters that describe the parameters in a class.<br>
 *
 * These objects can then be used to easily override the toString, hashCode, and equals methods with standard behaviors.
 *
 * <pre>
 * {@code
 *
 * private static final ParamList<Foo> paramList =
 *                 ParamList.forClass(Foo.class)
 *                          .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
 *                          .withParam("bar", Foo::getBar, String.class)
 *                          .withParam("baz", Foo::getBaz, Baz.class)
 *                          .withCollection("moreFoo", Foo::getMoreFoo, INCLUDED_IN_TOSTRING_ONLY, List.class, Foo.class)
 *                          .andThatsIt();
 * }
 * </pre>
 *
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
 *     return paramList.hashCode(this);
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
    final private Map<String, ParamDescription<? super O, ?>> paramDescriptionMap;
    final private List<String> paramOrder;
    private EasyOverriderService easyOverriderService;

    private static ParamList<ParamList> paramList;

    static ParamList<ParamList> getParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(ParamList.class)
                                 .withParam("parentClass", ParamList::getParentClass, Class.class)
                                 .withMap("paramDescriptionMap", ParamList::getParamDescriptionMap, Map.class,
                                          String.class, ParamDescription.class)
                                 .withCollection("paramOrder", ParamList::getParamOrder, List.class, String.class)
                                 .withParam("easyOverriderService", (pl) -> pl.easyOverriderService, EasyOverriderService.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Kicks off a ParamListBuilder for the provided class.<br>
     *
     * @param parentClass  the class you're building the parameter list for
     * @param <C>  the class you're building the parameter list for
     * @return A {@link ParamListBuilder} for the specified class.
     */
    public static <C> ParamListBuilder<C> forClass(final Class<C> parentClass) {
        return ParamListBuilder.forClass(parentClass);
    }

    /**
     * Constructor for a ParamList.<br>
     *
     * This is usually done using a {@link ParamListBuilder}.
     * Start with the {@link ParamList#forClass(Class)} method,
     * add in parameters using methods like {@link ParamListBuilder#withParam(String, Function, Class)},
     * and finished off with the {@link ParamListBuilder#andThatsIt()} method to create a new ParamList.<br>
     *
     * Uses the {@link EasyOverriderService#validateParamListConstructorOrThrow(Class, Map, List, EasyOverriderService)} method.
     *
     * @param parentClass  the class of the object these parameters represent
     * @param paramDescriptionMap  a map of name to ParamDescription objects describing the parameters in the parent object
     * @param paramOrder  the order that the parameters should be in
     * @param easyOverriderService  the easyOverriderService to use for the key pieces of functionality - cannot be null
     * @throws IllegalArgumentException if the easyOverriderService is null.
     */
    ParamList(final Class<O> parentClass, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap,
              final List<String> paramOrder, final EasyOverriderService easyOverriderService) {
         this.easyOverriderService = Optional.ofNullable(easyOverriderService).orElseGet(EasyOverriderServiceImpl::new);
         this.easyOverriderService.validateParamListConstructorOrThrow(parentClass, paramDescriptionMap, paramOrder, easyOverriderService);
         this.parentClass = parentClass;
         this.paramDescriptionMap = new HashMap<>(paramDescriptionMap);
         this.paramOrder = new LinkedList<>(paramOrder);
    }

    /**
     * Getter for the parentClass parameter that records the class this ParamList applies to.<br>
     *
     * @return A Class.
     */
    public Class<O> getParentClass() {
        return parentClass;
    }

    /**
     * Getter for the paramDescriptionMap parameter.
     *
     * @return An unmodifiable Map of Strings to ParamDescription values.<br>
     */
    public Map<String, ParamDescription<? super O, ?>> getParamDescriptionMap() {
        return Collections.unmodifiableMap(paramDescriptionMap);
    }

    /**
     * Getter for the order parameter.<br>
     *
     * @return An unmodifiable list of Strings
     */
    public List<String> getParamOrder() {
        return Collections.unmodifiableList(paramOrder);
    }

    /**
     * Gets the list of all param descriptions in an unmodifiable state.<br>
     *
     * Uses the {@link EasyOverriderService#getAllParamDescriptions(List, Map)} method.
     *
     * @return A list of ParamDescription objects.
     */
    public List<ParamDescription<? super O, ?>> getAllParamDescriptions() {
        return easyOverriderService.getAllParamDescriptions(paramOrder, paramDescriptionMap);
    }

    /**
     * Gets the list of all param descriptions that are to be used in an equals() method.<br>
     *
     * Uses the {@link EasyOverriderService#getEqualsParamDescriptions(List, Map)} method.
     *
     * @return A list of ParamDescription objects.
     */
    public List<ParamDescription<? super O, ?>> getEqualsParamDescriptions() {
        return easyOverriderService.getEqualsParamDescriptions(paramOrder, paramDescriptionMap);
    }

    /**
     * Gets the list of all param descriptions that are to be used in a hashCode() method.<br>
     *
     * Uses the {@link EasyOverriderService#getHashCodeParamDescriptions(List, Map)} method.
     *
     * @return A list of ParamDescription objects.
     */
    public List<ParamDescription<? super O, ?>> getHashCodeParamDescriptions() {
        return easyOverriderService.getHashCodeParamDescriptions(paramOrder, paramDescriptionMap);
    }

    /**
     * Gets the list of all param descriptions that are to be used in a toString() method.<br>
     *
     * Uses the {@link EasyOverriderService#getToStringParamDescriptions(List, Map)} method.
     *
     * @return A list of ParamDescription objects.
     */
    public List<ParamDescription<? super O, ?>> getToStringParamDescriptions() {
        return easyOverriderService.getToStringParamDescriptions(paramOrder, paramDescriptionMap);
    }

    /**
     * Checks to see if the provided objects are equal as described by this paramList.<br>
     *
     * Uses the {@link EasyOverriderService#equals(Object, Object, Class, List, Map)} method.
     *
     * @param thisObj  the main object you're checking against
     * @param thatObj  the other object you're wanting to test
     * @return True if both objects are equal. False otherwise.
     */
    public boolean equals(final Object thisObj, final Object thatObj) {
        return easyOverriderService.equals(thisObj, thatObj, parentClass, paramOrder, paramDescriptionMap);
    }

    /**
     * Generates the hashCode for the provided object using the appropriate parameters.<br>
     *
     * Uses the {@link EasyOverriderService#hashCode(Object, List, Map)} method.
     *
     * @param thisObj  the object to get the parameter values from
     * @return An integer.
     */
    public int hashCode(final O thisObj) {
        return easyOverriderService.hashCode(thisObj, paramOrder, paramDescriptionMap);
    }

    /**
     * Gets a String representation of the provided object using the appropriate parameters.<br>
     *
     * Uses the {@link EasyOverriderService#toString(Object, Map, Class, List, Map)} method.
     *
     * @param thisObj  the object to get the parameter values from
     * @return A string.
     */
    public String toString(final O thisObj) {
        return easyOverriderService.toString(thisObj, null, parentClass, paramOrder, paramDescriptionMap);
    }

    /**
     * Gets a String representation of the provided object using the appropriate parameters and preventing recursion if needed.<br>
     *
     * Uses the {@link EasyOverriderService#toString(Object, Map, Class, List, Map)} method.
     *
     * @param thisObj  the object to get the parameter values from
     * @param seen  the map of classes to sets of integers containing hashCodes of things that have been seen so far.
     * @return A string representation of the given object.
     */
    public String toString(final O thisObj, final Map<Class, Set<Integer>> seen) {
        return easyOverriderService.toString(thisObj, seen, parentClass, paramOrder, paramDescriptionMap);
    }

    /**
     * Gets a String representation of the desired parameters in the provided object.<br>
     *
     * Uses the {@link EasyOverriderService#getParamsString(Object, Map, List, Map)} method.
     *
     * @param thisObj  the object to get the parameter values from
     * @param seen  the map of classes to sets of integers containing hashCodes of things that have been seen so far.
     * @return A String of comma-space delimited name/value Strings.
     */
    public String getParamsString(final O thisObj, final Map<Class, Set<Integer>> seen) {
        return easyOverriderService.getParamsString(thisObj, seen, paramOrder, paramDescriptionMap);
    }

    /**
     * Creates a new ParamListBuilder based on this ParamList.<br>
     *
     * This allows you to extend an already created ParamList when, for example, extending a class, and adding new parameters.<br>
     *
     * @param newParentClass  the new class that extends the class that this ParamList is for
     * @param <C>  the type of the new class
     * @return A new {@link ParamListBuilder}.
     */
    public <C extends O> ParamListBuilder<C> extendedBy(Class<C> newParentClass) {
        return new ParamListBuilder<C>(newParentClass, this, easyOverriderService);
    }

    /**
     * equals method for a ParamList object.<br>
     *
     * @param obj  the object to test against
     * @return True if this ParamList equals the provided object. False otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        return getParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamList object.<br>
     *
     * @return An int.
     */
    @Override
    public int hashCode() {
        return getParamList().hashCode(this);
    }

    /**
     * toString method for a ParamList object.<br>
     *
     * @return A String.
     */
    @Override
    public String toString() {
        return getParamList().toString(this);
    }
}
