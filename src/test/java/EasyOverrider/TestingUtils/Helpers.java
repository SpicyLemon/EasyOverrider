package EasyOverrider.TestingUtils;

import static org.junit.Assert.assertEquals;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Helpers {

    //private empty constructor to prevent instantiation.
    private Helpers() {
        throw new UnsupportedOperationException("Helpers is a static class that should not be instantiated.");
    }

    /**
     * Simple test for a setter and getter for an object.<br>
     *
     * It calls the setter with the given value, then asserts that the getter returns that same value.<br>
     *
     * @param paramDescription  the message that will go in the assert so that you can know which parameter test failed
     * @param obj  the object with the getter and setter that you want to test - assumed not null
     * @param value  the value you want to use to test
     * @param setter  the setter to use - assumed not null
     * @param getter  the getter to use - assumed not null
     * @param <O>  the type of object you're testing
     * @param <P>  the type of the parameter (value) you're testing
     */
    public static <O, P> void testSetterGetter(String paramDescription, O obj, P value,
                                               BiConsumer<O, P> setter,
                                               Function<O, P> getter) {
        setter.accept(obj, value);
        P actual = getter.apply(obj);
        assertEquals(paramDescription, value, actual);
    }
}
