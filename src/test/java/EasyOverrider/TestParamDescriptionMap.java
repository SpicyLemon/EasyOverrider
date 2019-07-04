package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_TOSTRING;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;
import static EasyOverrider.TestingUtils.Helpers.getConfig;
import static EasyOverrider.TestingUtils.Helpers.objectToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class TestParamDescriptionMap {
    private ParamDescriptionMap<TestObj, String, Integer, ?> getParamMapStringInteger(String name, ParamMethodRestriction pmr) {
        return new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, Integer.class, name,
                                         TestObj::getTheMapStringInt, pmr);
    }

    private ParamDescriptionMap<TestObj, String, TestObj, ?> getParamMapStringTestObj(String name, ParamMethodRestriction pmr) {
        return new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, TestObj.class, name,
                                         TestObj::getTheMapStringTestObj, pmr);
    }

    public ParamListServiceConfig config;

    @Before
    public void initTestParamDescriptionCollection() {
        if (config == null) {
            config = getConfig();
        }
    }

    @Test
    public void constructor_nullParentClass_boom() {
        try {
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            new ParamDescriptionMap<>(null, Map.class, String.class, Integer.class, "theMapStringInt",
                                                      TestObj::getTheMapStringInt, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 1 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("parentClass"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullParamClass_boom() {
        try {
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            new ParamDescriptionMap<>(TestObj.class, null, String.class, Integer.class, "theMapStringInt",
                                                      TestObj::getTheMapStringInt, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 2 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("paramClass"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullKeyClass_boom() {
        try {
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            new ParamDescriptionMap<>(TestObj.class, Map.class, null, Integer.class, "theMapStringInt",
                                                      TestObj::getTheMapStringInt, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 3 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("keyClass"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullValueClass_boom() {
        try {
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, null, "theMapStringInt",
                                                      TestObj::getTheMapStringInt, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 4 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("valueClass"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullName_boom() {
        try {
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, Integer.class, null,
                                                      TestObj::getTheMapStringInt, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 5 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("name"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullGetter_boom() {
        try {
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, Integer.class, "theMapStringInt",
                                                      null, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 6 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("getter"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullParamMethodRestriction_boom() {
        try {
            ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                            new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, Integer.class, "theMapStringInt",
                                                      TestObj::getTheMapStringInt, null);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 7 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("paramMethodRestriction"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
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
    public void getValueClass_string_returnsCorrectValue() {
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
    @Ignore
    public void getGetter_getMap_returnsCorrectValue() {
        //TODO: Figure out how to test this.
        //See the note on the TestParamDescriptionCollection.getGetter_getCollection_returnsCorrectValue method for the issue here.
        //
        //Function<TestObj, Map> expected = TestObj::getTheMapStringInt;
        //ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
        //                new ParamDescriptionMap<>(TestObj.class, Map.class, String.class, Integer.class, "theMapStringInt",
        //                                          expected, INCLUDED_IN_ALL);
        //Function<TestObj,Map> actual = paramDescriptionMap.getGetter();
        //assertEquals(expected, actual);
        assertTrue(true);
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

    @Test
    public void getParamString_twoEntries_matchesExpected() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String expected = "{one=1, two=2}";
        TestObj testObj = new TestObj();
        testObj.setTheMapStringInt(new HashMap<>());
        testObj.getTheMapStringInt().put("one", 1);
        testObj.getTheMapStringInt().put("two", 2);
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionMap.getParamString(testObj, (p, c) -> objectToString(p, c, seen));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_null_matchesExpected() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String expected = config.getStringForNull();
        TestObj testObj = new TestObj();
        testObj.setTheMapStringInt(null);
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionMap.getParamString(testObj, (p, c) -> objectToString(p, c, seen));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_empty_matchesExpected() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String expected = "{}";
        TestObj testObj = new TestObj();
        testObj.setTheMapStringInt(new HashMap<>());
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionMap.getParamString(testObj, (p, c) -> objectToString(p, c, seen));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_twoEntriesCustomBiFunction_matchesExpected() {
        ParamDescriptionMap<TestObj, String, Integer, ?> paramDescriptionMap =
                        getParamMapStringInteger("theInt", INCLUDED_IN_ALL);
        String expected = "{one(String)=1(Integer), two(String)=2(Integer)}";
        TestObj testObj = new TestObj();
        testObj.setTheMapStringInt(new HashMap<>());
        testObj.getTheMapStringInt().put("one", 1);
        testObj.getTheMapStringInt().put("two", 2);
        String actual = paramDescriptionMap.getParamString(testObj, (p, c) -> p.toString() + "(" + c.getSimpleName() + ")");
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_recursiveEntries_matchesExpected() {
        ParamDescriptionMap<TestObj, String, TestObj, ?> paramDescriptionMap =
                        getParamMapStringTestObj("theStuff", INCLUDED_IN_ALL);
        String expected = "{3333=EasyOverrider.TestObj@HASHCODE [theInt='3'...], " +
                           "2222=EasyOverrider.TestObj@HASHCODE [theBoolean='false', theInt='2', theString='two', theOtherString=null, " +
                           "theCollectionString=null, theMapStringInt=null, theTestObj=null, theCollectionTestObj=null, theMapStringTestObj='" +
                             "{3333=EasyOverrider.TestObj@HASHCODE [theBoolean='false', theInt='3', theString='three', theOtherString=null, " +
                             "theCollectionString=null, theMapStringInt=null, theTestObj=null, theCollectionTestObj=null, theMapStringTestObj='" +
                               "{3333=EasyOverrider.TestObj@HASHCODE [theInt='3'...], " +
                               "2222=EasyOverrider.TestObj@HASHCODE [theInt='2'...]}'], " +
                             "2222=EasyOverrider.TestObj@HASHCODE [theInt='2'...]}']}";
        TestObj testObj1 = new TestObj();
        TestObj testObj2 = new TestObj();
        TestObj testObj3 = new TestObj();
        testObj1.setTheInt(1);
        testObj2.setTheInt(2);
        testObj3.setTheInt(3);
        testObj1.setTheString("one");
        testObj2.setTheString("two");
        testObj3.setTheString("three");
        Map<String, TestObj> map = new HashMap<>();
        map.put("2222", testObj2);
        map.put("3333", testObj3);
        testObj1.setTheMapStringTestObj(map);
        testObj2.setTheMapStringTestObj(map);
        testObj3.setTheMapStringTestObj(map);
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionMap.getParamString(testObj1, (p, c) -> objectToString(p, c, seen));
        assertEquals(expected, actual);
    }

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
}
