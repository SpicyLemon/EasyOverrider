package EasyOverrider;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface EasyOverriderService {

    String getStringForNull();

    void setStringForNull(String stringForNull);

    String getStringForRecursionPrevented();

    void setStringForRecursionPrevented(String stringForRecursionPrevented);

    String getStringForEmptyParamList();

    void setStringForEmptyParamList(String stringForEmptyParamList);

    String getParameterDelimiter();

    void setParameterDelimiter(String stringForEmptyParamList);

    String getNameValueFormat();

    void setNameValueFormat(String nameValueFormat);

    String getParameterValueFormat();

    void setParameterValueFormat(String parameterValueFormat);

    String getToStringFormat();

    Function<Class, String> getClassNameGetter();

    void setClassNameGetter(Function<Class, String> classNameGetter);

    void setToStringFormat(String toStringFormat);

    <O, P> P get(final O obj, final Function<? super O, P> getter, String name);

    <O, P> P safeGet(final O obj, final Function<? super O, P> getter);

    <O, P> boolean paramsAreEqual(final O thisO, final O thatO, final Function<? super O, P> getter, String name);

    <O, P> String paramValueToString(O obj, Function<? super O, P> getter, Map<Class, Set<Integer>> seen,
                                     BiFunction<P, Map<Class, Set<Integer>>, String> valueToStringPreventingRecursion);

    <O> String objectToStringPreventingRecursion(final Class<O> parentClass, final O object, final Map<Class, Set<Integer>> seen);

    <O, P> String getNameValueString(final O obj, String name, final Function<? super O, P> getter,
                                     final Map<Class, Set<Integer>> seen,
                                     BiFunction<P, Map<Class, Set<Integer>>, String> valueToStringPreventingRecursion);

    <P> String valueToStringPreventingRecursionSingle(final P value, final Map<Class, Set<Integer>> seen, Class<P> paramClass);

    <E, P extends Collection<? extends E>> String valueToStringPreventingRecursionCollection(
                    final P value, final Map<Class, Set<Integer>> seen, Class<E> entryClass);

    <K, V, P extends Map<? extends K, ? extends V>> String valueToStringPreventingRecursionMap(
                    final P value, final Map<Class, Set<Integer>> seen, Class<K> keyClass, Class<V> valueClass);

    <O> void validateParamListConstructorOrThrow(final Class<O> parentClass,
                                                 final Map<String, ParamDescription<? super O, ?>> paramDescriptionMap,
                                                 final List<String> paramOrder, final EasyOverriderService easyOverriderService);

    <O> String getParamsString(final O thisObj, final Map<Class, Set<Integer>> seen, final List<String> paramOrder,
                               Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    <O> List<ParamDescription<? super O, ?>> getAllParamDescriptions(final List<String> paramOrder,
                                                                     Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    <O> List<ParamDescription<? super O, ?>> getEqualsParamDescriptions(final List<String> paramOrder,
                                                                        Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    <O> List<ParamDescription<? super O, ?>> getHashCodeParamDescriptions(final List<String> paramOrder,
                                                                          Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    <O> List<ParamDescription<? super O, ?>> getToStringParamDescriptions(final List<String> paramOrder,
                                                                          Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    <O> boolean equals(final Object thisObj, final Object thatObj, Class<O> parentClass, final List<String> paramOrder,
                       Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    <O> int hashCode(final O thisObj, final List<String> paramOrder, Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    <O> String toString(final O thisObj, final Map<Class, Set<Integer>> seen, Class<O> parentClass,
                        final List<String> paramOrder, Map<String, ParamDescription<? super O, ?>> paramDescriptionMap);

    void requireNonNull(final Object obj, final int position, final String paramName, final String methodName);
}
