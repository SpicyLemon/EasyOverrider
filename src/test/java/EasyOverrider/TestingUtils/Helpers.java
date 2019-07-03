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
     * Tests a setter and getter for an object.
     * I.e., it calls the setter with the given value, then asserts that the getter returns that same value.
     * @param paramDescription - The message that will go in the assert so that you can know which parameter test failed.
     * @param obj - The object with the getter and setter that you want to test.
     * @param value - The value you want to use to test
     * @param setter - The setter to use
     * @param getter - The getter to use
     * @param <O> - The type of object you're testing
     * @param <P> - The type of the parameter (and value) you're testing
     */
    public static <O, P> void testGetterSetter(String paramDescription, O obj, P value,
                                               BiConsumer<O, P> setter,
                                               Function<O, P> getter) {
        setter.accept(obj, value);
        assertEquals(paramDescription, value, getter.apply(obj));
    }
}
