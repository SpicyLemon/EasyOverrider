package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
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
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class TestParamDescriptionSingle {

    public ParamListServiceConfig config;

    @Before
    public void initTestParamDescriptionSingle() {
        if (config == null) {
            config = getConfig();
        }
    }

    @Test
    public void constructor_nullParentClass_throwsException() {
        try {
            ParamDescriptionSingle<TestObj, TestObj>
                            paramDescriptionSingle = new ParamDescriptionSingle<TestObj, TestObj>(
                                            null, TestObj.class, "theTestObj", TestObj::getTheTestObj,
                                            INCLUDED_IN_TOSTRING_ONLY, false);
            fail("Constructor given null parentClass did not throw exception.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 1 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("parentClass"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullParamClass_throwsException() {
        try {
            ParamDescriptionSingle<TestObj, TestObj>
                            paramDescriptionSingle = new ParamDescriptionSingle<TestObj, TestObj>(
                            TestObj.class, null, "theTestObj", TestObj::getTheTestObj,
                            INCLUDED_IN_TOSTRING_ONLY, false);
            fail("Constructor given null parentClass did not throw exception.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 2 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("paramClass"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullParamName_throwsException() {
        try {
            ParamDescriptionSingle<TestObj, TestObj>
                            paramDescriptionSingle = new ParamDescriptionSingle<TestObj, TestObj>(
                            TestObj.class, TestObj.class, null, TestObj::getTheTestObj,
                            INCLUDED_IN_TOSTRING_ONLY, false);
            fail("Constructor given null parentClass did not throw exception.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 3 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("name"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullGetter_throwsException() {
        try {
            ParamDescriptionSingle<TestObj, TestObj>
                            paramDescriptionSingle = new ParamDescriptionSingle<TestObj, TestObj>(
                            TestObj.class, TestObj.class, "theTestObj", null,
                            INCLUDED_IN_TOSTRING_ONLY, false);
            fail("Constructor given null parentClass did not throw exception.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 4 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("getter"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullParamMethodRestriction_throwsException() {
        try {
            ParamDescriptionSingle<TestObj, TestObj>
                            paramDescriptionSingle = new ParamDescriptionSingle<TestObj, TestObj>(
                            TestObj.class, TestObj.class, "theTestObj", TestObj::getTheTestObj,
                            null, false);
            fail("Constructor given null parentClass did not throw exception.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 5 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("paramMethodRestriction"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void getParentClass_testObj_returnsCorrectValue() {
        Class<TestObj> expected = TestObj.class;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        expected, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        Class<TestObj> actual = paramDescriptionSingle1.getParentClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamClass_string_returnsCorrectValue() {
        Class<String> expected = String.class;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, expected, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        Class<String> actual = paramDescriptionSingle1.getParamClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getName_string_returnsCorrectValue() {
        String expected = "myCustomStringNameJustForThisTest";
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, expected,
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getGetter_getTheString_returnsCorrectValue() {
        Function<? super TestObj, String> expected = TestObj::getTheString;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        expected, INCLUDED_IN_ALL, false);
        Function<? super TestObj, String> actual = paramDescriptionSingle1.getGetter();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamMethodRestriction_includedInHashCodeOnly_returnsCorrectValue() {
        ParamMethodRestriction expected = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, expected, false);
        ParamMethodRestriction actual = paramDescriptionSingle1.getParamMethodRestriction();
        assertEquals(expected, actual);
    }

    @Test
    public void isEqualsInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsInclude();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString,
                                                                        pmr, false);
            boolean actual = paramDescriptionSingle.isEqualsInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeInclude();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString,
                                                                        pmr, false);
            boolean actual = paramDescriptionSingle.isHashCodeInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringInclude();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString,
                                                                        pmr, false);
            boolean actual = paramDescriptionSingle.isToStringInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isPrimary_true_returnsCorrectValue() {
        boolean expected = true;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "stuff",
                                        TestObj::getTheString, INCLUDED_IN_ALL, expected);
        boolean actual = paramDescriptionSingle1.isPrimary();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_stringNormal_equalsExpected() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_TOSTRING_ONLY, false);
        String expected = "It's Me!";
        TestObj testObj = new TestObj();
        testObj.setTheString(expected);
        String actual = paramDescriptionSingle.getParamString(testObj, (p, c) -> objectToString(p, c, new HashMap<>(), config));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_nullStringNormal_equalsExpected() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_TOSTRING_ONLY, false);
        String expected = config.getStringForNull();
        TestObj testObj = new TestObj();
        testObj.setTheString(null);
        String actual = paramDescriptionSingle.getParamString(testObj, (p, c) -> objectToString(p, c, new HashMap<>(), config));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_stringCustomMethod_equalsExpected() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_TOSTRING_ONLY, false);
        String expected = "String and foo";
        TestObj testObj = new TestObj();
        testObj.setTheString("foo");
        String actual = paramDescriptionSingle.getParamString(testObj, (p, c) -> c.getSimpleName() + " and " + p);
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_booleanTrueNormal_equalsExpected() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, INCLUDED_IN_TOSTRING_ONLY, false);
        String expected = "true";
        TestObj testObj = new TestObj();
        testObj.setTheBoolean(true);
        String actual = paramDescriptionSingle.getParamString(testObj, (p, c) -> objectToString(p, c, new HashMap<>(), config));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_booleanFalseNormal_equalsExpected() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, INCLUDED_IN_TOSTRING_ONLY, false);
        String expected = "false";
        TestObj testObj = new TestObj();
        testObj.setTheBoolean(false);
        String actual = paramDescriptionSingle.getParamString(testObj, (p, c) -> objectToString(p, c, new HashMap<>(), config));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_int5Normal_equalsExpected() {
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle =
                        new ParamDescriptionSingle<>(
                                        TestObj.class, Integer.class, "theInt",
                                        TestObj::getTheInt, INCLUDED_IN_TOSTRING_ONLY, false);
        String expected = "5";
        TestObj testObj = new TestObj();
        testObj.setTheInt(5);
        String actual = paramDescriptionSingle.getParamString(testObj, (p, c) -> objectToString(p, c, new HashMap<>(), config));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_testObjNormal_equalsExpected() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        String expected = "EasyOverrider.TestObj@HASHCODE [" +
                          "theBoolean='false', theInt='0', theString=null, theOtherString=null, theCollectionString=null, " +
                          "theMapStringInt=null, theTestObj='EasyOverrider.TestObj@HASHCODE [theInt='0'...]', " +
                          "theCollectionTestObj=null, theMapStringTestObj=null]";
        TestObj testObj = new TestObj(config);
        testObj.setTheTestObj(testObj);
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionSingle.getParamString(testObj, (p, c) -> objectToString(p, c, seen, config));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_testObjFourDeep_equalsExpected() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        String expected = "EasyOverrider.TestObj@HASHCODE [theBoolean='false', theInt='1', theString='one', theOtherString=null, " +
                          "theCollectionString=null, theMapStringInt=null, theTestObj='" +
                          "EasyOverrider.TestObj@HASHCODE [theBoolean='false', theInt='2', theString='two', theOtherString=null, " +
                          "theCollectionString=null, theMapStringInt=null, theTestObj='" +
                          "EasyOverrider.TestObj@HASHCODE [theBoolean='false', theInt='3', theString='three', theOtherString=null, " +
                          "theCollectionString=null, theMapStringInt=null, theTestObj='" +
                          "EasyOverrider.TestObj@HASHCODE [theBoolean='false', theInt='4', theString='four', theOtherString=null, " +
                          "theCollectionString=null, theMapStringInt=null, theTestObj='" +
                          "EasyOverrider.TestObj@HASHCODE [theInt='1'...]', " +
                          "theCollectionTestObj=null, theMapStringTestObj=null]', " +
                          "theCollectionTestObj=null, theMapStringTestObj=null]', " +
                          "theCollectionTestObj=null, theMapStringTestObj=null]', " +
                          "theCollectionTestObj=null, theMapStringTestObj=null]";
        TestObj testObj1 = new TestObj(config);
        TestObj testObj2 = new TestObj(config);
        TestObj testObj3 = new TestObj(config);
        TestObj testObj4 = new TestObj(config);
        testObj1.setTheInt(1);
        testObj2.setTheInt(2);
        testObj3.setTheInt(3);
        testObj4.setTheInt(4);
        testObj1.setTheString("one");
        testObj2.setTheString("two");
        testObj3.setTheString("three");
        testObj4.setTheString("four");
        testObj1.setTheTestObj(testObj2);
        testObj2.setTheTestObj(testObj3);
        testObj3.setTheTestObj(testObj4);
        testObj4.setTheTestObj(testObj1);
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionSingle.getParamString(testObj4, (p, c) -> objectToString(p, c, seen, config));
        assertEquals(expected, actual);
    }

    @Test
    public void equals_sameObject_true() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        assertTrue(paramDescriptionSingle.equals(paramDescriptionSingle));
    }

    @Test
    public void equals_sameConstructorParameters_true() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        assertTrue("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertTrue("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptNames_false() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj1",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj2",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        assertFalse("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertFalse("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptGetter_true() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheOtherString, INCLUDED_IN_ALL, false);
        assertTrue("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertTrue("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptParameter_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theString",
                                        TestObj::getTheInt, INCLUDED_IN_ALL, false);
        assertNotEquals("1, 2", paramDescriptionSingle1, paramDescriptionSingle2);
        assertNotEquals("2, 1", paramDescriptionSingle2, paramDescriptionSingle1);
    }

    @Test
    public void equals_sameConstructorParametersExceptParamMethodRestriction_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, IGNORED_FOR_ALL, false);
        assertFalse("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertFalse("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptParentClass_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_TOSTRING_ONLY, false);
        ParamDescriptionSingle<ParamDescription, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<ParamDescription, String>(
                                        ParamDescription.class, String.class, "theString",
                                        ParamDescription::getName, INCLUDED_IN_TOSTRING_ONLY, false);
        assertFalse("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertFalse("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void hashCode_runTwice_same() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        int actual = paramDescriptionSingle.hashCode();
        int expected = paramDescriptionSingle.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void hashCode_sameConstructorParameters_same() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        assertEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptNames_different() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj1",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj2",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, false);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptGetter_same() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheOtherString, INCLUDED_IN_ALL, false);
        assertEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParameter_different() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theString",
                                        TestObj::getTheInt, INCLUDED_IN_ALL, false);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParamMethodRestriction_different() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, IGNORED_FOR_ALL, false);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParentClass_different() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "Stringy",
                                        TestObj::getTheString, INCLUDED_IN_TOSTRING_ONLY, false);
        ParamDescriptionSingle<ParamDescription, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<ParamDescription, String>(
                                        ParamDescription.class, String.class, "Stringy",
                                        ParamDescription::getName, INCLUDED_IN_TOSTRING_ONLY, false);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void toString_stringParam_containsParamDescriptionSingle() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual.contains("ParamDescriptionSingle"));
    }

    @Test
    public void toString_stringParam_containsParentClass() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("parentClass"));
    }

    @Test
    public void toString_stringParam_containsParentClassName() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("TestObj"));
    }

    @Test
    public void toString_stringParam_containsParamClass() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("paramClass"));
    }

    @Test
    public void toString_stringParam_containsParamClassName() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("String"));
    }

    @Test
    public void toString_stringParam_containsName() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("name"));
    }

    @Test
    public void toString_stringParam_containsNameValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("theString"));
    }

    @Test
    public void toString_stringParam_containsGetter() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("getter"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestriction() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("paramMethodRestriction"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestrictionValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("INCLUDED_IN_ALL"));
    }
}

