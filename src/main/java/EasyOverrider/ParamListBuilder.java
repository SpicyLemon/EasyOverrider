package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.requireNonNull;
import static EasyOverrider.ParamUsage.INCLUDED_IN_ALL;
import static EasyOverrider.ParamUsage.TOSTRING_ONLY;
import static EasyOverrider.ParamUsageRestriction.ALLOW_UNSAFE;
import static EasyOverrider.ParamUsageRestriction.SAFE_ONLY;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Class to help with building the building of a ParamList.<br>
 *
 * Since the {@link ParamList} and {@link ParamDescription} objects are pretty complicated,
 * this class helps create them in a nice, fluent way.<br>
 *
 * <pre>
 * {@code
 *
 * private static ParamList<Foo> paramList =
 *                 ParamList.forClass(Foo.class)
 *                          .withPrimaryParam("id", Foo::getId, Integer.class)
 *                          .withParam("name", Foo::getName, String.class)
 *                          .withParam("bar", Foo::getBar, Bar.class)
 *                          .andThatsIt();
 * }
 * </pre>
 *
 * @param <O>  the type you're creating the ParamList for
 */
public class ParamListBuilder<O> {

    // The public methods in here that deal with collection and map parameters have to suppress warnings about unchecked conversions.
    // This prevents the warnings from showing up everywhere a call is made to those methods.
    //
    // The signature for those methods contain the raw Collection or Map types instead of also including the types they contain.
    // For example, public <E, P extends Collection> ParamListBuilder<O> withCollection(...). Notice the raw Collection there instead
    // of Collection<? extends E>.  If the <? extends E> is included in the signature, then the Class<P> parameter provided to
    // that method confuses things. You cannot create a Class<Collection<? extends E>> object, it can only be Class<Collection>.
    // So with the <? extends E> the compiler complains, and things just don't work.
    //
    // Basically, in order for all of this to be usable, those methods needed to just have the raw types for P.
    //
    // However, the ParamDescription objects still need that information.  So when calling the private add or update methods in here,
    // the <? extends E> part is included in the signature.  This results in an unchecked warning, but they're actually mostly okay.
    //
    // Java forgets all that crap when running anyway.

    private final Class<O> parentClass;
    private ParamUsageRestriction paramUsageRestriction;
    private final List<String> paramOrder;
    private final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap;
    private boolean usingServiceCalled = false;
    private ParamListService paramListService;
    private boolean configuredByCalled = false;
    private ParamListServiceConfig paramListServiceConfig;

    private static ParamList<ParamListBuilder> paramList;

    /**
     * Get the ParamList for a ParamListBuilder object.<br>
     *
     * This will create the ParamList if it's not already set.
     * The reason it does this rather than just having it statically set when the variable is created,
     * is that it uses itself. The class code needs to be loaded before it can be used.<br>
     *
     * @return The ParamList for a ParamListBuilder.
     */
    private static ParamList<ParamListBuilder> getParamList() {
        if (paramList == null) {
            //This will create the ParamList if it's not already set.
            //The reason it does this rather than just having it statically set when the variable is created,
            // is that it uses itself. The class code needs to be loaded before it can be used.
            paramList = ParamList.forClass(ParamListBuilder.class)
                                 .withParam("parentClass", (plb) -> plb.parentClass, Class.class)
                                 .withParam("paramUsageRestriction",
                                            (plb) -> plb.paramUsageRestriction,
                                            ParamUsageRestriction.class)
                                 .withCollection("paramOrder", (plb) -> plb.paramOrder, List.class, String.class)
                                 .withMap("paramDescriptionMap", (plb) -> plb.paramDescriptionMap,
                                          Map.class, String.class, ParamDescription.class)
                                 .withParam("usingServiceCalled", (plb) -> plb.usingServiceCalled, Boolean.class)
                                 .withParam("paramListService", (plb) -> plb.paramListService, ParamListService.class)
                                 .withParam("configuredByCalled", (plb) -> plb.configuredByCalled, Boolean.class)
                                 .withParam("paramListServiceConfig",
                                            (plb) -> plb.paramListServiceConfig,
                                            ParamListServiceConfig.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Private constructor to do all the meat of the setting of stuff without any validation.<br>
     *
     * This is so that the setting can all be done in one place, but we can have validation on the different actual constructors.<br>
     *
     * @param paramListService  the service that the ParamList will use for most functionality
     * @param parentClass  the class of the object being described
     * @param superParamList  any existing ParamList available to the parentClass
     */
    private ParamListBuilder(final ParamListService paramListService,
                             final Class<O> parentClass, final ParamList<? super O> superParamList) {
        this.paramListService = paramListService;
        this.paramListServiceConfig = Optional.ofNullable(paramListService)
                                              .map(ParamListService::getConfig)
                                              .orElse(null);
        this.parentClass = parentClass;
        this.paramOrder = Optional.ofNullable(superParamList)
                                  .map(ParamList::getParamOrder)
                                  .map(LinkedList::new)
                                  .orElseGet(LinkedList::new);
        this.paramDescriptionMap = Optional.ofNullable(superParamList)
                                           .map(ParamList::getParamDescriptionMap)
                                           .map(HashMap<String, ParamDescription<? super O, ?>>::new)
                                           .orElseGet(HashMap::new);
        this.paramUsageRestriction = SAFE_ONLY;
    }

    /**
     * Constructor for basing a new list off of an existing one.<br>
     *
     * This is usually done using {@link ParamList#extendedBy(Class)}
     * so that you don't have to import ParamListBuilder.<br>
     *
     * @param parentClass  the class of the object being described - cannot be null
     * @param superParamList  the existing {@link ParamList] available to the parentClass - cannot be null
     * @see ParamList#extendedBy(Class)
     */
    ParamListBuilder(final Class<O> parentClass, final ParamList<? super O> superParamList,
                     final ParamListService paramListService) {
        this(paramListService, parentClass, superParamList);
        requireNonNull(parentClass, 1, "parentClass", "ParamListBuilder Constructor");
        requireNonNull(superParamList, 2, "superParamList", "ParamListBuilder Constructor");
        requireNonNull(paramListService, 3, "paramListService", "ParamListBuilder Constructor");
    }

    /**
     * Kicks off a ParamListBuilder for the provided class.<br>
     *
     * This is usually done using {@link ParamList#forClass(Class)}, so that you don't have to import ParamListBuilder.
     * This one is mainly here for ease of use in case there's some confusion between a {@link ParamListBuilder} and a {@link ParamList}.
     * This way you can call <code>forClass</code> on either with the same results.<br>
     *
     * @param parentClass  the class you're building the parameter list for
     * @param <C>  the class you're building the parameter list for
     * @return A {@link ParamListBuilder} for the specified class.
     * @throws IllegalArgumentException if the provided class is null.
     */
    public static <C> ParamListBuilder<C> forClass(final Class<C> parentClass) {
        requireNonNull(parentClass, 1, "parentClass", "forClass");
        return new ParamListBuilder<>(null, parentClass, null);
    }

    /**
     * Set the ParamListService to use.<br>
     *
     * This method can only be called once for any ParamListBuilder.
     * Calling it a second time will result in an {@link IllegalStateException} being thrown.<br>
     *
     * If this method is not called, the default {@link ParamListServiceImpl} is used.<br>
     *
     * @param paramListService  the ParamListService to use for the parameters and param list
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided parameter is null.
     * @throws IllegalStateException if called multiple times for a builder.
     */
    public ParamListBuilder<O> usingService(final ParamListService paramListService) {
        requireNonNull(paramListService, 1, "paramListService", "usingService");
        if (usingServiceCalled) {
            throw new IllegalStateException("Method usingService called multiple times " +
                                            "while building the ParamList for " + parentClass.getCanonicalName());
        }
        this.paramListService = paramListService;
        if (!configuredByCalled) {
            this.paramListServiceConfig = paramListService.getConfig();
        }
        usingServiceCalled = true;
        return this;
    }

    /**
     * Set the ParamListServiceConfig to use.<br>
     *
     * This method can only be called once for any ParamListBuilder.
     * Calling it a second time will result in an {@link IllegalStateException} being thrown.<br>
     *
     * The config provided to this method takes precedence over any config
     * already contained in a service provided to {@link #usingService(ParamListService)}.
     * This is true regardless of the order in which <code>configuredBy</code> and <code>usingService</code> are called.<br>
     *
     * If <code>usingService</code> is not called, the default service will
     * be used in conjunction with the config provided to this method.<br>
     *
     * If not called, behavior is as follows:<br>
     * If {@link #usingService(ParamListService)} has been called, the config contained in that service is used.<br>
     * If not, then If this builder was created using {@link ParamList#extendedBy(Class)},
     * then the config used for the service in the original ParamList is maintained and provided to the new one.<br>
     * Otherwise, the default config is used.<br>
     *
     * @param paramListServiceConfig  the ParamListServiceConfig to use
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided parameter is null.
     * @throws IllegalStateException if called multiple times for a builder.
     */
    public ParamListBuilder<O> configuredBy(final ParamListServiceConfig paramListServiceConfig) {
        requireNonNull(paramListServiceConfig, 1, "paramListServiceConfig", "configuredBy");
        if (configuredByCalled) {
            throw new IllegalStateException("Method configuredBy called multiple times " +
                                            "while building the ParamList for " + parentClass.getCanonicalName());
        }
        this.paramListServiceConfig = paramListServiceConfig;
        configuredByCalled = true;
        return this;
    }

    /**
     * Set the ParamUsageRestriction value for the builder.<br>
     *
     * By default, a ParamListBuilder uses <code>ParamUsageRestriction.SAFE_ONLY</code>.<br>
     *
     * @param paramUsageRestriction  the ParamUsageRestriction to use
     * @see #allowingOnlySafeParamUsages()
     * @see #allowingUnsafeParamUsages()
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided parameter is null.
     */
    public ParamListBuilder<O> havingRestriction(final ParamUsageRestriction paramUsageRestriction) {
        requireNonNull(paramUsageRestriction, 1, "paramUsageRestriction", "havingRestriction");
        this.paramUsageRestriction = paramUsageRestriction;
        return this;
    }

    /**
     * Allow unsafe ParamUsage values.<br>
     *
     * This is the same as <code>havingRestriction(ParamUsageRestriction.ALLOW_UNSAFE)</code>.<br>
     *
     * @return The current ParamListBuilder.
     * @see #havingRestriction(ParamUsageRestriction)
     * @see #allowingOnlySafeParamUsages()
     */
    public ParamListBuilder<O> allowingUnsafeParamUsages() {
        this.paramUsageRestriction = ALLOW_UNSAFE;
        return this;
    }

    /**
     * Only allow safe ParamUsage values.<br>
     *
     * By default, a ParamListBuilder is in this state.<br>
     *
     * This is the same as <code>havingRestriction(ParamUsageRestriction.SAFE_ONLY)</code>.
     *
     * @return The current ParamListBuilder.
     * @see #havingRestriction(ParamUsageRestriction)
     * @see #allowingOnlySafeParamUsages()
     */
    public ParamListBuilder<O> allowingOnlySafeParamUsages() {
        this.paramUsageRestriction = SAFE_ONLY;
        return this;
    }

    /**
     * Add a new ParamDescriptionSingle to the list with the provided parameters.<br>
     *
     * Uses the default ParamUsage of {@link ParamUsage#INCLUDED_IN_ALL}.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withParam(String, Function, ParamUsage, Class)
     * @see #withPrimaryParam(String, Function, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withParam(final String name, final Function<? super O, P> getter,
                                             final Class<P> paramClass) {
        requireNonNull(name, 1, "name", "withParam");
        requireNonNull(getter, 2, "getter", "withParam");
        requireNonNull(paramClass, 3, "paramClass", "withParam");
        addSingleParam(paramClass, name, getter, INCLUDED_IN_ALL, false);
        return this;
    }

    /**
     * Add a new ParamDescriptionSingle to the list with the provided parameters.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     * @see #withParam(String, Function, Class)
     * @see #withPrimaryParam(String, Function, ParamUsage, Class)
     * @see #withCollection(String, Function, ParamUsage, Class, Class)
     * @see #withMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamUsage, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withParam(final String name, final Function<? super O, P> getter,
                                             final ParamUsage paramUsage,
                                             final Class<P> paramClass) {
        requireNonNull(name, 1, "name", "withParam");
        requireNonNull(getter, 2, "getter", "withParam");
        requireNonNull(paramUsage, 3, "paramUsage", "withParam");
        requireNonNull(paramClass, 4, "paramClass", "withParam");
        addSingleParam(paramClass, name, getter, paramUsage, false);
        return this;
    }

    /**
     * Add a new ParamDescriptionSingle that represents a primary parameter.<br>
     *
     * The default ParamUsage is {@link ParamUsage#TOSTRING_ONLY}.<br>
     *
     * The main purpose of a "Primary Parameter" is that it is still included in the toString output of
     * the containing object, even if a recursive toString is detected.
     * The idea is that, if the full contents of the containing object are included elsewhere in the string,
     * this parameter can be used to identify that entry.<br>
     *
     * <B>Caution:</B><br>
     * Primary Parameters should be used sparingly as they bypass recursion prevention.
     * Usually these are identifier parameters with simple types such as int or String.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withPrimaryParam(String, Function, ParamUsage, Class)
     * @see #withParam(String, Function, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedPrimaryParam(String, Function, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withPrimaryParam(final String name, final Function<? super O, P> getter,
                                                    final Class<P> paramClass) {
        requireNonNull(name, 1, "name", "withPrimaryParam");
        requireNonNull(getter, 2, "getter", "withPrimaryParam");
        requireNonNull(paramClass, 3, "paramClass", "withPrimaryParam");
        addSingleParam(paramClass, name, getter, TOSTRING_ONLY, true);
        return this;
    }

    /**
     * Add a new ParamDescriptionSingle that represents a primary parameter.<br>
     *
     * The main purpose of a "Primary Parameter" is that it is still included in the toString output of
     * the containing object, even if a recursive toString is detected.
     * The idea is that, if the full contents of the containing object are included elsewhere in the string,
     * this parameter can be used to identify that entry.<br>
     *
     * <B>Caution 1:</B><br>
     * Primary Parameters should be used sparingly as they bypass recursion prevention.
     * Usually these are identifier parameters with simple types such as int or String.<br>
     *
     * <B>Caution 2:</B><br>
     * It is usually unwise to include the primary key column(s) of database entities in <code>equals()</code> and <code>hashCode()</code>.
     * This is because saving the entity can alter the parameter, causing strange inequalities and changing hashCodes.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withPrimaryParam(String, Function, Class)
     * @see #withParam(String, Function, ParamUsage, Class)
     * @see #withCollection(String, Function, ParamUsage, Class, Class)
     * @see #withMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withUpdatedPrimaryParam(String, Function, ParamUsage, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withPrimaryParam(final String name, final Function<? super O, P> getter,
                                                    final ParamUsage paramUsage,
                                                    final Class<P> paramClass) {
        requireNonNull(name, 1, "name", "withPrimaryParam");
        requireNonNull(getter, 2, "getter", "withPrimaryParam");
        requireNonNull(paramUsage, 3, "paramUsage", "withPrimaryParam");
        requireNonNull(paramClass, 4, "paramClass", "withPrimaryParam");
        addSingleParam(paramClass, name, getter, paramUsage, true);
        return this;
    }

    /**
     * Create a new ParamDescriptionSingle and add it to be included in the ParamList.<br>
     *
     * @param paramClass  the class of the parameter in question
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     * @param <P>  the type of the parameter
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     */
    private <P> void addSingleParam(final Class<P> paramClass, final String name, final Function<? super O, P> getter,
                                    final ParamUsage paramUsage, final boolean isPrimaryKey) {
        addParam(new ParamDescriptionSingle<O, P>(parentClass, paramClass, name, getter, paramUsage, isPrimaryKey));
    }

    /**
     * Create a new ParamDescriptionCollection for a collection and add it to be included in the ParamList.<br>
     *
     * Uses the default paramUsage of {@link ParamUsage#INCLUDED_IN_ALL}.<br>
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
     * @see #withCollection(String, Function, ParamUsage, Class, Class)
     * @see #withParam(String, Function, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")   //There's a comment at the top about why this is needed.
    public <E, P extends Collection> ParamListBuilder<O> withCollection(final String name, Function<? super O, P> getter,
                                                                        final Class<P> paramClass, final Class<E> entryClass) {
        requireNonNull(name, 1, "name", "withCollection");
        requireNonNull(getter, 2, "getter", "withCollection");
        requireNonNull(paramClass, 3, "paramClass", "withCollection");
        requireNonNull(entryClass, 4, "entryClass", "withCollection");
        addCollectionParam(paramClass, entryClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Create a new ParamDescriptionCollection for a collection and add it to be included in the ParamList.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param entryClass  the class of the entries in the collection - cannot be null
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @return The current ParamListBuilder
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     * @see #withCollection(String, Function, Class, Class)
     * @see #withParam(String, Function, ParamUsage, Class)
     * @see #withMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamUsage, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")   //There's a comment at the top about why this is needed.
    public <E, P extends Collection> ParamListBuilder<O> withCollection(final String name, final Function<? super O, P> getter,
                                                                        final ParamUsage paramUsage,
                                                                        final Class<P> paramClass, final Class<E> entryClass) {
        requireNonNull(name, 1, "name", "withCollection");
        requireNonNull(getter, 2, "getter", "withCollection");
        requireNonNull(paramUsage, 3, "paramUsage", "withCollection");
        requireNonNull(paramClass, 4, "paramClass", "withCollection");
        requireNonNull(entryClass, 5, "entryClass", "withCollection");
        addCollectionParam(paramClass, entryClass, name, getter, paramUsage);
        return this;
    }

    /**
     * Create a new ParamDescriptionCollection and add it to be included in the ParamList.<br>
     *
     * @param paramClass  the class of the parameter in question
     * @param entryClass  the class of the entries in the collection
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     */
    private <E, P extends Collection<? extends E>> void addCollectionParam(final Class<P> paramClass, final Class<E> entryClass,
                                                                           final String name, final Function<? super O, P> getter,
                                                                           final ParamUsage paramUsage) {
        addParam(new ParamDescriptionCollection<O, E, P>(parentClass, paramClass, entryClass, name, getter, paramUsage));
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.<br>
     *
     * Uses the default ParamUsage of {@link ParamUsage#INCLUDED_IN_ALL}.<br>
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
     * @see #withMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withParam(String, Function, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")   //There's a comment at the top about why this is needed.
    public <K, E, P extends Map> ParamListBuilder<O> withMap(final String name, final Function<? super O, P> getter,
                                                             final Class<P> paramClass, final Class<K> keyClass,
                                                             final Class<E> entryClass) {
        requireNonNull(name, 1, "name", "withMap");
        requireNonNull(getter, 2, "getter", "withMap");
        requireNonNull(paramClass, 3, "paramClass", "withMap");
        requireNonNull(keyClass, 4, "keyClass", "withMap");
        requireNonNull(entryClass, 5, "entryClass", "withMap");
        addMapParam(paramClass, keyClass, entryClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
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
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withParam(String, Function, ParamUsage, Class)
     * @see #withCollection(String, Function, ParamUsage, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")   //There's a comment at the top about why this is needed.
    public <K, E, P extends Map> ParamListBuilder<O> withMap(final String name, final Function<? super O, P> getter,
                                                             final ParamUsage paramUsage,
                                                             final Class<P> paramClass, final Class<K> keyClass,
                                                             final Class<E> entryClass) {
        requireNonNull(name, 1, "name", "withMap");
        requireNonNull(getter, 2, "getter", "withMap");
        requireNonNull(paramUsage, 3, "paramUsage", "withMap");
        requireNonNull(paramClass, 4, "paramClass", "withMap");
        requireNonNull(keyClass, 5, "keyClass", "withMap");
        requireNonNull(entryClass, 6, "entryClass", "withMap");
        addMapParam(paramClass, keyClass, entryClass, name, getter, paramUsage);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and add it to be included in the ParamList.<br>
     *
     * @param paramClass  the class of the parameter in question
     * @param keyClass  the class of the keys in the map
     * @param entryClass  the class of the entries in the map
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     * @param <P>  the type of the parameter (must be a {@link Map} of some sort)
     * @param <K>  the type of the keys in the map
     * @param <E>  the type of the entries in the map
     * @throws IllegalArgumentException if a ParamDescription with the same name has already been added to this builder.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     */
    private <K, E, P extends Map<? extends K, ? extends E>> void addMapParam(final Class<P> paramClass, final Class<K> keyClass,
                                                                             final Class<E> entryClass, final String name,
                                                                             final Function<? super O, P> getter,
                                                                             final ParamUsage paramUsage) {
        addParam(new ParamDescriptionMap<O, K, E, P>(parentClass, paramClass, keyClass, entryClass,
                                                     name, getter, paramUsage));
    }

    /**
     * Adds a ParamDescription to what we've got.<br>
     *
     * @param paramDescription  the ParamDescription to add
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     * @throws IllegalArgumentException if the name of the provided ParamDescription has already been provided.
     */
    private void addParam(final ParamDescription<? super O, ?> paramDescription) {
        enforceParamMethodUsageRestriction(paramDescription.getParamUsage(), paramDescription.getName());
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
     * Uses the default ParamUsage of {@link ParamUsage#INCLUDED_IN_ALL}.<br>
     *
     * Specifically, a new ParamDescriptionSingle is created using the given info.
     * Then the old ParamDescription is replaced with this new one.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedParam(String, Function, ParamUsage, Class)
     * @see #withUpdatedPrimaryParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withParam(String, Function, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedParam(final String name, final Function<? super O, P> getter,
                                                    final Class<P> paramClass) {
        requireNonNull(name, 1, "name", "withUpdatedParam");
        requireNonNull(getter, 2, "getter", "withUpdatedParam");
        requireNonNull(paramClass, 3, "paramClass", "withUpdatedParam");
        updateSingleParam(paramClass, name, getter, INCLUDED_IN_ALL, false);
        return this;
    }

    /**
     * Updates the parameter having the provided name with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionSingle is created using the given info.
     * Then the old ParamDescription is replaced with this new one.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedPrimaryParam(String, Function, ParamUsage, Class)
     * @see #withUpdatedCollection(String, Function, ParamUsage, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withParam(String, Function, ParamUsage, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedParam(final String name, final Function<? super O, P> getter,
                                                    final ParamUsage paramUsage,
                                                    final Class<P> paramClass) {
        requireNonNull(name, 1, "name", "withUpdatedParam");
        requireNonNull(getter, 2, "getter", "withUpdatedParam");
        requireNonNull(paramUsage, 3, "paramUsage", "withUpdatedParam");
        requireNonNull(paramClass, 4, "paramClass", "withUpdatedParam");
        updateSingleParam(paramClass, name, getter, paramUsage, false);
        return this;
    }

    /**
     * Updates the parameter having the provided name with the new values given.<br>
     *
     * The default paramUsage is {@link ParamUsage#TOSTRING_ONLY}.<br>
     *
     * Specifically, a new ParamDescriptionSingle is created using the given info.
     * Then the old ParamDescription is replaced with this new one.<br>
     *
     * The main purpose of a "Primary Parameter" is that it is still included in the toString output of
     * the containing object, even if a recursive toString is detected.
     * The idea is that, if the full contents of the containing object are included elsewhere in the string,
     * this parameter can be used to identify that entry.<br>
     *
     * <B>Caution:</B><br>
     * Primary Parameters should be used sparingly as they bypass recursion prevention.
     * Usually these are identifier parameters with simple types such as int or String.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @see #withUpdatedPrimaryParam(String, Function, ParamUsage, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withPrimaryParam(String, Function, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedPrimaryParam(final String name, final Function<? super O, P> getter,
                                                           final Class<P> paramClass) {
        requireNonNull(name, 1, "name", "withUpdatedPrimaryParam");
        requireNonNull(getter, 2, "getter", "withUpdatedPrimaryParam");
        requireNonNull(paramClass, 3, "paramClass", "withUpdatedPrimaryParam");
        updateSingleParam(paramClass, name, getter, TOSTRING_ONLY, true);
        return this;
    }

    /**
     * Updates the parameter having the provided name with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionSingle is created using the given info.
     * Then the old ParamDescription is replaced with this new one.<br>
     *
     * The main purpose of a "Primary Parameter" is that it is still included in the toString output of
     * the containing object, even if a recursive toString is detected.
     * The idea is that, if the full contents of the containing object are included elsewhere in the string,
     * this parameter can be used to identify that entry.<br>
     *
     * <B>Caution 1:</B><br>
     * Primary Parameters should be used sparingly as they bypass recursion prevention.
     * Usually these are identifier parameters with simple types such as int or String.<br>
     *
     * <B>Caution 2:</B><br>
     * It is usually unwise to include the primary key column(s) of database entities in <code>equals()</code> and <code>hashCode()</code>.
     * This is because saving the entity can alter the parameter, causing strange inequalities and changing hashCodes.
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param <P>  the type of the parameter being described
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     * @see #withUpdatedPrimaryParam(String, Function, Class)
     * @see #withUpdatedParam(String, Function, ParamUsage, Class)
     * @see #withUpdatedCollection(String, Function, ParamUsage, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withPrimaryParam(String, Function, ParamUsage, Class)
     * @see #withoutParam(String)
     */
    public <P> ParamListBuilder<O> withUpdatedPrimaryParam(final String name, final Function<? super O, P> getter,
                                                           final ParamUsage paramUsage,
                                                           final Class<P> paramClass) {
        requireNonNull(name, 1, "name", "withUpdatedPrimaryParam");
        requireNonNull(getter, 2, "getter", "withUpdatedPrimaryParam");
        requireNonNull(paramUsage, 3, "paramUsage", "withUpdatedPrimaryParam");
        requireNonNull(paramClass, 4, "paramClass", "withUpdatedPrimaryParam");
        updateSingleParam(paramClass, name, getter, paramUsage, true);
        return this;
    }

    /**
     * Create a new ParamDescriptionSingle and replace the existing ParamDescription with the same name.<br>
     *
     * @param paramClass  the class of the parameter in question
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     * @param <P>  the type of the parameter
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     */
    private <P> void updateSingleParam(final Class<P> paramClass, final String name, final Function<? super O, P> getter,
                                       final ParamUsage paramUsage, boolean isPrimaryKey) {
        updateParam(new ParamDescriptionSingle<O, P>(parentClass, paramClass, name, getter, paramUsage, isPrimaryKey));
    }

    /**
     * Updates the parameter having the provided name to be a collection with the new values given.<br>
     *
     * Uses the default ParamUsage of {@link ParamUsage#INCLUDED_IN_ALL}.<br>
     *
     * Specifically, a new ParamDescriptionCollection is created using the given info.
     * Then the old ParamDescription is replaced with this new one.<br>
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
     * @see #withUpdatedCollection(String, Function, ParamUsage, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")   //There's a comment at the top about why this is needed.
    public <E, P extends Collection> ParamListBuilder<O> withUpdatedCollection(
                    final String name, final Function<? super O, P> getter,
                    final Class<P> paramClass, final Class<E> entryClass) {
        requireNonNull(name, 1, "name", "withUpdatedCollection");
        requireNonNull(getter, 2, "getter", "withUpdatedCollection");
        requireNonNull(paramClass, 3, "paramClass", "withUpdatedCollection");
        requireNonNull(entryClass, 4, "entryClass", "withUpdatedCollection");
        updateCollectionParam(paramClass, entryClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a collection with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionCollection is created using the given info.
     * Then the old ParamDescription is replaced with this new one.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     *                                - cannot be null
     * @param paramClass  the class of the parameter in question - cannot be null
     * @param entryClass  the class of the entries in the parameter - cannot be null
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if any parameter is null.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamUsage, Class)
     * @see #withUpdatedMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withCollection(String, Function, ParamUsage, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")   //There's a comment at the top about why this is needed.
    public <E, P extends Collection> ParamListBuilder<O> withUpdatedCollection(
                    final String name, final Function<? super O, P> getter,
                    final ParamUsage paramUsage,
                    final Class<P> paramClass, final Class<E> entryClass) {
        requireNonNull(name, 1, "name", "withUpdatedCollection");
        requireNonNull(getter, 2, "getter", "withUpdatedCollection");
        requireNonNull(paramUsage, 3, "paramUsage", "withUpdatedCollection");
        requireNonNull(paramClass, 4, "paramClass", "withUpdatedCollection");
        requireNonNull(entryClass, 5, "entryClass", "withUpdatedCollection");
        updateCollectionParam(paramClass, entryClass, name, getter, paramUsage);
        return this;
    }

    /**
     * Create a new ParamDescriptionCollection and replace the existing ParamDescription with the same name.<br>
     *
     * @param paramClass  the class of the parameter in question
     * @param entryClass  the class of the entries in the parameter
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     * @param <P>  the type of the parameter (must be a {@link Collection} of some sort)
     * @param <E>  the type of the entries in the parameter
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     */
    private <E, P extends Collection<? extends E>> void updateCollectionParam(final Class<P> paramClass, final Class<E> entryClass,
                                                                              final String name, final Function<? super O, P> getter,
                                                                              final ParamUsage paramUsage) {
        updateParam(new ParamDescriptionCollection<O, E, P>(parentClass, paramClass, entryClass,
                                                            name, getter, paramUsage));
    }

    /**
     * Updates the parameter having the provided name to be a map with the new values given.<br>
     *
     * Uses the default ParamUsage of {@link ParamUsage#INCLUDED_IN_ALL}.<br>
     *
     * Specifically, a new ParamDescriptionMap is created using the given info.
     * Then the old ParamDescription is replaced with this new one.<br>
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
     * @see #withUpdatedMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")   //There's a comment at the top about why this is needed.
    private <K, E, P extends Map> ParamListBuilder<O> withUpdatedMap(
                    final String name, final Function<? super O, P> getter,
                    final Class<P> paramClass, final Class<K> keyClass,
                    final Class<E> entryClass) {
        requireNonNull(name, 1, "name", "withUpdatedMap");
        requireNonNull(getter, 2, "getter", "withUpdatedMap");
        requireNonNull(paramClass, 3, "paramClass", "withUpdatedMap");
        requireNonNull(keyClass, 4, "keyClass", "withUpdatedMap");
        requireNonNull(entryClass, 5, "entryClass", "withUpdatedMap");
        updateMapParam(paramClass, keyClass, entryClass, name, getter, INCLUDED_IN_ALL);
        return this;
    }

    /**
     * Updates the parameter having the provided name to be a map with the new values given.<br>
     *
     * Specifically, a new ParamDescriptionMap is created using the given info.
     * Then the old ParamDescription is replaced with this new one.<br>
     *
     * @param name  the name of the parameter, e.g. "id" - cannot be null
     * @param getter  the getter for the parameter, e.g. Product::getId - cannot be null
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
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
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, ParamUsage, Class)
     * @see #withUpdatedCollection(String, Function, ParamUsage, Class, Class)
     * @see #withMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withoutParam(String)
     */
    @SuppressWarnings("unchecked")   //There's a comment at the top about why this is needed.
    private <K, E, P extends Map> ParamListBuilder<O> withUpdatedMap(
                    final String name, final Function<? super O, P> getter,
                    final ParamUsage paramUsage,
                    final Class<P> paramClass, final Class<K> keyClass,
                    final Class<E> entryClass) {
        requireNonNull(name, 1, "name", "withUpdatedMap");
        requireNonNull(getter, 2, "getter", "withUpdatedMap");
        requireNonNull(paramUsage, 3, "paramUsage", "withUpdatedMap");
        requireNonNull(paramClass, 4, "paramClass", "withUpdatedMap");
        requireNonNull(keyClass, 5, "keyClass", "withUpdatedMap");
        requireNonNull(entryClass, 6, "entryClass", "withUpdatedMap");
        updateMapParam(paramClass, keyClass, entryClass, name, getter, paramUsage);
        return this;
    }

    /**
     * Create a new ParamDescriptionMap and replace the existing ParamDescription with the same name.<br>
     *
     * @param paramClass  the class of the parameter in question
     * @param keyClass  the class of the keys in the parameter
     * @param entryClass  the class of the entries in the parameter
     * @param name  the name of the parameter, e.g. "id"
     * @param getter  the getter for the parameter, e.g. Product::getId
     * @param paramUsage  the {@link ParamUsage} value indicating how this parameter should be used
     * @param <P>  the type of the parameter (must be a {@link Map} of some sort)
     * @param <K>  the type of the keys in the parameter
     * @param <E>  the type of the entries in the parameter
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     */
    private <K, E, P extends Map<? extends K, ? extends E>> void updateMapParam(
                    final Class<P> paramClass, final Class<K> keyClass, final Class<E> entryClass,
                    final String name, final Function<? super O, P> getter,
                    final ParamUsage paramUsage) {
        updateParam(new ParamDescriptionMap<O, K, E, P>(parentClass, paramClass, keyClass, entryClass,
                                                        name, getter, paramUsage));
    }

    /**
     * Replaces the existing ParamDescription with the same name as the provided ParamDescription.<br>
     *
     * @param paramDescription  the ParamDescription to use
     * @throws IllegalArgumentException if the name of the provided ParamDescription has not already been defined.
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     */
    private void updateParam(final ParamDescription<? super O, ?> paramDescription) {
        enforceParamMethodUsageRestriction(paramDescription.getParamUsage(), paramDescription.getName());
        String name = paramDescription.getName();
        if (!paramDescriptionMap.containsKey(name)) {
            throw new IllegalArgumentException("No parameter named '" + name + "' exists to be updated while trying to " +
                                               "build the ParamList for a " + parentClass.getCanonicalName());
        }
        paramDescriptionMap.replace(name, paramDescription);
    }

    /**
     * Makes sure that the provided ParamUsage is allowed using this builder's ParamUsageRestriction.<br>
     *
     * @param paramUsage  the {@link ParamUsage} to check
     * @throws IllegalArgumentException if the {@link ParamUsageRestriction} doesn't allow
     *                                  the provided {@link ParamUsage}.
     */
    private void enforceParamMethodUsageRestriction(final ParamUsage paramUsage, final String name) {
        if (!paramUsageRestriction.allows(paramUsage)) {
            throw new IllegalArgumentException("The ParamUsage [" + paramUsage.name() + "] " +
                                               "on the " + name + " parameter " +
                                               "is not allowed while building a ParamList for a " +
                                               parentClass.getCanonicalName() + " " +
                                               "with a ParamUsageRestriction of " +
                                               "[" + paramUsageRestriction.name() + "].");
        }
    }

    /**
     * Removes the parameter with the provided name.<br>
     *
     * @param name  the name of the parameter to remove - cannot be null
     * @return The current ParamListBuilder.
     * @throws IllegalArgumentException if the provided name is null.
     * @throws IllegalArgumentException if the provided name is not already defined.
     * @see #withParam(String, Function, Class)
     * @see #withParam(String, Function, ParamUsage, Class)
     * @see #withPrimaryParam(String, Function, Class)
     * @see #withPrimaryParam(String, Function, ParamUsage, Class)
     * @see #withCollection(String, Function, Class, Class)
     * @see #withCollection(String, Function, ParamUsage, Class, Class)
     * @see #withMap(String, Function, Class, Class, Class)
     * @see #withMap(String, Function, ParamUsage, Class, Class, Class)
     * @see #withUpdatedParam(String, Function, Class)
     * @see #withUpdatedParam(String, Function, ParamUsage, Class)
     * @see #withUpdatedPrimaryParam(String, Function, Class)
     * @see #withUpdatedPrimaryParam(String, Function, ParamUsage, Class)
     * @see #withUpdatedCollection(String, Function, Class, Class)
     * @see #withUpdatedCollection(String, Function, ParamUsage, Class, Class)
     * @see #withUpdatedMap(String, Function, Class, Class, Class)
     * @see #withUpdatedMap(String, Function, ParamUsage, Class, Class, Class)
     */
    public ParamListBuilder<O> withoutParam(final String name) {
        requireNonNull(name, 1, "name", "withoutParam");
        if (!paramDescriptionMap.containsKey(name)) {
            throw new IllegalArgumentException("No parameter named '" + name + "' exists to be removed while trying to " +
                                               "build the ParamList for a " + parentClass.getCanonicalName());
        }
        paramOrder.remove(name);
        paramDescriptionMap.remove(name);
        return this;
    }

    /**
     * Finalizes the builder and returns the final ParamList.<br>
     *
     * @return a ParamList object.
     */
    public ParamList<O> andThatsIt() {
        if (paramListService == null) {
            paramListService = new ParamListServiceImpl();
        }
        if (paramListServiceConfig != null) {
            paramListService.setConfig(paramListServiceConfig);
        }
        return new ParamList<O>(parentClass, paramDescriptionMap, paramOrder, paramListService);
    }

    /**
     * equals method for a ParamListBuilder.<br>
     *
     * @param obj  the object to test against
     * @return True if this ParamListBuilder equals the provided object. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getParamList().equals(this, obj);
    }

    /**
     * hashCode method for a ParamListBuilder.<br>
     *
     * @return An int.
     */
    @Override
    public int hashCode() {
        return getParamList().hashCode(this);
    }

    /**
     * toString method for a ParamListBuilder.<br>
     *
     * @return A String.
     */
    @Override
    public String toString() {
        return getParamList().toString(this);
    }
}
