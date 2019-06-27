package EasyOverrider;

import java.util.Map;
import java.util.Set;

/**
 * An abstract class that extends EasyOverrider and implements RecursionPreventingToString.<br>
 *
 * The {@link EasyOverrider} abstract class provides the equals and hashCode override methods.
 * This abstract class provides an override method for <code>paramValueToString()</code> that calls <code>this.paramValueToString(false)</code>.
 * It also implements the <code>paramValueToString(boolean)</code> method required by {@link RecursionPreventingToString}
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
 *     private static ParamList<Foo> paramList =
 *                         ParamList.forClass(Foo.class)
 *                                  .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
 *                                  .withParam("name", Foo::getName, String.class)
 *                                  .withCollection("foos", Foo::getFoos, Foo::paramValueToString, List.class, Foo.class)
 *                                  .andThatsIt();
 *
 *     &#64;Override
 *     paramList<Foo> getParamList() {
 *         return paramList;
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
        return this.toString(null);
    }

    /**
     * An EasyOverrider version of toString(Map) that prevents recursion.<br>
     *
     * @param seen  the map of class to sets of hashCodes of objects that have already been toString-ified.
     * @return The string representation of this object as defined by a {@link ParamList}.
     * @see ParamList#toString(Object, Map)
     */
    @Override
    public String toString(final Map<Class, Set<Integer>> seen) {
        return getParamList().toString(getThis(), seen);
    }
}
