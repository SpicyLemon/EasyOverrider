package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_TOSTRING;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
public class TestParamDescriptionMap {
    private ParamDescriptionMap<TestObj, String, Integer, ?> getParamDescription(String name, ParamMethodRestriction pmr,
                                                                                 BiFunction<Integer, Boolean, String> recursionPreventingToString) {
        ParamDescriptionMap<TestObj, String, Integer, ?> retval =
                        new ParamDescriptionMap<>(
                                        TestObj.class, Map.class, String.class, Integer.class, name,
                                        TestObj::getTheMapStringInt, pmr, recursionPreventingToString);
        return retval;
    }

    @Test
    public void isCollection_something_false() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        assertFalse(paramDescriptionMap.isCollection());
    }

    @Test
    public void isMap_something_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        assertTrue(paramDescriptionMap.isMap());
    }

    @Test
    public void equals_sameObject_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        assertTrue(paramDescriptionMap.equals(paramDescriptionMap));
    }

    @Test
    public void equals_sameConstructorParameters_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        assertTrue("1.equals(2)", paramDescriptionMap1.equals(paramDescriptionMap2));
        assertTrue("2.equals(1)", paramDescriptionMap2.equals(paramDescriptionMap1));
    }

    @Test
    public void equals_sameConstructorParametersExceptRecursionPreventingToString_true() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap1 =
                        new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, "theTestObj",
                                                     TestObj::getTheMapStringTestObj, INCLUDED_IN_TOSTRING_ONLY, TestObj::toString);
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap2 =
                        new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, "theTestObj",
                                                     TestObj::getTheMapStringTestObj, INCLUDED_IN_TOSTRING_ONLY, null);
        assertTrue("1.equals(2)", paramDescriptionMap1.equals(paramDescriptionMap2));
        assertTrue("2.equals(1)", paramDescriptionMap2.equals(paramDescriptionMap1));
    }

    @Test
    public void equals_sameConstructorParametersExceptNames_false() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamDescription("theInt1", INCLUDED_IN_ALL, null);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamDescription("theInt2", INCLUDED_IN_ALL, null);
        assertFalse("1.equals(2)", paramDescriptionMap1.equals(paramDescriptionMap2));
        assertFalse("2.equals(1)", paramDescriptionMap2.equals(paramDescriptionMap1));
    }

    @Test
    public void equals_sameConstructorParametersExceptParamMethodRestriction_false() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamDescription("theInt1", INCLUDED_IN_ALL, null);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamDescription("theInt2", IGNORED_FOR_TOSTRING, null);
        assertFalse("1.equals(2)", paramDescriptionMap1.equals(paramDescriptionMap2));
        assertFalse("2.equals(1)", paramDescriptionMap2.equals(paramDescriptionMap1));
    }

    @Test
    public void hashCode_runTwice_same() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        int actual = paramDescriptionMap.hashCode();
        int expected = paramDescriptionMap.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void hashCode_sameConstructorParameters_same() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamDescription("theInt", IGNORED_FOR_TOSTRING, null);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamDescription("theInt", IGNORED_FOR_TOSTRING, null);
        assertEquals(paramDescriptionMap1.hashCode(), paramDescriptionMap2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptRecursionPreventingToString_same() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap1 =
                        new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, "theMapStringTestObj",
                                                  TestObj::getTheMapStringTestObj, INCLUDED_IN_TOSTRING_ONLY, TestObj::toString);
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap2 =
                        new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, "theMapStringTestObj",
                                                  TestObj::getTheMapStringTestObj, INCLUDED_IN_TOSTRING_ONLY, null);
        assertEquals(paramDescriptionMap1.hashCode(), paramDescriptionMap2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptNames_different() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamDescription("theInt1", IGNORED_FOR_TOSTRING, null);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamDescription("theInt2", IGNORED_FOR_TOSTRING, null);
        assertNotEquals(paramDescriptionMap1.hashCode(), paramDescriptionMap2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParamMethodRestriction_different() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamDescription("theInt", IGNORED_FOR_TOSTRING, null);
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap2 =
                        getParamDescription("theInt", INCLUDED_IN_TOSTRING_ONLY, null);
        assertNotEquals(paramDescriptionMap1.hashCode(), paramDescriptionMap2.hashCode());
    }

    @Test
    public void toString_stringParam_containsParamDescriptionMap() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        assertTrue(paramDescriptionMap1.toString().contains("ParamDescriptionMap"));
    }

    @Test
    public void toString_stringParam_containsParentClass() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionMap1.toString();
        assertTrue(actual, actual.contains("parentClass"));
    }

    @Test
    public void toString_stringParam_containsParentClassName() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap1 =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionMap1.toString();
        assertTrue(actual, actual.contains("TestObj"));
    }

    @Test
    public void toString_stringParam_containsParamClass() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionMap.toString();
        assertTrue(actual, actual.contains("Map"));
    }

    @Test
    public void toString_stringParam_containsParamName() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionMap.toString();
        assertTrue(actual, actual.contains("theInt"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestriction() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionMap.toString();
        assertTrue(actual, actual.contains("paramMethodRestriction"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestrictionValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionMap.toString();
        assertTrue(actual, actual.contains("INCLUDED_IN_ALL"));
    }

    @Test
    public void toString_stringParam_containsRecursionPreventingToString() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionMap.toString();
        assertTrue(actual, actual.contains("recursionPreventingToString"));
    }

    @Test
    public void getParentClass_testObj_returnsCorrectValue() {
        Class<TestObj> expected = TestObj.class;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        Class<TestObj> actual = paramDescriptionMap.getParentClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamClass_string_returnsCorrectValue() {
        Class<? extends Map> expected = Map.class;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        Class<? extends Map> actual = paramDescriptionMap.getParamClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getEntryClass_string_returnsCorrectValue() {
        Class<Integer> expected = Integer.class;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        Class<Integer> actual = paramDescriptionMap.getEntryClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getKeyClass_string_returnsCorrectValue() {
        Class<String> expected = String.class;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        Class<String> actual = paramDescriptionMap.getKeyClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getName_string_returnsCorrectValue() {
        String expected = "myCustomStringNameJustForThisTest";
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription(expected, INCLUDED_IN_ALL, null);
        String actual = paramDescriptionMap.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamMethodRestriction_includedInHashCodeOnly_returnsCorrectValue() {
        ParamMethodRestriction expected = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", expected, null);
        ParamMethodRestriction actual = paramDescriptionMap.getParamMethodRestriction();
        assertEquals(expected, actual);
    }

    @Test
    public void getRecursionPreventingToString_toString_returnsCorrectValue() {
        BiFunction<? super TestObj, Boolean, String> expected = TestObj::toString;
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, "theMapStringTestObj",
                                                  TestObj::getTheMapStringTestObj, INCLUDED_IN_ALL, expected);
        BiFunction<? super TestObj, Boolean, String> actual = paramDescriptionMap.getRecursionPreventingToString();
        assertEquals(actual, expected);
    }

    @Test
    public void isEqualsIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsIgnore();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamDescription("theInt", pmr, null);
            boolean actual = paramDescriptionMap.isEqualsIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isEqualsInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsInclude();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamDescription("theInt", pmr, null);
            boolean actual = paramDescriptionMap.isEqualsInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeIgnore();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamDescription("theInt", pmr, null);
            boolean actual = paramDescriptionMap.isHashCodeIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeInclude();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamDescription("theInt", pmr, null);
            boolean actual = paramDescriptionMap.isHashCodeInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringIgnore();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamDescription("theInt", pmr, null);
            boolean actual = paramDescriptionMap.isToStringIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringInclude();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamDescription("theInt", pmr, null);
            boolean actual = paramDescriptionMap.isToStringInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void get_singleEntry_matchesObjectValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Map<String, Integer> expected = new HashMap<>();
        expected.put("five", 5);
        testObj.setTheMapStringInt(expected);
        Map<String, Integer> actual = paramDescriptionMap.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void get_nullObj_blowsUp() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            Map<String, Integer> actual = paramDescriptionMap.get(testObj);
            fail("IllegalArgumentException should have been thrown calling get(null).");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void safeGet_stringValue_matchesObjectValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Map<String, Integer> expected = new HashMap<>();
        expected.put("not five", 69);
        testObj.setTheMapStringInt(expected);
        Map<String, Integer> actual = paramDescriptionMap.safeGet(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void safeGet_nullObj_returnsNull() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        Integer expected = null;
        Map<String, Integer> actual = paramDescriptionMap.safeGet(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void paramsAreEqual_nullNull_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj1 = null;
        TestObj testObj2 = null;
        assertTrue("1, 2", paramDescriptionMap.paramsAreEqual(testObj1, testObj2));
        assertTrue("2, 1", paramDescriptionMap.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_nullVsNullParam_false() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj1 = null;
        TestObj testObj2 = new TestObj();
        testObj2.setTheString(null);
        assertFalse("1, 2", paramDescriptionMap.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionMap.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_same_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Map<String, Integer> map = new HashMap<>();
        map.put("five", 5);
        map.put("six", 6);
        testObj.setTheMapStringInt(map);
        assertTrue(paramDescriptionMap.paramsAreEqual(testObj, testObj));
    }

    @Test
    public void paramsAreEqual_differentObjectsSameValue_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj1 = new TestObj();
        TestObj testObj2 = new TestObj();
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("five", 5);
        map1.put("six", 6);
        testObj1.setTheMapStringInt(map1);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("five", 5);
        map2.put("six", 6);
        testObj2.setTheMapStringInt(map2);
        assertTrue(paramDescriptionMap.paramsAreEqual(testObj1, testObj2));
    }

    @Test
    public void paramsAreEqual_differentObjectsDifferentValues_false() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj1 = new TestObj();
        TestObj testObj2 = new TestObj();
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("five", 5);
        map1.put("six", 6);
        testObj1.setTheMapStringInt(map1);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("five", 5);
        map2.put("eight", 8);
        testObj2.setTheMapStringInt(map2);
        assertFalse("1, 2", paramDescriptionMap.paramsAreEqual(testObj1, testObj2));
        assertFalse("1, 2", paramDescriptionMap.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_differentObjectsOneNullValue_false() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj1 = new TestObj();
        TestObj testObj2 = new TestObj();
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("five", 5);
        map1.put("six", 6);
        testObj1.setTheMapStringInt(map1);
        testObj2.setTheMapStringInt(null);
        assertFalse("1, 2", paramDescriptionMap.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionMap.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void toString_object_returnsExpectedValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Map<String, Integer> map = new HashMap<>();
        map.put("ten", 10);
        testObj.setTheMapStringInt(map);
        assertEquals(map.toString(), paramDescriptionMap.toString(testObj));
    }

    @Test
    public void toString_nullObject_blowsUP() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.toString(testObj);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void toString_objectFalse_returnsExpectedValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Map<String, Integer> map = new HashMap<>();
        map.put("ten", 10);
        testObj.setTheMapStringInt(map);
        assertEquals(map.toString(), paramDescriptionMap.toString(testObj, false));
    }

    @Test
    public void toString_nullObjectFalse_blowsUP() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.toString(testObj, false);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void toString_objectTrue_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, "theMapStringTestObj",
                                                  TestObj::getTheMapStringTestObj, INCLUDED_IN_ALL, TestObj::toString);
        String expected = "...";
        TestObj testObj = new TestObj();
        Map<String, TestObj> map = new HashMap<>();
        map.put("testObj", testObj);
        testObj.setTheMapStringTestObj(map);
        assertEquals(expected, paramDescriptionMap.toString(testObj, true));
    }

    @Test
    public void toString_nullObjectTrue_blowsUP() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theInt", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.toString(testObj, true);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_object_returnsExpectedValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theMapStringInt", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Map<String, Integer> map = new HashMap<>();
        map.put("ten", 10);
        testObj.setTheMapStringInt(map);
        String expected = "theMapStringInt='" + map.toString() + "'";
        assertEquals(expected, paramDescriptionMap.getNameValueString(testObj));
    }

    @Test
    public void getNameValueString_objectWithNull_returnsExpectedValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theMapStringInt", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        testObj.setTheMapStringInt(null);
        String expected = "theMapStringInt=null";
        assertEquals(expected, paramDescriptionMap.getNameValueString(testObj));
    }

    @Test
    public void getNameValueString_nullObject_blowsUP() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theMapStringInt", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.getNameValueString(testObj);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectFalse_returnsExpectedValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theMapStringInt", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Map<String, Integer> map = new HashMap<>();
        map.put("ten", 311);
        testObj.setTheMapStringInt(map);
        String expected = "theMapStringInt='" + map.toString() + "'";
        assertEquals(expected, paramDescriptionMap.getNameValueString(testObj, false));
    }

    @Test
    public void getNameValueString_objectWithNullFalse_returnsExpectedValue() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theMapStringInt", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        testObj.setTheMapStringInt(null);
        String expected = "theMapStringInt=null";
        assertEquals(expected, paramDescriptionMap.getNameValueString(testObj, false));
    }

    @Test
    public void getNameValueString_nullObjectFalse_blowsUP() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamDescription("theMapStringInt", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.getNameValueString(testObj, false);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectTrue_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, "theMapStringTestObj",
                                                  TestObj::getTheMapStringTestObj, INCLUDED_IN_ALL, TestObj::toString);
        TestObj testObj = new TestObj();
        Map<String, TestObj> map = new HashMap<>();
        map.put("whatever", testObj);
        testObj.setTheMapStringTestObj(map);
        String expected = "theMapStringTestObj=...";
        assertEquals(expected, paramDescriptionMap.getNameValueString(testObj, true));
    }

    @Test
    public void getNameValueString_objectWithNullTrue_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, "theMapStringTestObj",
                                                  TestObj::getTheMapStringTestObj, INCLUDED_IN_ALL, TestObj::toString);
        TestObj testObj = new TestObj();
        testObj.setTheMapStringTestObj(null);
        String expected = "theMapStringTestObj=null";
        assertEquals(expected, paramDescriptionMap.getNameValueString(testObj, true));
    }

    @Test
    public void getNameValueString_nullObjectTrue_blowsUP() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, "theMapStringTestObj",
                                                  TestObj::getTheMapStringTestObj, INCLUDED_IN_ALL, TestObj::toString);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.getNameValueString(testObj, true);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }
}
