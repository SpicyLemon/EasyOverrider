package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_HASHCODE__UNSAFE;
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
import java.util.function.BiFunction;
import java.util.function.Function;

public class TestParamDescriptionSingle {
    @Test
    public void isMap_something_false() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Boolean>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, IGNORED_FOR_ALL);
        assertFalse(paramDescriptionSingle.isMap());
    }

    @Test
    public void isCollection_something_false() {
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theInt",
                                        TestObj::getTheInt, INCLUDED_IN_ALL);
        assertFalse(paramDescriptionSingle.isCollection());
    }

    @Test
    public void equals_sameObject_true() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        assertTrue(paramDescriptionSingle.equals(paramDescriptionSingle));
    }

    @Test
    public void equals_sameConstructorParameters_true() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        assertTrue("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertTrue("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptNames_false() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj1",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj2",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        assertFalse("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertFalse("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptGetter_true() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheOtherString, INCLUDED_IN_ALL);
        assertTrue("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertTrue("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptParameter_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theString",
                                        TestObj::getTheInt, INCLUDED_IN_ALL);
        assertNotEquals("1, 2", paramDescriptionSingle1, paramDescriptionSingle2);
        assertNotEquals("2, 1", paramDescriptionSingle2, paramDescriptionSingle1);
    }

    @Test
    public void equals_sameConstructorParametersExceptParamMethodRestriction_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, IGNORED_FOR_ALL);
        assertFalse("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertFalse("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void hashCode_runTwice_same() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        int actual = paramDescriptionSingle.hashCode();
        int expected = paramDescriptionSingle.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void hashCode_sameConstructorParameters_same() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        assertEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptNames_different() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj1",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj2",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptGetter_same() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheOtherString, INCLUDED_IN_ALL);
        assertEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParameter_different() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theString",
                                        TestObj::getTheInt, INCLUDED_IN_ALL);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParamMethodRestriction_different() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, IGNORED_FOR_ALL);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void toString_stringParam_containsParamDescriptionSingle() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        assertTrue(paramDescriptionSingle1.toString().contains("ParamDescriptionSingle"));
    }

    @Test
    public void toString_stringParam_containsParentClass() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("parentClass"));
    }

    @Test
    public void toString_stringParam_containsParentClassName() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("TestObj"));
    }

    @Test
    public void toString_stringParam_containsParamClass() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("String"));
    }

    @Test
    public void toString_stringParam_containsParamName() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("theString"));
    }

    @Test
    public void toString_stringParam_containsGetter() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("getter"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestriction() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("paramMethodRestriction"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestrictionValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("INCLUDED_IN_ALL"));
    }

    @Test
    public void toString_stringParam_containsRecursionPreventingToString() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("recursionPreventingToString"));
    }

    @Test
    public void getParentClass_testObj_returnsCorrectValue() {
        Class<TestObj> expected = TestObj.class;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        expected, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        Class<TestObj> actual = paramDescriptionSingle1.getParentClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamClass_string_returnsCorrectValue() {
        Class<String> expected = String.class;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, expected, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        Class<String> actual = paramDescriptionSingle1.getParamClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getName_string_returnsCorrectValue() {
        String expected = "myCustomStringNameJustForThisTest";
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, expected,
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String actual = paramDescriptionSingle1.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getGetter_getTheString_returnsCorrectValue() {
        Function<? super TestObj, String> expected = TestObj::getTheString;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        expected, INCLUDED_IN_ALL);
        Function<? super TestObj, String> actual = paramDescriptionSingle1.getGetter();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamMethodRestriction_includedInHashCodeOnly_returnsCorrectValue() {
        ParamMethodRestriction expected = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, expected);
        ParamMethodRestriction actual = paramDescriptionSingle1.getParamMethodRestriction();
        assertEquals(expected, actual);
    }

    @Test
    public void isEqualsIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsIgnore();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString, pmr);
            boolean actual = paramDescriptionSingle.isEqualsIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isEqualsInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsInclude();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString, pmr);
            boolean actual = paramDescriptionSingle.isEqualsInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeIgnore();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString, pmr);
            boolean actual = paramDescriptionSingle.isHashCodeIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeInclude();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString, pmr);
            boolean actual = paramDescriptionSingle.isHashCodeInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringIgnore();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString, pmr);
            boolean actual = paramDescriptionSingle.isToStringIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringInclude();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString, pmr);
            boolean actual = paramDescriptionSingle.isToStringInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void get_stringValue_matchesObjectValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        String expected = "some string or something";
        testObj.setTheString(expected);
        String actual = paramDescriptionSingle.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void get_nullStringValue_matchesObjectValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        String expected = null;
        testObj.setTheString(expected);
        String actual = paramDescriptionSingle.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void get_intValue_matchesObjectValue() {
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theInt",
                                        TestObj::getTheInt, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        int expected = 69;
        testObj.setTheInt(expected);
        int actual = paramDescriptionSingle.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void get_nullObj_blowsUp() {
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theInt",
                                        TestObj::getTheInt, INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            int actual = paramDescriptionSingle.get(testObj);
            fail("IllegalArgumentException should have been thrown calling get(null).");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void safeGet_stringValue_matchesObjectValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        String expected = "some string or something";
        testObj.setTheString(expected);
        String actual = paramDescriptionSingle.safeGet(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void safeGet_nullStringValue_matchesObjectValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        String expected = null;
        testObj.setTheString(expected);
        String actual = paramDescriptionSingle.safeGet(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void safeGet_intValue_matchesObjectValue() {
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theInt",
                                        TestObj::getTheInt, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        int expected = 69;
        testObj.setTheInt(expected);
        int actual = paramDescriptionSingle.safeGet(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void safeGet_nullObj_returnsNull() {
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theInt",
                                        TestObj::getTheInt, INCLUDED_IN_ALL);
        TestObj testObj = null;
        Integer expected = null;
        Integer actual = paramDescriptionSingle.safeGet(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void paramsAreEqual_nullNull_true() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Boolean>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, INCLUDED_IN_ALL);
        TestObj testObj1 = null;
        TestObj testObj2 = null;
        assertTrue("1, 2", paramDescriptionSingle.paramsAreEqual(testObj1, testObj2));
        assertTrue("2, 1", paramDescriptionSingle.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_nullVsNullParam_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        TestObj testObj1 = null;
        TestObj testObj2 = new TestObj();
        testObj2.setTheString(null);
        assertFalse("1, 2", paramDescriptionSingle.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionSingle.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_same_true() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Boolean>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        testObj.setTheBoolean(true);
        assertTrue(paramDescriptionSingle.paramsAreEqual(testObj, testObj));
    }

    @Test
    public void paramsAreEqual_differentObjectsSameValue_true() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Boolean>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, INCLUDED_IN_ALL);
        TestObj testObj1 = new TestObj();
        testObj1.setTheBoolean(true);
        TestObj testObj2 = new TestObj();
        testObj2.setTheBoolean(true);
        assertTrue(paramDescriptionSingle.paramsAreEqual(testObj1, testObj2));
    }

    @Test
    public void paramsAreEqual_differentObjectsDifferentValues_false() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Boolean>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, INCLUDED_IN_ALL);
        TestObj testObj1 = new TestObj();
        testObj1.setTheBoolean(true);
        TestObj testObj2 = new TestObj();
        testObj2.setTheBoolean(false);
        assertFalse("1, 2", paramDescriptionSingle.paramsAreEqual(testObj1, testObj2));
        assertFalse("1, 2", paramDescriptionSingle.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_differentObjectsOneNullValue_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        TestObj testObj1 = new TestObj();
        testObj1.setTheString("food");
        TestObj testObj2 = new TestObj();
        testObj2.setTheString(null);
        assertFalse("1, 2", paramDescriptionSingle.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionSingle.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void toString_objectNull_returnsExpectedValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString2",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        String expected = "theValue2";
        TestObj testObj = new TestObj();
        testObj.setTheString(expected);
        assertEquals(expected, paramDescriptionSingle.toString(testObj, null));
    }

    @Test
    public void toString_nullObjectFalse_blowsUP() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionSingle.toString(testObj, null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void toString_objectTrue_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL);
        String expected = "...";
        TestObj testObj = new TestObj();
        testObj.setTheTestObj(testObj);
        String actual = paramDescriptionSingle.toString(testObj, null);
        assertEquals(expected, actual);
    }

    @Test
    public void toString_nullObjectEmptyMap_blowsUP() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionSingle.toString(testObj, new HashMap<>());
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectNull_returnsExpectedValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString2",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        testObj.setTheString("theValue2");
        String expected = "theString2='theValue2'";
        assertEquals(expected, paramDescriptionSingle.getNameValueString(testObj, null));
    }

    @Test
    public void getNameValueString_objectWithNullNull_returnsExpectedValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString2",
                                        TestObj::getTheString, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        testObj.setTheString(null);
        String expected = "theString2=null";
        assertEquals(expected, paramDescriptionSingle.getNameValueString(testObj, null));
    }

    @Test
    public void getNameValueString_nullObjectNull_blowsUP() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionSingle.getNameValueString(testObj, null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectTrue_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        testObj.setTheTestObj(testObj);
        String expected = "theTestObj=...";
        String actual = paramDescriptionSingle.getNameValueString(testObj, new HashMap<>());
        assertEquals(expected, actual);
    }

    @Test
    public void getNameValueString_objectWithNullNull_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        testObj.setTheTestObj(null);
        String expected = "theTestObj=null";
        assertEquals(expected, paramDescriptionSingle.getNameValueString(testObj, null));
    }

    @Test
    public void getNameValueString_nullObjectTrue_blowsUP() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionSingle.getNameValueString(testObj, null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }
}

