package EasyOverrider.TestingUtils;

import static org.junit.Assert.assertEquals;

import EasyOverrider.ParamListServiceConfig;
import EasyOverrider.RecursionPreventingToString;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Helpers {

    private static ParamListServiceConfig config;

    //private empty constructor to prevent instantiation.
    private Helpers() {
        throw new UnsupportedOperationException("Helpers is a static class that should not be instantiated.");
    }

    public static ParamListServiceConfig getConfig() {
        if (config == null) {
            config = new ParamListServiceConfig();
            config.setHashCodeToString((i) -> "HASHCODE");
        }
        return config;
    }

    /**
     * Simple test for a setter and getter for an object.<br>
     *
     * It calls the setter with the given value, then asserts that the getter returns that same value.<br>
     *
     * @param paramDescription  the message that will go in the assert so that you can know which parameter test failed
     * @param obj  the object with the getter and setter that you want to test - assumed not null
     * @param setter  the setter to use - assumed not null
     * @param value  the value you want to use to test
     * @param getter  the getter to use - assumed not null
     * @param <O>  the type of object you're testing
     * @param <P>  the type of the parameter (value) you're testing
     */
    public static <O, P> void testSetterGetter(String paramDescription, O obj, BiConsumer<O, P> setter,
                                               P value, Function<O, P> getter) {
        setter.accept(obj, value);
        P actual = getter.apply(obj);
        assertEquals(paramDescription, value, actual);
    }

    public static <P> String objectToString(final P obj, final Class<P> objClass, final Map<Class, Set<Integer>> seen) {
        ParamListServiceConfig config = getConfig();
        if (obj == null) {
            return config.getStringForNull();
        }
        if (!RecursionPreventingToString.class.isAssignableFrom(objClass)) {
            return obj.toString();
        }
        int objHashCode = obj.hashCode();
        RecursionPreventingToString recursiveObject = (RecursionPreventingToString)obj;
        if (!seen.containsKey(objClass)) {
            seen.put(objClass, new HashSet<>());
        }
        if (!seen.get(objClass).contains(objHashCode)) {
            seen.get(objClass).add(objHashCode);
            return recursiveObject.toString(seen);
        }
        return Optional.ofNullable(recursiveObject.primaryToString())
                       .orElseGet(() -> String.format(config.getToStringFormat(),
                                                      config.getClassNameGetter().apply(objClass),
                                                      config.getHashCodeToString().apply(obj.hashCode()),
                                                      config.getStringForRecursionPrevented()));
    }

}
