package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.requireNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * @param parentClass  the class of the object these parameters represent
     * @param paramDescriptionMap  a map of name to ParamDescription objects describing the parameters in the parent object
     * @param paramOrder  the order that the parameters should be in
     * @param easyOverriderService  the easyOverriderService to use for the key pieces of functionality - cannot be null
     * @throws IllegalArgumentException if the easyOverriderService is null.
     */
    ParamList(final Class<O> parentClass, final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap,
              final List<String> paramOrder, final EasyOverriderService easyOverriderService) {
        requireNonNull(parentClass, 1, "parentClass", "ParamList constructor");
        requireNonNull(paramDescriptionMap, 2, "paramDescriptionMap", "ParamList constructor");
        requireNonNull(paramOrder, 3, "paramOrder", "ParamList constructor");
        requireNonNull(easyOverriderService, 4, "easyOverriderService", "ParamList constructor");
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
        this.paramOrder = new LinkedList<>(paramOrder);
        this.easyOverriderService = easyOverriderService;
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
     * Uses the {@link EasyOverriderService#hashCode(Object, ParamList)} method.
     *
     * @param thisObj  the object to get the parameter values from
     * @return An integer.
     */
    public int hashCode(final O thisObj) {
        return easyOverriderService.hashCode(thisObj, this);
    }

    /**
     * Gets a String representation of the provided object using the appropriate parameters.<br>
     *
     * Uses the {@link EasyOverriderService#toString(Object, ParamList, Map)} method.
     *
     * @param thisObj  the object to get the parameter values from
     * @return A string.
     */
    public String toString(final O thisObj) {
        return easyOverriderService.toString(thisObj, this, null);
    }

    /**
     * Gets a String representation of the provided object using the appropriate parameters and preventing recursion if needed.<br>
     *
     * Uses the {@link EasyOverriderService#toString(Object, ParamList, Map)} method.
     *
     * @param thisObj  the object to get the parameter values from
     * @param seen  the map of classes to sets of integers containing hashCodes of things that have been seen so far.
     * @return A string representation of the given object.
     */
    public String toString(final O thisObj, final Map<Class, Set<Integer>> seen) {
        return easyOverriderService.toString(thisObj, this, seen);
    }

    /**
     * Gets a String representation of the provided object using only the primary parameters.<br>
     *
     * Uses the {@link EasyOverriderService#primaryToString(Object, ParamList)} method.
     *
     * @param thisObj  the object to get the parameter values from
     * @return A short String representation of the given object.
     */
    public String primaryToString(final O thisObj) {
        return easyOverriderService.primaryToString(thisObj, this);
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
    public <C extends O> ParamListBuilder<C> extendedBy(final Class<C> newParentClass) {
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
