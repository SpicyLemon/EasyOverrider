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

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class TestParamDescriptionMap {
    static ParamListService easyOverriderService = null;

    @Before
    public void initTestStuff() {
        if (easyOverriderService == null) {
            easyOverriderService = new ParamListServiceImpl();
        }
    }

    private ParamDescriptionMap<TestObj, String, Integer, ?> getParamMapStringInteger(String name, ParamMethodRestriction pmr) {
        ParamDescriptionMap<TestObj, String, Integer, ?> retval =
                        new ParamDescriptionMap<>(
                                        TestObj.class, Map.class, String.class, Integer.class, name,
                                        TestObj::getTheMapStringInt, pmr, easyOverriderService);
        return retval;
    }

    private ParamDescriptionMap<TestObj, String, TestObj, ?> getParamMapStringTestObj(String name, ParamMethodRestriction pmr) {
        ParamDescriptionMap<TestObj, String, TestObj, ?> retval =
                        new ParamDescriptionMap<>(
                                        TestObj.class, Map.class, String.class, TestObj.class, name,
                                        TestObj::getTheMapStringTestObj, pmr, easyOverriderService);
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
    public void isEqualsIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsIgnore();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamMapStringInteger("theInt", pmr);
            boolean actual = paramDescriptionMap.isEqualsIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
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
    public void isHashCodeIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeIgnore();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamMapStringInteger("theInt", pmr);
            boolean actual = paramDescriptionMap.isHashCodeIgnore();
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
    public void isToStringIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringIgnore();
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            getParamMapStringInteger("theInt", pmr);
            boolean actual = paramDescriptionMap.isToStringIgnore();
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

    @Test
    public void paramsAreEqual_nullNull_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        TestObj testObj1 = null;
        TestObj testObj2 = null;
        assertTrue("1, 2", paramDescriptionMap.paramsAreEqual(testObj1, testObj2));
        assertTrue("2, 1", paramDescriptionMap.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_nullVsNullParam_false() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        TestObj testObj1 = null;
        TestObj testObj2 = new TestObj();
        testObj2.setTheString(null);
        assertFalse("1, 2", paramDescriptionMap.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionMap.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_same_true() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
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
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
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
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
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
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
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
    public void paramValueToString_nullObjectFalse_blowsUP() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.paramValueToString(testObj, null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void paramValueToString_objectEmpty_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        getParamMapStringTestObj("theMapStringTestObj", INCLUDED_IN_ALL);
        String expected = "{testObj=EasyOverriderMethods.TestObj@45cc6af4 [" +
                          "theBoolean='false', " +
                          "theInt='0', " +
                          "theString=null, " +
                          "theOtherString=null, " +
                          "theCollectionString=null, " +
                          "theMapStringInt=null, " +
                          "theTestObj=null, " +
                          "theCollectionTestObj=null, " +
                          "theMapStringTestObj='{testObj=...}']}";
        TestObj testObj = new TestObj();
        Map<String, TestObj> map = new HashMap<>();
        map.put("testObj", testObj);
        testObj.setTheMapStringTestObj(map);
        String actual = paramDescriptionMap.paramValueToString(testObj, new HashMap<>());
        assertEquals(expected, actual);
    }

    @Test
    public void paramValueToString_nullObjectEmptyMap_blowsUP() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.paramValueToString(testObj, new HashMap<>());
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_nullObjectFalse_blowsUP() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theMapStringInt", INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.getNameValueString(testObj, null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectSeen_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        getParamMapStringTestObj("theMapStringTestObj", INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        Map<String, TestObj> map = new HashMap<>();
        map.put("whatever", testObj);
        testObj.setTheMapStringTestObj(map);
        String expected = "theMapStringTestObj='{whatever=...}'";
        Map<Class, Set<Integer>> seen = new HashMap<>();
        seen.put(TestObj.class, new HashSet<>());
        seen.get(TestObj.class).add(testObj.hashCode());
        assertEquals(expected, paramDescriptionMap.getNameValueString(testObj, seen));
    }

    @Test
    public void getNameValueString_objectWithNullSeen_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        getParamMapStringTestObj("theMapStringTestObj", INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        testObj.setTheMapStringTestObj(null);
        String expected = "theMapStringTestObj=null";
        Map<Class, Set<Integer>> seen = new HashMap<>();
        seen.put(TestObj.class, new HashSet<>());
        seen.get(TestObj.class).add(testObj.hashCode());
        assertEquals(expected, paramDescriptionMap.getNameValueString(testObj, seen));
    }

    @Test
    public void getNameValueString_nullObjectTrue_blowsUP() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        getParamMapStringTestObj("theMapStringTestObj", INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionMap.getNameValueString(testObj, new HashMap<>());
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }
}
