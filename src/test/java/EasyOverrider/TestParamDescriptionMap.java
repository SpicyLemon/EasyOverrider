package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_TOSTRING;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Map;

@SuppressWarnings("unchecked")
public class TestParamDescriptionMap {
    private ParamDescriptionMap<TestObj, String, Integer, ?> getParamMapStringInteger(String name, ParamMethodRestriction pmr) {
        ParamDescriptionMap<TestObj, String, Integer, ?> retval =
                        new ParamDescriptionMap<>(
                                        TestObj.class, Map.class, String.class, Integer.class, name,
                                        TestObj::getTheMapStringInt, pmr);
        return retval;
    }

    private ParamDescriptionMap<TestObj, String, TestObj, ?> getParamMapStringTestObj(String name, ParamMethodRestriction pmr) {
        ParamDescriptionMap<TestObj, String, TestObj, ?> retval =
                        new ParamDescriptionMap<>(
                                        TestObj.class, Map.class, String.class, TestObj.class, name,
                                        TestObj::getTheMapStringTestObj, pmr);
        return retval;
    }

    //TODO: Clean up and make sure there's enough test coverage.

    @Test
    public void equals_sameObject_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        assertTrue(paramDescriptionMap.equals(paramDescriptionMap));
    }

    @Test
    public void equals_sameConstructorParameters_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        assertTrue("1.equals(2)", paramDescriptionMap1.equals(paramDescriptionMap2));
        assertTrue("2.equals(1)", paramDescriptionMap2.equals(paramDescriptionMap1));
    }

    @Test
    public void equals_sameConstructorParametersExceptNames_false() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamMapStringInteger("theInt1", INCLUDED_IN_ALL);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamMapStringInteger("theInt2", INCLUDED_IN_ALL);
        assertFalse("1.equals(2)", paramDescriptionMap1.equals(paramDescriptionMap2));
        assertFalse("2.equals(1)", paramDescriptionMap2.equals(paramDescriptionMap1));
    }

    @Test
    public void equals_sameConstructorParametersExceptParamMethodRestriction_false() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamMapStringInteger("theInt1", INCLUDED_IN_ALL);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamMapStringInteger("theInt2", IGNORED_FOR_TOSTRING);
        assertFalse("1.equals(2)", paramDescriptionMap1.equals(paramDescriptionMap2));
        assertFalse("2.equals(1)", paramDescriptionMap2.equals(paramDescriptionMap1));
    }

    @Test
    public void hashCode_runTwice_same() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        int actual = paramDescriptionMap.hashCode();
        int expected = paramDescriptionMap.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void hashCode_sameConstructorParameters_same() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamMapStringInteger("theInt", IGNORED_FOR_TOSTRING);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamMapStringInteger("theInt", IGNORED_FOR_TOSTRING);
        assertEquals(paramDescriptionMap1.hashCode(), paramDescriptionMap2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptNames_different() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamMapStringInteger("theInt1", IGNORED_FOR_TOSTRING);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamMapStringInteger("theInt2", IGNORED_FOR_TOSTRING);
        assertNotEquals(paramDescriptionMap1.hashCode(), paramDescriptionMap2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParamMethodRestriction_different() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamMapStringInteger("theInt", IGNORED_FOR_TOSTRING);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamMapStringInteger("theInt", INCLUDED_IN_TOSTRING_ONLY);
        assertNotEquals(paramDescriptionMap1.hashCode(), paramDescriptionMap2.hashCode());
    }

    @Test
    public void toString_stringParam_containsParamDescriptionMap() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        assertTrue(paramDescriptionMap1.toString().contains("ParamDescriptionMap"));
    }

    @Test
    public void toString_stringParam_containsParentClass() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String actual = paramDescriptionMap1.toString();
        assertTrue(actual, actual.contains("parentClass"));
    }

    @Test
    public void toString_stringParam_containsParentClassName() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String actual = paramDescriptionMap1.toString();
        assertTrue(actual, actual.contains("TestObj"));
    }

    @Test
    public void toString_stringParam_containsParamClass() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String actual = paramDescriptionMap.toString();
        assertTrue(actual, actual.contains("Map"));
    }

    @Test
    public void toString_stringParam_containsParamName() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String actual = paramDescriptionMap.toString();
        assertTrue(actual, actual.contains("theInt"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestriction() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String actual = paramDescriptionMap.toString();
        assertTrue(actual, actual.contains("paramMethodRestriction"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestrictionValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String actual = paramDescriptionMap.toString();
        assertTrue(actual, actual.contains("INCLUDED_IN_ALL"));
    }

    @Test
    public void getParentClass_testObj_returnsCorrectValue() {
        Class<TestObj> expected = TestObj.class;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        Class<TestObj> actual = paramDescriptionMap.getParentClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamClass_string_returnsCorrectValue() {
        Class<? extends Map> expected = Map.class;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        Class<? extends Map> actual = paramDescriptionMap.getParamClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getEntryClass_string_returnsCorrectValue() {
        Class<Integer> expected = Integer.class;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        Class<Integer> actual = paramDescriptionMap.getValueClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getKeyClass_string_returnsCorrectValue() {
        Class<String> expected = String.class;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        Class<String> actual = paramDescriptionMap.getKeyClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getName_string_returnsCorrectValue() {
        String expected = "myCustomStringNameJustForThisTest";
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger(expected, INCLUDED_IN_ALL);
        String actual = paramDescriptionMap.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamMethodRestriction_includedInHashCodeOnly_returnsCorrectValue() {
        ParamMethodRestriction expected = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", expected);
        ParamMethodRestriction actual = paramDescriptionMap.getParamMethodRestriction();
        assertEquals(expected, actual);
    }

    @Test
    public void isEqualsInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsInclude();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamMapStringInteger("theInt", pmr);
            boolean actual = paramDescriptionMap.isEqualsInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeInclude();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamMapStringInteger("theInt", pmr);
            boolean actual = paramDescriptionMap.isHashCodeInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringInclude();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamMapStringInteger("theInt", pmr);
            boolean actual = paramDescriptionMap.isToStringInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }
}
