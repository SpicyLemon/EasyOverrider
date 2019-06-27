package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static EasyOverrider.ParamMethodRestrictionRestriction.ALLOW_UNSAFE;
import static EasyOverrider.ParamMethodRestrictionRestriction.SAFE_ONLY;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Class to help with building the building of a ParamList.<br>
 *
 * Since the {@link ParamList} objects are pretty complicated, this class helps create them in a nice, fluent way.
 *
 * <pre>
 * {@code
 *
 * private static ParamList<Foo> paramList =
 *                 ParamList.forClass(Foo.class)
 *                          .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
 *                          .withParam("name", Foo::getName, String.class)
 *                          .withParam("bar", Foo::getBar, Bar.class)
 *                          .andThatsIt();
 * }
 * </pre>
 *
 * @param <O>  the type of the param list you're creating
 */
public class ParamListBuilder<O> {

    final Class<O> parentClass;
    ParamMethodRestrictionRestriction paramMethodRestrictionRestriction;
    final List<String> paramOrder;
    final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap;
    EasyOverriderService easyOverriderService;
    EasyOverriderService defaultEasyOverriderService;

    static ParamList<ParamListBuilder> paramList;

    /**
     * Get the ParamList for a ParamListBuilder object.<br>
     *
     * This will create the ParamList if it's not already set.
     * The reason it does this rather than just having it statically set when the variable is created,
     * is that it uses itself. The class code needs to be loaded before it can be used.
     * @return The ParamList for a ParamListBuilder.
     */
    static ParamList<ParamListBuilder> getParamList() {
        if (paramList == null) {
            //This will create the ParamList if it's not already set.
            //The reason it does this rather than just having it statically set when the variable is created,
            // is that it uses itself. The class code needs to be loaded before it can be used.
            paramList = ParamList.forClass(ParamListBuilder.class)
                                 .withParam("parentClass", ParamListBuilder::getParentClass, Class.class)
                                 .withParam("paramMethodRestrictionRestriction",
                                            ParamListBuilder::getParamMethodRestrictionRestriction,
                                            ParamMethodRestrictionRestriction.class)
                                 .withCollection("paramOrder", ParamListBuilder::getParamOrder, List.class, String.class)
                                 .withMap("paramDescriptionMap", ParamListBuilder::getParamDescriptionMap,
                                          Map.class, String.class, ParamDescription.class)
                                 .withParam("easyOverriderService",
                                            (plb) -> plb.easyOverriderService,
                                            EasyOverriderService.class)
                                 .withParam("defaultEasyOverriderService",
                                            (plb) -> plb.defaultEasyOverriderService,
                                            EasyOverriderService.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Private constructor to do all the meat of the setting of stuff without any validation.<br>
     *
     * This is so that the setting can all be done in one place, but we can have validation on the different actual constructors.
     *
     * @param superParamList  any existing ParamList available to the parentClass
     * @param parentClass  the class of the object being described
     * @param paramMethodRestrictionRestriction  the {@link ParamMethodRestrictionRestriction} to use -
     *                                          if null, {@link ParamMethodRestrictionRestriction#SAFE_ONLY} is used
     */
    private ParamListBuilder(final EasyOverriderService easyOverriderService,
                             final ParamList<? super O> superParamList, final Class<O> parentClass,
                             final ParamMethodRestrictionRestriction paramMethodRestrictionRestriction) {
        this.easyOverriderService = easyOverriderService;
        this.parentClass = parentClass;
        this.paramMethodRestrictionRestriction = Optional.ofNullable(paramMethodRestrictionRestriction).orElse(SAFE_ONLY);
        this.paramOrder = Optional.ofNullable(superParamList)
                                  .map(ParamList::getParamOrder)
                                  .map(LinkedList::new)
                                  .orElseGet(LinkedList::new);
        this.paramDescriptionMap = Optional.ofNullable(superParamList)
                                           .map(ParamList::getParamDescriptionMap)
                                           .map(HashMap<String, ParamDescription<? super O, ?>>::new)
                                           .orElseGet(HashMap::new);
        defaultEasyOverriderService = new EasyOverriderServiceImpl();
    }

    /**
     * Default constructor to start.<br>
     *
     * Usually this is done using {@link ParamList#forClass(Class)}
     * so that you don't have to import ParamListBuilder.
     *
     * @param parentClass  the class of the object being described - cannot be null
     * @see ParamList#forClass(Class)
     */
    ParamListBuilder(final Class<O> parentClass) {
        this(null, null, parentClass, null);
        getEasyOverriderServiceOrDefault().requireNonNull(parentClass, 1, "parentClass", "ParamListBuilder Constructor");
    }

    /**
     * Constructor for basing a new list off of an existing one.<br>
     *
     * This is usually done using {@link ParamList#extendedBy(Class)}
     * so that you don't have to import ParamListBuilder.
     *
     * @param parentClass  the class of the object being described - cannot be null
     * @param superParamList  the existing {@link ParamList] available to the parentClass - cannot be null
     * @see ParamList#extendedBy(Class)
     */
    ParamListBuilder(final Class<O> parentClass, final ParamList<? super O> superParamList,
                     final EasyOverriderService easyOverriderService) {
        this(easyOverriderService, superParamList, parentClass, null);
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(parentClass, 1, "parentClass", "ParamListBuilder Constructor");
        eos.requireNonNull(superParamList, 2, "superParamList", "ParamListBuilder Constructor");
        eos.requireNonNull(easyOverriderService, 3, "easyOverriderService", "ParamListBuilder Constructor");
    }

    /**
     * Getter for the parentClass parameter.
     *
     * @return A Class.
     */
    public Class<O> getParentClass() {
        return parentClass;
    }

    /**
     * Getter for the paramMethodRestrictionRestriction parameter.
     *
     * @return A {@link ParamMethodRestrictionRestriction} value.
     */
    public ParamMethodRestrictionRestriction getParamMethodRestrictionRestriction() {
        return paramMethodRestrictionRestriction;
    }

    /**
     * Getter for the paramOrder parameter.
     *
     * @return A list of name strings that dictate the parameter order.
     */
    public List<String> getParamOrder() {
        return Collections.unmodifiableList(paramOrder);
    }

    /**
     * Getter for the map of parameter names to their descriptions.
     *
     * @return A map of name strings to ParamDescription objects.
     */
    public Map<String, ParamDescription<? super O, ?>> getParamDescriptionMap() {
        return Collections.unmodifiableMap(paramDescriptionMap);
    }

    /**
     * A getter for the EasyOverriderService that ensures it isn't null.<br>
     *
     * Gets <code>this.easyOverriderService</code> if not null, otherwise gets <code>this.defaultEasyOverriderService</code>.
     * @return the EasyOverriderService to use.
     */
    private EasyOverriderService getEasyOverriderServiceOrDefault() {
        return Optional.ofNullable(easyOverriderService).orElse(defaultEasyOverriderService);
    }

    /**
     * Set the EasyOverriderService to use.<br>
     *
     * If there are already <code>ParamDescription</code> objects in this builder, they are all updated to use this new service.
     *
     * @param easyOverriderService  the EasyOverriderService to use for the parameters and param list.
     * @return The current ParamListBuilder.
     */
    public ParamListBuilder<O> usingService(EasyOverriderService easyOverriderService) {
        getEasyOverriderServiceOrDefault().requireNonNull(easyOverriderService, 1, "easyOverriderService", "usingService");
        this.easyOverriderService = easyOverriderService;
        if (!paramDescriptionMap.isEmpty()) {
            paramDescriptionMap.values().forEach(e -> e.setService(easyOverriderService));
        }
        return this;
    }

    /**
     * Set the ParamMethodRestrictionRestriction value for the builder.<br>
     *
     * By default, a ParamListBuilder uses <code>ParamMethodRestrictionRestriction.SAFE_ONLY</code>.<br>
     *
     * @param paramMethodRestrictionRestriction  the ParamMethodRestrictionRestriction to use
     * @see #allowingOnlySafeParamMethodRestrictions()
     * @see #allowingUnsafeParamMethodRestrictions()
     * @return The current ParamListBuilder.
     */
    public ParamListBuilder<O> havingRestriction(ParamMethodRestrictionRestriction paramMethodRestrictionRestriction) {
        this.paramMethodRestrictionRestriction = paramMethodRestrictionRestriction;
        return this;
    }

    /**
     * Allow unsafe ParamMethodRestriction values.<br>
     *
     * This is the same as <code>havingRestriction(ParamMethodRestrictionRestriction.ALLOW_UNSAFE)</code>.
     *
     * @return The current ParamListBuilder.
     * @see #havingRestriction(ParamMethodRestrictionRestriction)
     * @see #allowingOnlySafeParamMethodRestrictions()
     */
    public ParamListBuilder<O> allowingUnsafeParamMethodRestrictions() {
        this.paramMethodRestrictionRestriction = ALLOW_UNSAFE;
        return this;
    }

    /**
     * Only allow safe ParamMethodRestriction values.<br>
     *
     * By default, a ParamListBuilder is in this state.<br>
     *
     * This is the same as <code>havingRestriction(ParamMethodRestrictionRestriction.SAFE_ONLY)</code>.
     *
     * @return The current ParamListBuilder.
     * @see #havingRestriction(ParamMethodRestrictionRestriction)
     * @see #allowingOnlySafeParamMethodRestrictions()
     */
    public ParamListBuilder<O> allowingOnlySafeParamMethodRestrictions() {
        this.paramMethodRestrictionRestriction = SAFE_ONLY;
        return this;
    }

    /**
     * Add a new ParamDescriptionSingle to the list with the provided parameters.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withParam(final String name, final Function<? super O, P> getter,
                                             final Class<P> paramClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withParam");
        eos.requireNonNull(getter, 2, "getter", "withParam");
        eos.requireNonNull(paramClass, 3, "paramClass", "withParam");
        addSingleParam(paramClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Add a new ParamDescriptionSingle to the list with the provided parameters.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     * @see #withParam(String, Function, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withParam(final String name, final Function<? super O, P> getter,
                                             final ParamMethodRestriction paramMethodRestriction,
                                             final Class<P> paramClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withParam");
        eos.requireNonNull(getter, 2, "getter", "withParam");
        eos.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withParam");
        eos.requireNonNull(paramClass, 4, "paramClass", "withParam");
        addSingleParam(paramClass, name, getter, paramMethodRestriction);
        return this;
    }

    /**
     * Create a new ParamDescriptionSingle and add it to be included in the ParamList.
     *
     * @param paramClass  the class of the parameter in question
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     * @param <P>  the type of the parameter
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     */
    private <P> void addSingleParam(final Class<P> paramClass, final String name, final Function<? super O, P> getter,
                                    final ParamMethodRestriction paramMethodRestriction) {
        addParam(new ParamDescriptionSingle<O, P>(parentClass, paramClass, name, getter, paramMethodRestriction,
                                                  getEasyOverriderServiceOrDefault()));
    }

    /**
     * Create a new ParamDescriptionCollection for a collection and add it to be included in the ParamList.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param entryClass  the class of the entries in the collection - cannot be null
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withParam(String, Function, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")
    public <E, P extends Collection> ParamListBuilder<O> withCollection(final String name, Function<? super O, P> getter,
                                                                        final Class<P> paramClass, final Class<E> entryClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withCollection");
        eos.requireNonNull(getter, 2, "getter", "withCollection");
        eos.requireNonNull(paramClass, 3, "paramClass", "withCollection");
        eos.requireNonNull(entryClass, 4, "entryClass", "withCollection");
        addCollectionParam(paramClass, entryClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Create a new ParamDescriptionCollection for a collection and add it to be included in the ParamList.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param entryClass  the class of the entries in the collection - cannot be null
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @return The current ParamListBuilder
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     * @see #withCollection(String, Function, Class, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")
    public <E, P extends Collection> ParamListBuilder<O> withCollection(final String name, final Function<? super O, P> getter,
                                                                        final ParamMethodRestriction paramMethodRestriction,
                                                                        final Class<P> paramClass, final Class<E> entryClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withCollection");
        eos.requireNonNull(getter, 2, "getter", "withCollection");
        eos.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withCollection");
        eos.requireNonNull(paramClass, 4, "paramClass", "withCollection");
        eos.requireNonNull(entryClass, 5, "entryClass", "withCollection");
        addCollectionParam(paramClass, entryClass, name, getter, paramMethodRestriction);
        return this;
    }

    /**
     * Create a new ParamDescriptionCollection and add it to be included in the ParamList.
     *
     * @param paramClass  the class of the parameter in question
     * @param entryClass  the class of the entries in the collection
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     */
    private <E, P extends Collection<? extends E>> void addCollectionParam(final Class<P> paramClass, final Class<E> entryClass,
                                                                           final String name, final Function<? super O, P> getter,
                                                                           final ParamMethodRestriction paramMethodRestriction) {
        addParam(new ParamDescriptionCollection<O, E, P>(parentClass, paramClass, entryClass, name, getter, paramMethodRestriction,
                                                         getEasyOverriderServiceOrDefault()));
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param keyClass  the class of the map's keys - cannot be null
     * @param entryClass  the class of the entries in the collection - cannot be null
     * @param <P>  the type of the parameter (must be a {@link Map} of some sort)
     * @param <K>  the type of the keys in the map
     * @param <E>  the type of the entries in the map
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withParam(String, Function, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")
    public <K, E, P extends Map> ParamListBuilder<O> withMap(final String name, final Function<? super O, P> getter,
                                                             final Class<P> paramClass, final Class<K> keyClass,
                                                             final Class<E> entryClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withMap");
        eos.requireNonNull(getter, 2, "getter", "withMap");
        eos.requireNonNull(paramClass, 3, "paramClass", "withMap");
        eos.requireNonNull(keyClass, 4, "keyClass", "withMap");
        eos.requireNonNull(entryClass, 5, "entryClass", "withMap");
        addMapParam(paramClass, keyClass, entryClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param keyClass  the class of the map's keys - cannot be null
     * @param entryClass  the class of the entries in the collection - cannot be null
     * @param <P>  the type of the parameter (must be a {@link Map} of some sort)
     * @param <K>  the type of the keys in the map
     * @param <E>  the type of the entries in the map
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")
    public <K, E, P extends Map> ParamListBuilder<O> withMap(final String name, final Function<? super O, P> getter,
                                                             final ParamMethodRestriction paramMethodRestriction,
                                                             final Class<P> paramClass, final Class<K> keyClass,
                                                             final Class<E> entryClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withMap");
        eos.requireNonNull(getter, 2, "getter", "withMap");
        eos.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withMap");
        eos.requireNonNull(paramClass, 4, "paramClass", "withMap");
        eos.requireNonNull(keyClass, 5, "keyClass", "withMap");
        eos.requireNonNull(entryClass, 6, "entryClass", "withMap");
        addMapParam(paramClass, keyClass, entryClass, name, getter, paramMethodRestriction);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.
     *
     * @param paramClass  the class of the parameter in question
     * @param keyClass  the class of the keys in the map
     * @param entryClass  the class of the entries in the map
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     * @param <P>  the type of the parameter (must be a {@link Map} of some sort)
     * @param <K>  the type of the keys in the map
     * @param <E>  the type of the entries in the map
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     */
    private <K, E, P extends Map<? extends K, ? extends E>> void addMapParam(final Class<P> paramClass, final Class<K> keyClass,
                                                                             final Class<E> entryClass, final String name,
                                                                             final Function<? super O, P> getter,
                                                                             final ParamMethodRestriction paramMethodRestriction) {
        addParam(new ParamDescriptionMap<O, K, E, P>(parentClass, paramClass, keyClass, entryClass, name, getter,
                                                     paramMethodRestriction, getEasyOverriderServiceOrDefault()));
    }

    /**
     * Adds a ParamDescription to what we've got.
     *
     * @param paramDescription  the ParamDescription to add
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     * @throws IllegalArgumentException if the name of the provided ParamDescription has already been provided.
     */
    private void addParam(final ParamDescription<? super O, ?> paramDescription) {
        enforceParamMethodRestrictionRestriction(paramDescription.getParamMethodRestriction(), paramDescription.getName());
        String name = paramDescription.getName();
        if (paramDescriptionMap.containsKey(name)) {
            throw new IllegalArgumentException("A parameter named '" + name + "' already exists while trying to " +
                                               "build the ParamList for a " + parentClass.getCanonicalName());
        }
        paramOrder.add(name);
        paramDescriptionMap.put(name, paramDescription);
    }

    /**
     * Updates the parameter having the provided name with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionSingle is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withParam(String, Function, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedParam(final String name, final Function<? super O, P> getter,
                                                    final Class<P> paramClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withUpdatedParam");
        eos.requireNonNull(getter, 2, "getter", "withUpdatedParam");
        eos.requireNonNull(paramClass, 3, "paramClass", "withUpdatedParam");
        updateSingleParam(paramClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Updates the parameter having the provided name with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionSingle is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withParam(String, Function, ParamMethodRestriction, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedParam(final String name, final Function<? super O, P> getter,
                                                    final ParamMethodRestriction paramMethodRestriction,
                                                    final Class<P> paramClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withUpdatedParam");
        eos.requireNonNull(getter, 2, "getter", "withUpdatedParam");
        eos.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withUpdatedParam");
        eos.requireNonNull(paramClass, 4, "paramClass", "withUpdatedParam");
        updateSingleParam(paramClass, name, getter, paramMethodRestriction);
        return this;
    }

    /**
     * Create a new ParamDescriptionSingle and replace the existing ParamDescription with the same name.
     *
     * @param paramClass  the class of the parameter in question
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     * @param <P>  the type of the parameter
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     */
    private <P> void updateSingleParam(final Class<P> paramClass, final String name, final Function<? super O, P> getter,
                                       final ParamMethodRestriction paramMethodRestriction) {
        updateParam(new ParamDescriptionSingle<O, P>(parentClass, paramClass, name, getter, paramMethodRestriction,
                                                     getEasyOverriderServiceOrDefault()));
    }

    /**
     * Updates the parameter having the provided name to be a collection with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionCollection is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param entryClass  the class of the entries in the parameter - cannot be null
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withoutParam(String)
     */
    public <E, P extends Collection<? extends E>> ParamListBuilder<O> withUpdatedCollection(
                    final String name, final Function<? super O, P> getter,
                    final Class<P> paramClass, final Class<E> entryClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withUpdatedCollection");
        eos.requireNonNull(getter, 2, "getter", "withUpdatedCollection");
        eos.requireNonNull(paramClass, 3, "paramClass", "withUpdatedCollection");
        eos.requireNonNull(entryClass, 4, "entryClass", "withUpdatedCollection");
        updateCollectionParam(paramClass, entryClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a collection with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionCollection is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param entryClass  the class of the entries in the parameter - cannot be null
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withoutParam(String)
     */
    public <E, P extends Collection<? extends E>> ParamListBuilder<O> withUpdatedCollection(
                    final String name, final Function<? super O, P> getter,
                    final ParamMethodRestriction paramMethodRestriction,
                    final Class<P> paramClass, final Class<E> entryClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withUpdatedCollection");
        eos.requireNonNull(getter, 2, "getter", "withUpdatedCollection");
        eos.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withUpdatedCollection");
        eos.requireNonNull(paramClass, 4, "paramClass", "withUpdatedCollection");
        eos.requireNonNull(entryClass, 5, "entryClass", "withUpdatedCollection");
        updateCollectionParam(paramClass, entryClass, name, getter, paramMethodRestriction);
        return this;
    }

    /**
     * Create a new ParamDescriptionCollection and replace the existing ParamDescription with the same name.
     *
     * @param paramClass  the class of the parameter in question
     * @param entryClass  the class of the entries in the parameter
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     */
    @SuppressWarnings("unchecked")
    private <E, P extends Collection<? extends E>> void updateCollectionParam(final Class<P> paramClass, final Class<E> entryClass,
                                                                              final String name, final Function<? super O, P> getter,
                                                                              final ParamMethodRestriction paramMethodRestriction) {
        updateParam(new ParamDescriptionCollection<O, E, P>(parentClass, paramClass, entryClass, name, getter, paramMethodRestriction,
                                                            getEasyOverriderServiceOrDefault()));
    }

    /**
     * Updates the parameter having the provided name to be a map with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionMap is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     *
     * @param name the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param keyClass  the class of the keys in the parameter - cannot be null
     * @param entryClass  the class of the entries in the parameter - cannot be null
     * @param <P> he type of the parameter (must be a {@link Map} of some sort)
     * @param <K> he type of the keys in the parameter
     * @param <E> he type of the entries in the parameter
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withoutParam(String)
     */
    private <K, E, P extends Map<? extends K, ? extends E>> ParamListBuilder<O> withUpdatedMap(
                    final String name, final Function<? super O, P> getter,
                    final Class<P> paramClass, final Class<K> keyClass,
                    final Class<E> entryClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withUpdatedMap");
        eos.requireNonNull(getter, 2, "getter", "withUpdatedMap");
        eos.requireNonNull(paramClass, 3, "paramClass", "withUpdatedMap");
        eos.requireNonNull(keyClass, 4, "keyClass", "withUpdatedMap");
        eos.requireNonNull(entryClass, 5, "entryClass", "withUpdatedMap");
        updateMapParam(paramClass, keyClass, entryClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a map with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionMap is created using the given info.
     * Then the old ParamDescription is replaced with this new one.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param keyClass  the class of the keys in the parameter - cannot be null
     * @param entryClass  the class of the entries in the parameter - cannot be null
     * @param <P>  the type of the parameter (must be a {@link Map} of some sort)
     * @param <K>  the type of the keys in the parameter
     * @param <E>  the type of the entries in the parameter
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamMethodRestriction, Class)
     * @see #withUpdatedCollection(String, Function, ParamMethodRestriction, Class, Class)
     * @see #withMap(String, Function, ParamMethodRestriction, Class, Class, Class)
     * @see #withoutParam(String)
     */
    private <K, E, P extends Map<? extends K, ? extends E>> ParamListBuilder<O> withUpdatedMap(
                    final String name, final Function<? super O, P> getter,
                    final ParamMethodRestriction paramMethodRestriction,
                    final Class<P> paramClass, final Class<K> keyClass,
                    final Class<E> entryClass) {
        EasyOverriderService eos = getEasyOverriderServiceOrDefault();
        eos.requireNonNull(name, 1, "name", "withUpdatedMap");
        eos.requireNonNull(getter, 2, "getter", "withUpdatedMap");
        eos.requireNonNull(paramMethodRestriction, 3, "paramMethodRestriction", "withUpdatedMap");
        eos.requireNonNull(paramClass, 4, "paramClass", "withUpdatedMap");
        eos.requireNonNull(keyClass, 5, "keyClass", "withUpdatedMap");
        eos.requireNonNull(entryClass, 6, "entryClass", "withUpdatedMap");
        updateMapParam(paramClass, keyClass, entryClass, name, getter, paramMethodRestriction);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and replace the existing ParamDescription with the same name.
     *
     * @param paramClass  the class of the parameter in question
     * @param keyClass  the class of the keys in the parameter
     * @param entryClass  the class of the entries in the parameter
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value indicating how this parameter should be used
     * @param <P>  the type of the parameter (must be a {@link Map} of some sort)
     * @param <K>  the type of the keys in the parameter
     * @param <E>  the type of the entries in the parameter
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     */
    @SuppressWarnings("unchecked")
    private <K, E, P extends Map<? extends K, ? extends E>> void updateMapParam(
                    final Class<P> paramClass, final Class<K> keyClass, final Class<E> entryClass,
                    final String name, final Function<? super O, P> getter,
                    final ParamMethodRestriction paramMethodRestriction) {
        updateParam(new ParamDescriptionMap<O, K, E, P>(parentClass, paramClass, keyClass, entryClass, name, getter,
                                                        paramMethodRestriction, getEasyOverriderServiceOrDefault()));
    }

    /**
     * Replaces the existing ParamDescription with the same name as the provided ParamDescription.
     *
     * @param paramDescription  the ParamDescription to use
     * @throws IllegalArgumentException if the name of the provided ParamDescription has not already been defined.
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     */
    private void updateParam(final ParamDescription<? super O, ?> paramDescription) {
        enforceParamMethodRestrictionRestriction(paramDescription.getParamMethodRestriction(), paramDescription.getName());
        String name = paramDescription.getName();
        if (!paramDescriptionMap.containsKey(name)) {
            throw new IllegalArgumentException("No parameter named '" + name + "' exists to be updated while trying to " +
                                               "build the ParamList for a " + parentClass.getCanonicalName());
        }
        paramDescriptionMap.replace(name, paramDescription);
    }

    /**
     * Makes sure that the provided ParamMethodRestriction is allowed using this builder's ParamMethodRestrictionRestriction.
     *
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} to check
     * @throws IllegalArgumentException if the {@link ParamMethodRestrictionRestriction} doesn't allow
     *                                  the provided {@link ParamMethodRestriction}.
     */
    private void enforceParamMethodRestrictionRestriction(final ParamMethodRestriction paramMethodRestriction, String name) {
        if (!paramMethodRestrictionRestriction.allows(paramMethodRestriction)) {
            throw new IllegalArgumentException("The ParamMethodRestriction [" + paramMethodRestriction.name() + "] " +
                                               "on the " + name + " parameter " +
                                               "is not allowed while building a ParamList for a " +
                                               parentClass.getCanonicalName() + " " +
                                               "with a ParamMethodRestrictionRestriction of " +
                                               "[" + paramMethodRestrictionRestriction.name() + "].");
        }
    }

    /**
     * Removes the parameter with the provided name.
     *
     * @param name  the name of the parameter to remove - cannot be null
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is null.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @see #withoutParam(String)
     * @see #withParam(String, Function, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     */
    public ParamListBuilder<O> withoutParam(final String name) {
        getEasyOverriderServiceOrDefault().requireNonNull(name, 1, "name", "withoutParam");
        if (!paramDescriptionMap.containsKey(name)) {
            throw new IllegalArgumentException("No parameter named '" + name + "' exists to be removed while trying to " +
                                               "build the ParamList for a " + parentClass.getCanonicalName());
        }
        paramOrder.remove(name);
        paramDescriptionMap.remove(name);
        return this;
    }

    /**
     * Finalizes the builder and returns the final ParamList.
     *
     * @return a ParamList object.
     */
    public ParamList<O> andThatsIt() {
        return new ParamList<O>(parentClass, paramDescriptionMap, paramOrder, getEasyOverriderServiceOrDefault());
    }

    /**
     * Kicks off a ParamListBuilder for the provided class.<br>
     *
     * This is usually done using {@link ParamList#forClass(Class)}, so that you don't have to import ParamListBuilder.
     * That method just calls this one, and this one just calls the {@link #ParamListBuilder(Class)} constructor.
     * This one is mainly here for ease of use in case there's some confusion between a {@link ParamListBuilder} and a {@link ParamList}.
     * This way you can call <code>forClass</code> on either with the same results.
     *
     * @param parentClass  the class you're building the parameter list for
     * @param <C>  the class you're building the parameter list for
     * @return A {@link ParamListBuilder} for the specified class.
     */
    public static <C> ParamListBuilder<C> forClass(Class<C> parentClass) {
        return new ParamListBuilder<C>(parentClass);
    }

    /**
     * equals method for a ParamListBuilder.
     *
     * @param obj  the object to test against
     * @return True if this ParamListBuilder equals the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamListBuilder.
     *
     * @return An int.
     */
    @Override
    public int hashCode() {
        return getParamList().hashCode(this);
    }

    /**
     * toString method for a ParamListBuilder.
     *
     * @return A String.
     */
    @Override
    public String toString() {
        return getParamList().toString(this);
    }
}
