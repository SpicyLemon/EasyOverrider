package EasyOverrider;

/**
 * An abstract class that overrides the equals, hashCode and toString methods using a ParamList.<br>
 *
 * The extender is required to implement the {@link #getParamList()} method.<br>
 *
 * Example class:
 * <pre>{@code
 *
 * import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;
 *
 * import Bar;
 * import EasyOverrider.EasyOverrider;
 * import EasyOverrider.ParamList;
 *
 * public class Foo extends EasyOverrider<Foo> {
 *     private int id;
 *     private String name;
 *     private Bar bar;
 *
 *     private static ParamList<Foo> paramList = null;
 *
 *     &#64;Override
 *     paramList<Foo> getParamList() {
 *         if (paramList == null) {
 *             paramList = ParamList.forClass(Foo.class)
 *                                  .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
 *                                  .withParam("name", Foo::getName, String.class)
 *                                  .withParam("bar", Foo::getBar, Bar.class)
 *                                  .andThatsIt();
 *         }
 *         return paramList;
 *     }
 *
 *     public Foo(int id, String name, Bar bar) {
 *         this.id = id;
 *         this.name = name;
 *         this.bar = bar;
 *     }
 *
 *     public int getId() {
 *         return id;
 *     }
 *
 *     public String getName() {
 *         return name;
 *     }
 *
 *     public Bar getBar() {
 *         return bar;
 *     }
 * }
 * }</pre>
 *
 * @param <B>  the type of object being extended.
 * @see EasyOverriderPreventingRecursiveToString
 */
public abstract class EasyOverrider<B> {

    /**
     * A method that returns this class's ParamList.<br>
     *
     * It's best practice to do something like this:
     * <pre>
     * {@code
     *
     * private static ParamList<Foo> paramList = null;
     * static paramList<Foo> getFooParamList() {
     *     if (paramList == null) {
     *         paramList = ParamList.forClass(Foo.class)
     *                              .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY)
     *                              .withParam("name", Foo::getName)
     *                              .withParam("bar", Foo::getBar)
     *                              .andThatsIt();
     *     }
     *     return paramList;
     * }
     *
     *@literal @Override
     * ParamList<Foo> getParamList() {
     *     return getFooParamList();
     * }
     * }
     * </pre>
     *
     * @return A {@link ParamList} for the extending class.
     */
    abstract ParamList<B> getParamList();

    /**
     * Casts this into a B as expected by the ParamList methods.
     *
     * @return A B.
     */
    @SuppressWarnings("unchecked")
    B getThis() {
        return (B)this;
    }

    /**
     * An EasyOverrider version of equals(Object).
     *
     * @param obj  the object to compare this object to
     * @return True if the provided object equals this object as defined by the {@link ParamList}. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getParamList().equals(getThis(), obj);
    }

    /**
     * An EasyOverrider version of hashCode().
     *
     * @return The hash code int for this object as defined by the {@link ParamList}.
     */
    @Override
    public int hashCode() {
        return getParamList().hashCode(getThis());
    }

    /**
     * An EasyOverrider version of toString().
     *
     * @return The string representation of this object as defined by the {@link ParamList}.
     */
    @Override
    public String toString() {
        return getParamList().toString(getThis());
    }
}
