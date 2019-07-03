package EasyOverrider;

/**
 * An abstract class that overrides the equals, hashCode and toString methods using a ParamList.<br>
 *
 * The extender is required to implement the {@link #getParamList()} method.<br>
 *
 * Example class:
 * <pre>{@code
 *
 * import Bar;
 * import EasyOverrider.EasyOverriderMethods;
 * import EasyOverrider.ParamList;
 *
 * public class Foo extends EasyOverriderMethods<Foo> {
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
 *                                  .withPrimaryParam("id", Foo::getId, Integer.class)
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
 */
public abstract class EasyOverriderMethods<B> implements EasyOverrider<B> {

    /**
     * An EasyOverriderMethods version of equals(Object).
     *
     * @param obj  the object to compare this object to
     * @return True if the provided object equals this object as defined by the {@link ParamList}. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return getParamList().equals(getThis(), obj);
    }

    /**
     * An EasyOverriderMethods version of hashCode().
     *
     * @return The hash code int for this object as defined by the {@link ParamList}.
     */
    @Override
    public int hashCode() {
        return getParamList().hashCode(getThis());
    }

    /**
     * An EasyOverriderMethods version of toString().
     *
     * @return The string representation of this object as defined by the {@link ParamList}.
     */
    @Override
    public String toString() {
        return getParamList().toString(getThis());
    }
}
