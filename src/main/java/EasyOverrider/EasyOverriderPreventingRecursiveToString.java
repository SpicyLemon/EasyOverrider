package EasyOverrider;

/**
 * An abstract class that extends EasyOverrider and implements RecursionPreventingToString.<br>
 *
 * The {@link EasyOverrider} abstract class provides the equals and hashCode override methods.
 * This abstract class provides an override method for <code>toString()</code> that calls <code>this.toString(false)</code>.
 * It also implements the <code>toString(boolean)</code> method required by {@link RecursionPreventingToString}
 * utilizing the extending class' {@link ParamList}.<br>
 *
 * The extender is required to implement the {@link #getParamList()} method.<br>
 *
 * Example:
 * <pre>
 * {@code
 *
 * import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;
 *
 * import EasyOverrider.EasyOverrider;
 * import EasyOverrider.ParamList;
 * import java.util.List;
 *
 * public class Foo extends EasyOverriderPreventingRecursiveToString<Foo> {
 *     private int id;
 *     private String name;
 *     private List<Foo> foos;
 *
 *     private static ParamList<Foo> paramList = null;
 *
 *     static paramList<Foo> getFooParamList() {
 *         if (paramList == null) {
 *             paramList = ParamList.forClass(Foo.class)
 *                                  .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
 *                                  .withParam("name", Foo::getName, String.class)
 *                                  .withCollection("foos", Foo::getFoos, Foo::toString, List.class, Foo.class)
 *                                  .andThatsIt();
 *         }
 *         return paramList;
 *     }
 *
 *     &#64;Override
 *     paramList<Foo> getParamList() {
 *         return getFooParamList();
 *     }
 *
 *     public Foo(int id, String name, List<Foo> foos) {
 *         this.id = id;
 *         this.name = name;
 *         this.foos = foos;
 *     }
 *
 *     public int getId() {
 *         return id;
 *     }
 *
 *     public int getName() {
 *         return name;
 *     }
 *
 *     public int getFoos() {
 *         return foos;
 *     }
 * }
 * }
 * </pre>
 *
 * @param <B>  the type of object being extended.
 * @see EasyOverrider
 */
public abstract class EasyOverriderPreventingRecursiveToString<B> extends EasyOverrider<B> implements RecursionPreventingToString {

    /**
     * Casts this into a B as expected by the ParamList methods.
     * @return A B.
     */
    @SuppressWarnings("unchecked")
    private B getThis() {
        return (B)this;
    }

    /**
     * An EasyOverrider version of toString() that prevents recursion.
     *
     * @return The string representation of this object as defined by the {@link ParamList}.
     */
    @Override
    public String toString() {
        return this.toString(false);
    }

    /**
     * An EasyOverrider version of toString(boolean) that prevents recrsion.<br>
     *
     * Basically, if the provided flag is false, then toString behaves mostly normally.
     * That is, it will include all the values of this object.
     * However, when it gets the string values of those objects, and they have a similar toString(boolean), that method is called
     * providing a true flag.  If the provided flag is true, then for parameters that have a toString(boolean) function,
     * a value of "..." is used instead of calling its toString method.
     *
     * @param preventingRecursion  the flag for whether or not we're trying to prevent recursion
     * @return The string representation of this object as defined by the {@link ParamList}.
     * @see ParamList#toString(Object, boolean)
     */
    @Override
    public String toString(boolean preventingRecursion) {
        return getParamList().toString(getThis(), preventingRecursion);
    }
}
