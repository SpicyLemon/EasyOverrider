package EasyOverrider;

import java.util.Map;
import java.util.Set;

/**
 * An abstract class that extends EasyOverriderMethods and implements EasyOverrider.<br>
 *
 * The {@link EasyOverriderMethods} abstract class provides the equals and hashCode override methods.
 * This abstract class implements the <code>toString(Map)</code> method required by {@link EasyOverrider}
 * utilizing the extending class' {@link #getParamList()} method.<br>
 *
 * The extender is required to implement the {@link #getParamList()} method.<br>
 *
 * Example class:
 * <pre>
 * {@code
 *
 * import static EasyOverriderMethods.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;
 *
 * import EasyOverriderMethods.EasyOverriderMethods;
 * import EasyOverriderMethods.ParamList;
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
 *             paramList = ParamList.forClass(Bar.class)
 *                                  .withParam("id", Bar::getId, INCLUDED_IN_TOSTRING_ONLY, Integer.class)
 *                                  .withParam("name", Bar::getName, String.class)
 *                                  .withParam("bar", Bar::getBar, Bar.class)
 *                                  .withCollection("bars", Bar::getBars, List.class, Bar.class)
 *                                  .andThatsIt();
 *         }
 *         return paramList;
 *     }
 *
 *     public Bar(int id, String name, Bar bar, List<Bar> bars) {
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
 *     public List<Bar> getBars() {
 *         return bars;
 *     }
 * }
 * }
 * </pre>
 *
 * @param <B>  the type of object being extended.
 * @see EasyOverriderMethods
 * @see EasyOverrider
 */
public abstract class EasyOverriderPreventingRecursiveToString<B> extends EasyOverriderMethods<B> implements EasyOverrider {

    /**
     * An EasyOverriderMethods version of toString(Map) that prevents recursion.<br>
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
