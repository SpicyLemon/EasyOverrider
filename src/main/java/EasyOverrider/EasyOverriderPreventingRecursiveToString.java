package EasyOverrider;

import java.util.Map;
import java.util.Set;

/**
 * An abstract class that extends EasyOverrider and implements RecursionPreventingToString.<br>
 *
 * The {@link EasyOverrider} abstract class provides the equals and hashCode override methods.
 * This abstract classs implements the <code>toString(Map)</code> method required by {@link RecursionPreventingToString}
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
 * public class Bar extends EasyOverriderPreventingRecursiveToString<Bar> {
 *     private int id;
 *     private String name;
 *     private Bar bar;
 *     private List<Bar> bars;
 *
 *     private static ParamList<Bar> paramList = null;
 *
 *     &#64;Override
 *     paramList<Bar> getParamList() {
 *         if (paramList == null) {
 *             paramList = ParamList.forClass(Foo.class)
 *                                  .withParam("id", Foo::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
 *                                  .withParam("name", Foo::getName, String.class)
 *                                  .withParam("bar", Foo::getBar, Bar.class)
 *                                  .withCollection("foos", Foo::getBars, List.class, Bar.class)
 *                                  .andThatsIt();
 *         }
 *         return paramList;
 *     }
 *
 *     public Foo(int id, String name, Bar bar, List<Bar> bars) {
 *         this.id = id;
 *         this.name = name;
 *         this.bar = bar;
 *         this.bars = bars;
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
 *
 *     public List<Bar> getFoos() {
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
