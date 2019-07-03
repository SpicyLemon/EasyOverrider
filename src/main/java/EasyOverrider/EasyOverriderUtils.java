package EasyOverrider;

import java.util.Optional;
import java.util.function.BiConsumer;

public class EasyOverriderUtils {

    //private empty constructor to prevent instantiation.
    private EasyOverriderUtils() {
        throw new UnsupportedOperationException("EasyOverriderUtils is a static class that should not be instantiated.");
    }

    /**
     * Method used to make sure provided arguments aren't null.<br>
     *
     * If the provided <code>obj</code> is null, an {@link IllegalArgumentException} is thrown.<br>
     *
     * The message of the exception is in the format of
     * <code>"Argument {position} ({paramName}) provided to {methodName} cannot be null."</code><br>
     *
     * @param obj  {@inheritDoc} - cannot be null
     * @param position  {@inheritDoc}
     * @param paramName  {@inheritDoc}
     * @param methodName  {@inheritDoc}
     * @throws IllegalArgumentException if the provided obj is null
     */
    public static void requireNonNull(final Object obj, final int position, final String paramName, final String methodName) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format("Argument %1$s (%2$s) provided to %3$s cannot be null.",
                                                             position, paramName, methodName));
        }
    }

    /**
     * Runs the provided setter if none of the parameters are null.<br>
     *
     * @param obj  the primary object of the setter
     * @param value  the value to set using the setter
     * @param setter  the setter to call
     * @param <O>  the type of the object
     * @param <V>  the type of the value
     */
    public static <O, V> void runSetterIfNotNull(O obj, BiConsumer<O, V> setter, V value) {
        if (obj != null && value != null && setter != null) {
            setter.accept(obj, value);
        }
    }
}
