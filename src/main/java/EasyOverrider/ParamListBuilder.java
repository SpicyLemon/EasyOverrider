package EasyOverrider;

import static models.easyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static models.easyOverrider.ParamMethodRestrictionRestriction.SAFE_ONLY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Class to help with building a {@link ParamList}.
 * @param <O> The type of the param list you're creating.
 */
public class ParamListBuilder<O> {

    private final Class<O> parentClass;
    private final ParamMethodRestrictionRestriction paramMethodRestrictionRestriction;
    private final List<String> paramOrder;
    private final Map<String, ParamDescription<? super O, ?, ?>> paramDescriptionMap;

    private static ParamList<ParamListBuilder> paramList;

    static ParamList<ParamListBuilder> getParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(ParamListBuilder.class)
                                 .withParam("parentClass", ParamListBuilder::getParentClass, Class.class)
                                 .withParam("paramMethodRestrictionRestriction", ParamListBuilder::getParamMethodRestrictionRestriction,
                                            ParamMethodRestrictionRestriction.class)
                                 .withCollection("paramOrder", ParamListBuilder::getParamOrder, List.class, String.class)
                                 .withMap("paramDescriptionMap", ParamListBuilder::getParamDescriptionMap,
                                          Map.class, String.class, ParamDescription.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Private constructor to do all the meat of the setting of stuff, without any validation.
     * This is so that the setting can all be done in one place, but we can have validation on the different actual constructors.
     * @param superParamList Any existing ParamList available to the parentClass.
     * @param parentClass The class of the object being described.
     * @param paramMethodRestrictionRestriction The {@link ParamMethodRestrictionRestriction} to use.
     *                                          If null, {@link ParamMethodRestrictionRestriction#SAFE_ONLY} is used.
     */
    private ParamListBuilder(final ParamList<? super O> superParamList, final Class<O> parentClass,
                             final ParamMethodRestrictionRestriction paramMethodRestrictionRestriction) {
        this.parentClass = parentClass;
        this.paramMethodRestrictionRestriction = Optional.ofNullable(paramMethodRestrictionRestriction).orElse(SAFE_ONLY);
        this.paramOrder = Optional.ofNullable(superParamList)
                                  .map(ParamList::getParamOrder)
                                  .map(ArrayList::new)
                                  .orElseGet(ArrayList::new);
        this.paramDescriptionMap = Optional.ofNullable(superParamList)
                                           .map(ParamList::getParamDescriptionMap)
                                           .map(HashMap<String, ParamDescription<? super O, ?, ?>>::new)
                                           .orElseGet(HashMap::new);
    }

    /**
     * Default constructor to start. Usually this is done using {@link ParamList#forClass(Class)}
     * so that you don't have to import ParamListBuilder.
     * It uses a default {@link ParamMethodRestrictionRestriction} of {@link ParamMethodRestrictionRestriction#SAFE_ONLY}
     * @param parentClass The class of the object being described.
     * @see ParamList#forClass(Class)
     */
    ParamListBuilder(final Class<O> parentClass) {
        this(null, parentClass, null);
        ParamList.requireNonNull(parentClass, 1, "parentClass", "ParamListBuilder Constructor");
    }

    /**
     * Default constructor to start. Usually this is done using {@link ParamList#forClass(Class, ParamMethodRestrictionRestriction)}
     * so that you don't have to import ParamListBuilder.
     * It allows you to specify the {@link ParamMethodRestrictionRestriction} if needed.
     * @param parentClass The class of the object being described.
     * @param paramMethodRestrictionRestriction The {@link ParamMethodRestrictionRestriction} to use.
     * @see ParamList#forClass(Class)
     */
    ParamListBuilder(final Class<O> parentClass, final ParamMethodRestrictionRestriction paramMethodRestrictionRestriction) {
        this(null, parentClass, paramMethodRestrictionRestriction);
        ParamList.requireNonNull(parentClass, 1, "parentClass", "ParamListBuilder Constructor");
        ParamList.requireNonNull(paramMethodRestrictionRestriction, 2, "paramMethodRestrictionRestriction", "ParamListBuilder Constructor");
    }

    /**
     * Constructor for basing a new list off of an existing one.
     * This is usually done using {@link ParamList#extendedBy(Class)}.
     * It uses a default {@link ParamMethodRestrictionRestriction} of {@link ParamMethodRestrictionRestriction#SAFE_ONLY}
     * @param parentClass The class of the object being described.
     * @param superParamList The existing ParamList available to the parentClass.
     * @see ParamList#extendedBy(Class)
     */
    ParamListBuilder(final Class<O> parentClass, final ParamList<? super O> superParamList) {
        this(superParamList, parentClass, null);
        ParamList.requireNonNull(parentClass, 1, "parentClass", "ParamListBuilder Constructor");
        ParamList.requireNonNull(superParamList, 2, "superParamList", "ParamListBuilder Constructor");
    }

    /**
     * Constructor for basing a new list off of an existing one.
     * This is usually done using {@link ParamList#extendedBy(Class, ParamMethodRestrictionRestriction)}
     * @param parentClass The class of the object being described.
     * @param superParamList The existing ParamList available to the parentClass.
     * @param paramMethodRestrictionRestriction The {@link ParamMethodRestrictionRestriction} to use.
     * @see ParamList#extendedBy(Class)
     */
    ParamListBuilder(final Class<O> parentClass, final ParamList<? super O> superParamList,
                     final ParamMethodRestrictionRestriction paramMethodRestrictionRestriction) {
        this(superParamList, parentClass, paramMethodRestrictionRestriction);
        ParamList.requireNonNull(parentClass, 1, "parentClass", "ParamListBuilder Constructor");
        ParamList.requireNonNull(superParamList, 2, "superParamList", "ParamListBuilder Constructor");
        ParamList.requireNonNull(paramMethodRestrictionRestriction, 3, "paramMethodRestrictionRestriction", "ParamListBuilder Constructor");
    }

    /**
     * Getter for the parentClass parameter.
     * @return A Class.
     */
    public Class<O> getParentClass() {
        return parentClass;
    }

    /**
     * Getter for the paramMethodRestrictionRestriction parameter.
     * @return A {@link ParamMethodRestrictionRestriction} value.
     */
    public ParamMethodRestrictionRestriction getParamMethodRestrictionRestriction() {
        return paramMethodRestrictionRestriction;
    }

    /**
     * Getter for the ParamOrder parameter.
     * @return A list of name strings that dictate the parameter order.
     */
    public List<String> getParamOrder() {
        return Collections.unmodifiableList(paramOrder);
    }

    /**
     * Getter for the map of parameter names to their descriptions.
     * @return A map of name strings to ParamDescription objects.
     */
    public Map<String, ParamDescription<? super O, ?, ?>> getParamDescriptionMap() {
        return Collections.unmodifiableMap(paramDescriptionMap);
    }

    /**
     * Add a new ParamDescription to the list with the provided parameters.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramClass The class of the parameter in question.
     * @param <P> The type of the parameter being described.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withParam(String, Function, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withParam(String, Function, BiFunction, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withParam(final String name, final Function<? super O, P> getter,
                                             final Class<P> paramClass) {
        ParamList.requireNonNull(name, 1, "name", "withParam");
        ParamList.requireNonNull(getter, 2, "getter", "withParam");
        ParamList.requireNonNull(paramClass, 3, "paramClass", "withParam");
        addSingleParam(paramClass, name, getter, INCLUDED_IN_ALL, null);
        return this;
    }

    /**
     * Add a new ParamDescription to the list with the provided parameters.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param <P> The type of the parameter being described.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withParam(String, Function, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withParam(String, Function, BiFunction, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withParam(final String name, final Function<? super O, P> getter,
                                             final ParamMethodRestriction paramMethodRestriction,
                                             final Class<P> paramClass) {
        ParamList.requireNonNull(name, 1, "name", "withParam");
        ParamList.requireNonNull(getter, 2, "getter", "withParam");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withParam");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withParam");
        addSingleParam(paramClass, name, getter, paramMethodRestriction, null);
        return this;
    }

    /**
     * Add a new ParamDescription to the list with the provided parameters.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question.  Cannot be null.
     * @param <P> The type of the parameter being described.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withParam(String, Function, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withParam(String, Function, BiFunction, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withCollection(String, Function, BiFunction, Class, Class)
     * @see #withMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, BiFunction, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withParam(final String name, final Function<? super O, P> getter,
                                             final BiFunction<? super P, Boolean, String> recursionPreventingToString,
                                             final Class<P> paramClass) {
        ParamList.requireNonNull(name, 1, "name", "withParam");
        ParamList.requireNonNull(getter, 2, "getter", "withParam");
        ParamList.requireNonNull(recursionPreventingToString, 3, "recursionPreventingToString", "withParam");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withParam");
        addSingleParam(paramClass, name, getter, INCLUDED_IN_ALL, recursionPreventingToString);
        return this;
    }

    /**
     * Add a new ParamDescription to the list with the provided parameters.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param <P> The type of the parameter being described.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withParam(String, Function, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withParam(String, Function, BiFunction, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withParam(final String name, final Function<? super O, P> getter,
                                             final ParamMethodRestriction paramMethodRestriction,
                                             final BiFunction<? super P, Boolean, String> recursionPreventingToString,
                                             final Class<P> paramClass) {
        ParamList.requireNonNull(name, 1, "name", "withParam");
        ParamList.requireNonNull(getter, 2, "getter", "withParam");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withParam");
        ParamList.requireNonNull(recursionPreventingToString, 4, "recursionPreventingToString", "withParam");
        ParamList.requireNonNull(paramClass, 5, "paramClass", "withParam");
        addSingleParam(paramClass, name, getter, paramMethodRestriction, recursionPreventingToString);
        return this;
    }

    /**
     * Create a new ParamDescriptionSingle and add it to be included in the ParamList.
     * @param paramClass The class of the parameter in question.
     * @param name The name of the parameter, e.g. "id".
     * @param getter The getter for the parameter, e.g. Product::getId.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    If null, it is assumed that no recursion prevention is needed.
     * @param <P> The type of the parameter.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     */
    private <P> void addSingleParam(final Class<P> paramClass, final String name, final Function<? super O, P> getter,
                                    final ParamMethodRestriction paramMethodRestriction,
                                    final BiFunction<? super P, Boolean, String> recursionPreventingToString) {
        addParam(new ParamDescriptionSingle<>(parentClass, paramClass, name, getter,
                                              paramMethodRestriction, recursionPreventingToString));
    }

    /**
     * Create a new ParamDescription for a collection and add it to be included in the ParamList.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param entryClass The class of the entries in the collection. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withCollection(String, Function, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withCollection(String, Function, BiFunction, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withParam(String, Function, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withoutCollection(String)
     */
    public <E, P extends Collection> ParamListBuilder<O> withCollection(final String name, Function<? super O, P> getter,
                                                                        final Class<P> paramClass, final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withCollection");
        ParamList.requireNonNull(getter, 2, "getter", "withCollection");
        ParamList.requireNonNull(paramClass, 3, "paramClass", "withCollection");
        ParamList.requireNonNull(entryClass, 4, "entryClass", "withCollection");
        addCollectionParam(paramClass, entryClass, name, getter, INCLUDED_IN_ALL, null);
        return this;
    }

    /**
     * Create a new ParamDescription for a collection and add it to be included in the ParamList.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param entryClass The class of the entries in the collection. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withCollection(String, Function, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withCollection(String, Function, BiFunction, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withoutCollection(String)
     */
    public <E, P extends Collection> ParamListBuilder<O> withCollection(final String name, final Function<? super O, P> getter,
                                                                        final ParamMethodRestriction paramMethodRestriction,
                                                                        final Class<P> paramClass, final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withCollection");
        ParamList.requireNonNull(getter, 2, "getter", "withCollection");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withCollection");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withCollection");
        ParamList.requireNonNull(entryClass, 5, "entryClass", "withCollection");
        addCollectionParam(paramClass, entryClass, name, getter, paramMethodRestriction, null);
        return this;
    }

    /**
     * Create a new ParamDescription for a collection and add it to be included in the ParamList.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param entryClass The class of the entries in the collection. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withCollection(String, Function, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withCollection(String, Function, BiFunction, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withParam(String, Function, BiFunction, Class)
     * @see #withMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withUpdatedCollection(String, Function, BiFunction, Class, Class)
     * @see #withoutCollection(String)
     */
    public <E, P extends Collection> ParamListBuilder<O> withCollection(final String name, final Function<? super O, P> getter,
                                                                        final BiFunction<? super E, Boolean, String> recursionPreventingToString,
                                                                        final Class<P> paramClass, final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withCollection");
        ParamList.requireNonNull(getter, 2, "getter", "withCollection");
        ParamList.requireNonNull(recursionPreventingToString, 3, "recursionPreventingToString", "withCollection");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withCollection");
        ParamList.requireNonNull(entryClass, 5, "entryClass", "withCollection");
        addCollectionParam(paramClass, entryClass, name, getter, INCLUDED_IN_ALL, recursionPreventingToString);
        return this;
    }

    /**
     * Create a new ParamDescription for a collection and add it to be included in the ParamList.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param entryClass The class of the entries in the collection. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withCollection(String, Function, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withCollection(String, Function, BiFunction, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withoutCollection(String)
     */
    public <E, P extends Collection> ParamListBuilder<O> withCollection(final String name, final Function<? super O, P> getter,
                                                                        final ParamMethodRestriction paramMethodRestriction,
                                                                        final BiFunction<? super E, Boolean, String> recursionPreventingToString,
                                                                        final Class<P> paramClass, final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withCollection");
        ParamList.requireNonNull(getter, 2, "getter", "withCollection");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withCollection");
        ParamList.requireNonNull(recursionPreventingToString, 4, "recursionPreventingToString", "withCollection");
        ParamList.requireNonNull(paramClass, 5, "paramClass", "withCollection");
        ParamList.requireNonNull(entryClass, 6, "entryClass", "withCollection");
        addCollectionParam(paramClass, entryClass, name, getter, paramMethodRestriction, recursionPreventingToString);
        return this;
    }

    /**
     * Create a new ParamDescriptionCollection and add it to be included in the ParamList.
     * @param paramClass The class of the parameter in question.
     * @param entryClass The class of the entries in the collection.
     * @param name The name of the parameter, e.g. "id".
     * @param getter The getter for the parameter, e.g. Product::getId.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    If null, it is assumed that no recursion prevention is needed.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     */
    @SuppressWarnings("unchecked")
    private <E, P extends Collection> void addCollectionParam(final Class<P> paramClass, final Class<E> entryClass,
                                                              final String name, final Function<? super O, P> getter,
                                                              final ParamMethodRestriction paramMethodRestriction,
                                                              final BiFunction<? super E, Boolean, String> recursionPreventingToString) {
        addParam(new ParamDescriptionCollection<>(parentClass, paramClass, entryClass, name, getter,
                                                  paramMethodRestriction, recursionPreventingToString));
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param keyClass The class of the map's keys. Cannot be null.
     * @param entryClass The class of the entries in the collection. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the map.
     * @param <E> The type of the entries in the map.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withParam(String, Function, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withoutMap(String)
     */
    public <K, E, P extends Map> ParamListBuilder<O> withMap(final String name, final Function<? super O, P> getter,
                                                             final Class<P> paramClass, final Class<K> keyClass,
                                                             final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withMap");
        ParamList.requireNonNull(getter, 2, "getter", "withMap");
        ParamList.requireNonNull(paramClass, 3, "paramClass", "withMap");
        ParamList.requireNonNull(keyClass, 4, "keyClass", "withMap");
        ParamList.requireNonNull(entryClass, 5, "entryClass", "withMap");
        addMapParam(paramClass, keyClass, entryClass, name, getter, INCLUDED_IN_ALL, null);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param keyClass The class of the map's keys. Cannot be null.
     * @param entryClass The class of the entries in the collection. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the map.
     * @param <E> The type of the entries in the map.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withoutMap(String)
     */
    public <K, E, P extends Map> ParamListBuilder<O> withMap(final String name, final Function<? super O, P> getter,
                                                             final ParamMethodRestriction paramMethodRestriction,
                                                             final Class<P> paramClass, final Class<K> keyClass,
                                                             final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withMap");
        ParamList.requireNonNull(getter, 2, "getter", "withMap");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withMap");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withMap");
        ParamList.requireNonNull(keyClass, 5, "keyClass", "withMap");
        ParamList.requireNonNull(entryClass, 6, "entryClass", "withMap");
        addMapParam(paramClass, keyClass, entryClass, name, getter, paramMethodRestriction, null);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param keyClass The class of the map's keys. Cannot be null.
     * @param entryClass The class of the entries in the collection. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the map.
     * @param <E> The type of the entries in the map.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withParam(String, Function, BiFunction, Class)
     * @see #withCollection(String, Function, BiFunction, Class, Class)
     * @see #withUpdatedMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withoutMap(String)
     */
    public <K, E, P extends Map> ParamListBuilder<O> withMap(final String name, final Function<? super O, P> getter,
                                                             final BiFunction<? super E, Boolean, String> recursionPreventingToString,
                                                             final Class<P> paramClass, final Class<K> keyClass,
                                                             final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withMap");
        ParamList.requireNonNull(getter, 2, "getter", "withMap");
        ParamList.requireNonNull(recursionPreventingToString, 3, "recursionPreventingToString", "withMap");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withMap");
        ParamList.requireNonNull(keyClass, 5, "keyClass", "withMap");
        ParamList.requireNonNull(entryClass, 6, "entryClass", "withMap");
        addMapParam(paramClass, keyClass, entryClass, name, getter, INCLUDED_IN_ALL, recursionPreventingToString);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param keyClass The class of the map's keys. Cannot be null.
     * @param entryClass The class of the entries in the collection. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the map.
     * @param <E> The type of the entries in the map.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withoutMap(String)
     */
    public <K, E, P extends Map> ParamListBuilder<O> withMap(final String name, final Function<? super O, P> getter,
                                                             final ParamMethodRestriction paramMethodRestriction,
                                                             final BiFunction<? super E, Boolean, String> recursionPreventingToString,
                                                             final Class<P> paramClass, final Class<K> keyClass,
                                                             final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withMap");
        ParamList.requireNonNull(getter, 2, "getter", "withMap");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withMap");
        ParamList.requireNonNull(recursionPreventingToString, 4, "recursionPreventingToString", "withMap");
        ParamList.requireNonNull(paramClass, 5, "paramClass", "withMap");
        ParamList.requireNonNull(keyClass, 6, "keyClass", "withMap");
        ParamList.requireNonNull(entryClass, 7, "entryClass", "withMap");
        addMapParam(paramClass, keyClass, entryClass, name, getter, paramMethodRestriction, recursionPreventingToString);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.
     * @param paramClass The class of the parameter in question.
     * @param keyClass The class of the keys in the map.
     * @param entryClass The class of the entries in the collection.
     * @param name The name of the parameter, e.g. "id".
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    If null, it is assumed that no recursion prevention is needed.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the map.
     * @param <E> The type of the entries in the map.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     */
    @SuppressWarnings("unchecked")
    private <K, E, P extends Map> void addMapParam(final Class<P> paramClass, final Class<K> keyClass,
                                                   final Class<E> entryClass, final String name,
                                                   final Function<? super O, P> getter,
                                                   final ParamMethodRestriction paramMethodRestriction,
                                                   final BiFunction<? super E, Boolean, String> recursionPreventingToString) {
        addParam(new ParamDescriptionMap<>(parentClass, paramClass, keyClass, entryClass, name, getter,
                                           paramMethodRestriction, recursionPreventingToString));
    }

    /**
     * Adds a ParamDescription to what we've got.
     * @param paramDescription The ParamDescription to add.
     * @throws IllegalArgumentException if the name of the provided ParamDescription has already been provided.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     */
    private void addParam(final ParamDescription<? super O, ?, ?> paramDescription) {
        enforceParamMethodRestrictionRestriction(paramDescription.getParamMethodRestriction());
        String name = paramDescription.getName();
        if (paramDescriptionMap.containsKey(name)) {
            throw new IllegalArgumentException("A parameter named '" + name + "' already exists while trying to " +
                                               "build the ParamList for a " + parentClass.getName());
        }
        paramOrder.add(name);
        paramDescriptionMap.put(name, paramDescription);
    }

    /**
     * Updates the parameter having the provided name with the new values given.
     * Specifically, a new ParamDescription is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param <P> The type of the parameter being described.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withUpdatedParam(String, Function, BiFunction, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withParam(String, Function, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedParam(final String name, final Function<? super O, P> getter,
                                                    final Class<P> paramClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedParam");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedParam");
        ParamList.requireNonNull(paramClass, 3, "paramClass", "withUpdatedParam");
        updateSingleParam(paramClass, name, getter, INCLUDED_IN_ALL, null);
        return this;
    }

    /**
     * Updates the parameter having the provided name with the new values given.
     * Specifically, a new ParamDescription is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param <P> The type of the parameter being described.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withUpdatedParam(String, Function, BiFunction, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedParam(final String name, final Function<? super O, P> getter,
                                                    final ParamMethodRestriction paramMethodRestriction,
                                                    final Class<P> paramClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedParam");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedParam");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withUpdatedParam");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withUpdatedParam");
        updateSingleParam(paramClass, name, getter, paramMethodRestriction, null);
        return this;
    }

    /**
     * Updates the parameter having the provided name with the new values given.
     * Specifically, a new ParamDescription is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param <P> The type of the parameter being described.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withUpdatedParam(String, Function, BiFunction, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withUpdatedCollection(String, Function, BiFunction, Class, Class)
     * @see #withUpdatedMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withParam(String, Function, BiFunction, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedParam(final String name, final Function<? super O, P> getter,
                                                    final BiFunction<? super P, Boolean, String> recursionPreventingToString,
                                                    final Class<P> paramClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedParam");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedParam");
        ParamList.requireNonNull(recursionPreventingToString, 3, "recursionPreventingToString", "withUpdatedParam");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withUpdatedParam");
        updateSingleParam(paramClass, name, getter, INCLUDED_IN_ALL, recursionPreventingToString);
        return this;
    }

    /**
     * Updates the parameter having the provided name with the new values given.
     * Specifically, a new ParamDescription is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param <P> The type of the parameter being described.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withUpdatedParam(String, Function, BiFunction, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedParam(final String name, final Function<? super O, P> getter,
                                                    final ParamMethodRestriction paramMethodRestriction,
                                                    final BiFunction<? super P, Boolean, String> recursionPreventingToString,
                                                    final Class<P> paramClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedParam");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedParam");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withUpdatedParam");
        ParamList.requireNonNull(recursionPreventingToString, 4, "recursionPreventingToString", "withUpdatedParam");
        ParamList.requireNonNull(paramClass, 5, "paramClass", "withUpdatedParam");
        updateSingleParam(paramClass, name, getter, paramMethodRestriction, recursionPreventingToString);
        return this;
    }

    /**
     * Create a new ParamDescriptionSingle and replace the existing ParamDescription with the same name.
     * @param paramClass The class of the parameter in question.
     * @param name The name of the parameter, e.g. "id".
     * @param getter The getter for the parameter, e.g. Product::getId.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    If null, it is assumed that no recursion prevention is needed.
     * @param <P> The type of the parameter.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     */
    private <P> void updateSingleParam(final Class<P> paramClass, final String name, final Function<? super O, P> getter,
                                       final ParamMethodRestriction paramMethodRestriction,
                                       final BiFunction<? super P, Boolean, String> recursionPreventingToString) {
        updateParam(new ParamDescriptionSingle<>(parentClass, paramClass, name, getter,
                                                 paramMethodRestriction, recursionPreventingToString));
    }

    /**
     * Updates the parameter having the provided name to be a collection with the new values given.
     * Specifically, a new ParamDescription for a collection is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param entryClass The class of the entries in the parameter. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withUpdatedCollection(String, Function, BiFunction, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withoutCollection(String)
     */
    public <E, P extends Collection> ParamListBuilder<O> withUpdatedCollection(final String name, final Function<? super O, P> getter,
                                                                               final Class<P> paramClass, final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedCollection");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedCollection");
        ParamList.requireNonNull(paramClass, 3, "paramClass", "withUpdatedCollection");
        ParamList.requireNonNull(entryClass, 4, "entryClass", "withUpdatedCollection");
        updateCollectionParam(paramClass, entryClass, name, getter, INCLUDED_IN_ALL, null);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a collection with the new values given.
     * Specifically, a new ParamDescription for a collection is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param entryClass The class of the entries in the parameter. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withUpdatedCollection(String, Function, BiFunction, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withoutCollection(String)
     */
    public <E, P extends Collection> ParamListBuilder<O> withUpdatedCollection(final String name, final Function<? super O, P> getter,
                                                                               final ParamMethodRestriction paramMethodRestriction,
                                                                               final Class<P> paramClass, final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedCollection");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedCollection");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withUpdatedCollection");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withUpdatedCollection");
        ParamList.requireNonNull(entryClass, 5, "entryClass", "withUpdatedCollection");
        updateCollectionParam(paramClass, entryClass, name, getter, paramMethodRestriction, null);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a collection with the new values given.
     * Specifically, a new ParamDescription for a collection is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param entryClass The class of the entries in the parameter. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withUpdatedCollection(String, Function, BiFunction, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withUpdatedParam(String, Function, BiFunction, Class)
     * @see #withUpdatedMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withCollection(String, Function, BiFunction, Class, Class)
     * @see #withoutCollection(String)
     */
    public <E, P extends Collection> ParamListBuilder<O> withUpdatedCollection(final String name, final Function<? super O, P> getter,
                                                                               final BiFunction<? super E, Boolean, String> recursionPreventingToString,
                                                                               final Class<P> paramClass, final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedCollection");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedCollection");
        ParamList.requireNonNull(recursionPreventingToString, 3, "recursionPreventingToString", "withUpdatedCollection");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withUpdatedCollection");
        ParamList.requireNonNull(entryClass, 5, "entryClass", "withUpdatedCollection");
        updateCollectionParam(paramClass, entryClass, name, getter, INCLUDED_IN_ALL, recursionPreventingToString);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a collection with the new values given.
     * Specifically, a new ParamDescription for a collection is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param entryClass The class of the entries in the parameter. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withUpdatedCollection(String, Function, BiFunction, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withoutCollection(String)
     */
    public <E, P extends Collection> ParamListBuilder<O> withUpdatedCollection(final String name, final Function<? super O, P> getter,
                                                                               final ParamMethodRestriction paramMethodRestriction,
                                                                               final BiFunction<? super E, Boolean, String> recursionPreventingToString,
                                                                               final Class<P> paramClass, final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedCollection");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedCollection");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withUpdatedCollection");
        ParamList.requireNonNull(recursionPreventingToString, 4, "recursionPreventingToString", "withUpdatedCollection");
        ParamList.requireNonNull(paramClass, 5, "paramClass", "withUpdatedCollection");
        ParamList.requireNonNull(entryClass, 6, "entryClass", "withUpdatedCollection");
        updateCollectionParam(paramClass, entryClass, name, getter, paramMethodRestriction, recursionPreventingToString);
        return this;
    }

    /**
     * Create a new ParamDescriptionCollection and replace the existing ParamDescription with the same name.
     * @param paramClass The class of the parameter in question.
     * @param entryClass The class of the entries in the parameter.
     * @param name The name of the parameter, e.g. "id".
     * @param getter The getter for the parameter, e.g. Product::getId.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    If null, it is assumed that no recursion prevention is needed.
     * @param <P> The type of the parameter.
     * @param <E> The type of the entries in the parameter.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     */
    @SuppressWarnings("unchecked")
    private <E, P extends Collection> void updateCollectionParam(final Class<P> paramClass, final Class<E> entryClass,
                                                                 final String name, final Function<? super O, P> getter,
                                                                 final ParamMethodRestriction paramMethodRestriction,
                                                                 final BiFunction<? super E, Boolean, String> recursionPreventingToString) {
        updateParam(new ParamDescriptionCollection<>(parentClass, paramClass, entryClass, name, getter,
                                                     paramMethodRestriction, recursionPreventingToString));
    }

    /**
     * Updates the parameter having the provided name to be a map with the new values given.
     * Specifically, a new ParamDescription for a map is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param keyClass The class of the keys in the parameter. Cannot be null.
     * @param entryClass The class of the entries in the parameter. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withoutMap(String)
     */
    private <K, E, P extends Map> ParamListBuilder<O> withUpdatedMap(final String name, final Function<? super O, P> getter,
                                                                     final Class<P> paramClass, final Class<K> keyClass,
                                                                     final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedMap");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedMap");
        ParamList.requireNonNull(paramClass, 3, "paramClass", "withUpdatedMap");
        ParamList.requireNonNull(keyClass, 4, "keyClass", "withUpdatedMap");
        ParamList.requireNonNull(entryClass, 5, "entryClass", "withUpdatedMap");
        updateMapParam(paramClass, keyClass, entryClass, name, getter, INCLUDED_IN_ALL, null);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a map with the new values given.
     * Specifically, a new ParamDescription for a map is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param keyClass The class of the keys in the parameter. Cannot be null.
     * @param entryClass The class of the entries in the parameter. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withoutMap(String)
     */
    private <K, E, P extends Map> ParamListBuilder<O> withUpdatedMap(final String name, final Function<? super O, P> getter,
                                                                     final ParamMethodRestriction paramMethodRestriction,
                                                                     final Class<P> paramClass, final Class<K> keyClass,
                                                                     final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedMap");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedMap");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withUpdatedMap");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withUpdatedMap");
        ParamList.requireNonNull(keyClass, 5, "keyClass", "withUpdatedMap");
        ParamList.requireNonNull(entryClass, 6, "entryClass", "withUpdatedMap");
        updateMapParam(paramClass, keyClass, entryClass, name, getter, paramMethodRestriction, null);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a map with the new values given.
     * Specifically, a new ParamDescription for a map is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param keyClass The class of the keys in the parameter. Cannot be null.
     * @param entryClass The class of the entries in the parameter. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, BiFunction, Class)
     * @see #withUpdatedCollection(String, Function, BiFunction, Class, Class)
     * @see #withMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withoutMap(String)
     */
    private <K, E, P extends Map> ParamListBuilder<O> withUpdatedMap(final String name, final Function<? super O, P> getter,
                                                                     final BiFunction<? super E, Boolean, String> recursionPreventingToString,
                                                                     final Class<P> paramClass, final Class<K> keyClass,
                                                                     final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedMap");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedMap");
        ParamList.requireNonNull(recursionPreventingToString, 3, "recursionPreventingToString", "withUpdatedMap");
        ParamList.requireNonNull(paramClass, 4, "paramClass", "withUpdatedMap");
        ParamList.requireNonNull(keyClass, 5, "keyClass", "withUpdatedMap");
        ParamList.requireNonNull(entryClass, 6, "entryClass", "withUpdatedMap");
        updateMapParam(paramClass, keyClass, entryClass, name, getter, INCLUDED_IN_ALL, recursionPreventingToString);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a map with the new values given.
     * Specifically, a new ParamDescription for a map is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     * @param name The name of the parameter, e.g. "id". Cannot be null.
     * @param getter The getter for the parameter, e.g. Product::getId. Cannot be null.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used. Cannot be null.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    Cannot be null.
     * @param paramClass The class of the parameter in question. Cannot be null.
     * @param keyClass The class of the keys in the parameter. Cannot be null.
     * @param entryClass The class of the entries in the parameter. Cannot be null.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the parameter.
     * @param <E> The type of the entries in the parameter.
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, BiFunction, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, BiFunction, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, BiFunction, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, BiFunction, Class, Class, Class)
     * @see #withoutMap(String)
     */
    private <K, E, P extends Map> ParamListBuilder<O> withUpdatedMap(final String name, final Function<? super O, P> getter,
                                                                     final ParamMethodRestriction paramMethodRestriction,
                                                                     final BiFunction<? super E, Boolean, String> recursionPreventingToString,
                                                                     final Class<P> paramClass, final Class<K> keyClass,
                                                                     final Class<E> entryClass) {
        ParamList.requireNonNull(name, 1, "name", "withUpdatedMap");
        ParamList.requireNonNull(getter, 2, "getter", "withUpdatedMap");
        ParamList.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withUpdatedMap");
        ParamList.requireNonNull(recursionPreventingToString, 4, "recursionPreventingToString", "withUpdatedMap");
        ParamList.requireNonNull(paramClass, 5, "paramClass", "withUpdatedMap");
        ParamList.requireNonNull(keyClass, 6, "keyClass", "withUpdatedMap");
        ParamList.requireNonNull(entryClass, 7, "entryClass", "withUpdatedMap");
        updateMapParam(paramClass, keyClass, entryClass, name, getter, paramMethodRestriction, recursionPreventingToString);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and replace the existing ParamDescription with the same name.
     * @param paramClass The class of the parameter in question.
     * @param keyClass The class of the keys in the parameter.
     * @param entryClass The class of the entries in the parameter.
     * @param name The name of the parameter, e.g. "id".
     * @param getter The getter for the parameter, e.g. Product::getId.
     * @param paramMethodRestriction A {@link ParamMethodRestriction} value indicating how this parameter should be used.
     * @param recursionPreventingToString A toString method that takes in a flag that can be used to prevent recursive toString functions.
     *                                    If null, it is assumed that no recursion prevention is needed.
     * @param <P> The type of the parameter.
     * @param <K> The type of the keys in the parameter.
     * @param <E> The type of the entries in the parameter.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     */
    @SuppressWarnings("unchecked")
    private <K, E, P extends Map> void updateMapParam(final Class<P> paramClass, final Class<K> keyClass, final Class<E> entryClass,
                                                            final String name, final Function<? super O, P> getter,
                                                            final ParamMethodRestriction paramMethodRestriction,
                                                            final BiFunction<? super E, Boolean, String> recursionPreventingToString) {
        updateParam(new ParamDescriptionMap<>(parentClass, paramClass, keyClass, entryClass, name, getter,
                                              paramMethodRestriction, recursionPreventingToString));
    }

    /**
     * Replaces the existing ParamDescription with the same name as the provided ParamDescription.
     * @param paramDescription The ParamDescription to use.
     * @throws IllegalArgumentException if the name of the provided ParamDescription has not already been defined.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     */
    private void updateParam(final ParamDescription<? super O, ?, ?> paramDescription) {
        enforceParamMethodRestrictionRestriction(paramDescription.getParamMethodRestriction());
        String name = paramDescription.getName();
        if (!paramDescriptionMap.containsKey(name)) {
            throw new IllegalArgumentException("No parameter named '" + name + "' exists to be updated while trying to " +
                                               "build the ParamList for a " + parentClass.getName());
        }
        paramDescriptionMap.replace(name, paramDescription);
    }

    /**
     * Makes sure that the provided {@link ParamMethodRestriction} is allowed using this builder's {@link ParamMethodRestrictionRestriction}.
     * @param paramMethodRestriction The {@link ParamMethodRestriction} to check.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow the provided {@link ParamMethodRestriction}.
     */
    private void enforceParamMethodRestrictionRestriction(final ParamMethodRestriction paramMethodRestriction) {
        if (!paramMethodRestrictionRestriction.allows(paramMethodRestriction)) {
            throw new IllegalArgumentException("The ParamMethodRestriction [" + paramMethodRestriction.name() + "] " +
                                               "is not allowed while building a ParamList for a " + parentClass.getName() + " " +
                                               "with a ParamMethodRestrictionRestriction of [" + paramMethodRestrictionRestriction.name() + "].");
        }
    }

    /**
     * Removes the parameter with the provided name.
     * @param name The name of the parameter to remove. Cannot be null.
     * @return the current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is null.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @see #withoutParam(String)
     * @see #withoutCollection(String)
     * @see #withoutMap(String)
     * @see #withParam(String, Function, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     */
    public ParamListBuilder<O> withoutParam(final String name) {
        ParamList.requireNonNull(name, 1, "name", "withoutParam");
        removeParam(name, null, "withoutParam");
        return this;
    }

    /**
     * Removes the parameter with the provided name.
     * @param name The name of the parameter to remove. Cannot be null.
     * @return the current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is null.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the parameter for the provided name is not a Collection.
     * @see #withoutParam(String)
     * @see #withoutCollection(String)
     * @see #withoutMap(String)
     * @see #withParam(String, Function, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     */
    public ParamListBuilder<O> withoutCollection(final String name) {
        ParamList.requireNonNull(name, 1, "name", "withoutCollection");
        removeParam(name, Collection.class, "withoutCollection");
        return this;
    }

    /**
     * Removes the parameter with the provided name.
     * @param name The name of the parameter to remove. Cannot be null.
     * @return the current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is null.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the parameter for the provided name is not a Map.
     * @see #withoutParam(String)
     * @see #withoutCollection(String)
     * @see #withoutMap(String)
     * @see #withParam(String, Function, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     */
    public ParamListBuilder<O> withoutMap(final String name) {
        ParamList.requireNonNull(name, 1, "name", "withoutMap");
        removeParam(name, Map.class, "withoutCollection");
        return this;
    }

    /**
     * Removes the parameter with the provide name.
     * @param name The name of the parameter to remove.
     * @param expectedParamClass The class you're expecting the parameter to be. If null, this part of the check is skipped.
     * @param callingMethod The name of the calling method (used in exceptions).
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if an expectedParamClass is provided, but the parameter with the provided name is not assignable to that class.
     */
    private void removeParam(String name, Class expectedParamClass, String callingMethod) {
        if (!paramDescriptionMap.containsKey(name)) {
            throw new IllegalArgumentException("No parameter named '" + name + "' exists to be removed while trying to " +
                                               "build the ParamList for a " + parentClass.getName());
        }
        if (expectedParamClass != null && !expectedParamClass.getClass().isAssignableFrom(paramDescriptionMap.get(name).getParamClass())) {
            throw new IllegalArgumentException("While building the ParamList for a " + parentClass.getName() + ", " +
                                               "the '" + name + "' parameter is not a " + expectedParamClass.getClass().getName() + ", " +
                                               "and cannot be removed using " + callingMethod + ".");
        }
        paramOrder.remove(name);
        paramDescriptionMap.remove(name);
    }

    /**
     * Finalizes the builder and returns the final ParamList.
     * @return a ParamList object.
     */
    public ParamList<O> andThatsIt() {
        return new ParamList<>(parentClass, paramDescriptionMap, paramOrder);
    }

    /**
     * equals method for a ParamListBuilder.
     * @param obj The object to test against.
     * @return True of this ParamListBuilder equals the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamListBuilder.
     * @return An int.
     */
    @Override
    public int hashCode() {
        return getParamList().hashCode(this);
    }

    /**
     * toString method for a ParamListBuilder.
     * @return A String.
     */
    @Override
    public String toString() {
        return getParamList().toString(this);
    }
}
