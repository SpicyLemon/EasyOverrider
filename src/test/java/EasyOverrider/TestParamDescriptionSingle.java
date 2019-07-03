package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
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
import java.util.function.Function;

public class TestParamDescriptionSingle {

    private static EasyOverriderService easyOverriderService = null;

    @Before
    public void initTestStuff() {
        if (easyOverriderService == null) {
            //Since hashcodes don't have to be the same all the time, we can't hard-code
            //toString results in here that contain the hashCode values.
            //So, just tell the service to use a hard coded string for that part.
            easyOverriderService = new EasyOverriderServiceImpl(new EasyOverriderConfig().setHashCodeToString((i) -> "HASHCODE"));
        }
    }

    @Test
    public void constructor_nullParentClass_throwsException() {
        try {
            ParamDescriptionSingle<TestObj, TestObj>
                            paramDescriptionSingle = new ParamDescriptionSingle<TestObj, TestObj>(
                                            null, TestObj.class, "theTestObj", TestObj::getTheTestObj,
                                            INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
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
                            INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
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
                            INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
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
                            INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
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
                            null, easyOverriderService, false);
            fail("Constructor given null parentClass did not throw exception.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 5 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("paramMethodRestriction"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullService_throwsException() {
        try {
            ParamDescriptionSingle<TestObj, TestObj>
                            paramDescriptionSingle = new ParamDescriptionSingle<TestObj, TestObj>(
                            TestObj.class, TestObj.class, "theTestObj", TestObj::getTheTestObj,
                            INCLUDED_IN_TOSTRING_ONLY, null, false);
            fail("Constructor given null parentClass did not throw exception.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 6 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("easyOverriderService"));
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
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        Class<TestObj> actual = paramDescriptionSingle1.getParentClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamClass_string_returnsCorrectValue() {
        Class<String> expected = String.class;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, expected, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        Class<String> actual = paramDescriptionSingle1.getParamClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getName_string_returnsCorrectValue() {
        String expected = "myCustomStringNameJustForThisTest";
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, expected,
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getGetter_getTheString_returnsCorrectValue() {
        Function<? super TestObj, String> expected = TestObj::getTheString;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        expected, INCLUDED_IN_ALL, easyOverriderService, false);
        Function<? super TestObj, String> actual = paramDescriptionSingle1.getGetter();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamMethodRestriction_includedInHashCodeOnly_returnsCorrectValue() {
        ParamMethodRestriction expected = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, expected, easyOverriderService, false);
        ParamMethodRestriction actual = paramDescriptionSingle1.getParamMethodRestriction();
        assertEquals(expected, actual);
    }

    @Test
    public void setService_differentSettings_toStringContainsServiceToString() {
        EasyOverriderService originalService = easyOverriderService;
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, originalService, false);
        EasyOverriderService newService = new EasyOverriderServiceImpl();
        newService.getConfig().setParameterValueFormat(">>>>%1$s<<<<");
        newService.getConfig().setHashCodeToString((i) -> "HASHCODE");
        newService.getConfig().setParameterDelimiter(" | ");
        paramDescriptionSingle.setService(newService);
        String originalServiceString = originalService.toString();
        String newServiceString = newService.toString();
        String pdString = paramDescriptionSingle.toString();
        assertFalse("The ParamDescriptionSingle.toString() should not have the original service info in it.",
                    pdString.contains(originalServiceString) );
        assertTrue("The ParamDescriptionSingle.toString() should contain the new service info in it.",
                   pdString.contains(newServiceString));
    }

    @Test
    public void isEqualsIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsIgnore();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString,
                                                                        pmr, easyOverriderService, false);
            boolean actual = paramDescriptionSingle.isEqualsIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isEqualsInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsInclude();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString,
                                                                        pmr, easyOverriderService, false);
            boolean actual = paramDescriptionSingle.isEqualsInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeIgnore();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString,
                                                                        pmr, easyOverriderService, false);
            boolean actual = paramDescriptionSingle.isHashCodeIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeInclude();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString,
                                                                        pmr, easyOverriderService, false);
            boolean actual = paramDescriptionSingle.isHashCodeInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringIgnore();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString,
                                                                        pmr, easyOverriderService, false);
            boolean actual = paramDescriptionSingle.isToStringIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringInclude();
            ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                            new ParamDescriptionSingle<TestObj, String>(TestObj.class, String.class, "theString", TestObj::getTheString,
                                                                        pmr, easyOverriderService, false);
            boolean actual = paramDescriptionSingle.isToStringInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void get_stringValue_matchesObjectValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
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
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
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
                                        TestObj::getTheInt, INCLUDED_IN_ALL, easyOverriderService, false);
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
                                        TestObj::getTheInt, INCLUDED_IN_ALL, easyOverriderService, false);
        TestObj testObj = null;
        try {
            int actual = paramDescriptionSingle.get(testObj);
            fail("IllegalArgumentException should have been thrown calling get(null).");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void paramsAreEqual_nullNull_true() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Boolean>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, INCLUDED_IN_ALL, easyOverriderService, false);
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
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        TestObj testObj1 = null;
        TestObj testObj2 = new TestObj();
        testObj2.setTheString(null);
        assertFalse("1, 2", paramDescriptionSingle.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionSingle.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_nullVsValue_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        TestObj testObj1 = null;
        TestObj testObj2 = new TestObj();
        testObj2.setTheString("A Value");
        assertFalse("1, 2", paramDescriptionSingle.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionSingle.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_sameObject_true() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Boolean>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, INCLUDED_IN_ALL, easyOverriderService, false);
        TestObj testObj = new TestObj();
        testObj.setTheBoolean(true);
        assertTrue(paramDescriptionSingle.paramsAreEqual(testObj, testObj));
    }

    @Test
    public void paramsAreEqual_differentObjectsSameValue_true() {
        ParamDescriptionSingle<TestObj, Boolean> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, Boolean>(
                                        TestObj.class, Boolean.class, "theBoolean",
                                        TestObj::isTheBoolean, INCLUDED_IN_ALL, easyOverriderService, false);
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
                                        TestObj::isTheBoolean, INCLUDED_IN_ALL, easyOverriderService, false);
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
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        TestObj testObj1 = new TestObj();
        testObj1.setTheString("food");
        TestObj testObj2 = new TestObj();
        testObj2.setTheString(null);
        assertFalse("1, 2", paramDescriptionSingle.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionSingle.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramValueToString_nullObjectWithMap_blowsUP() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        try {
            String boom = paramDescriptionSingle.paramValueToString(null, new HashMap<>());
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void paramValueToString_ObjectNullMap_blowsUP() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        try {
            String boom = paramDescriptionSingle.paramValueToString(new TestObj(), null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void paramValueToString_objectEmpty_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        TestObj testObj = new TestObj();
        testObj.setTheTestObj(testObj);
        String expectedStart = "EasyOverrider.TestObj@";
        String expectedEnd = " [" +
                          "theBoolean='false', " +
                          "theInt='0', " +
                          "theString=null, " +
                          "theOtherString=null, " +
                          "theCollectionString=null, " +
                          "theMapStringInt=null, " +
                          "theTestObj=..., " +
                          "theCollectionTestObj=null, " +
                          "theMapStringTestObj=null]";
        String actual = paramDescriptionSingle.paramValueToString(testObj, new HashMap<>());
        assertTrue("Start of toString result is incorret.", actual.contains(expectedStart));
        assertTrue("End of toString result is incorret.", actual.contains(expectedEnd));
    }

    @Test
    public void valueToStringPreventingRecursion_nullEmpty_returnsStringNull() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        String expected = "null";
        String actual = paramDescriptionSingle.valueToStringPreventingRecursion(null, new HashMap<>());
        assertEquals(expected, actual);
    }

    @Test
    public void valueToStringPreventingRecursion_ObjectNullMap_boom() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        try {
            String boom = paramDescriptionSingle.valueToStringPreventingRecursion(new TestObj(), null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void valueToStringPreventingRecursion_objectMap_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        TestObj testObj = new TestObj();
        testObj.setTheTestObj(testObj);
        String expectedStart = "EasyOverrider.TestObj@";
        String expectedEnd = " [" +
                          "theBoolean='false', " +
                          "theInt='0', " +
                          "theString=null, " +
                          "theOtherString=null, " +
                          "theCollectionString=null, " +
                          "theMapStringInt=null, " +
                          "theTestObj=..., " +
                          "theCollectionTestObj=null, " +
                          "theMapStringTestObj=null]";
        String actual = paramDescriptionSingle.valueToStringPreventingRecursion(testObj, new HashMap<>());
        assertTrue("Start of toString result is incorret.", actual.contains(expectedStart));
        assertTrue("End of toString result is incorret.", actual.contains(expectedEnd));
    }

    @Test
    public void getNameValueString_nullObjectEmptyMap_blowsUP() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        try {
            String boom = paramDescriptionSingle.getNameValueString(null, new HashMap<>());
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectNullMap_blowsUP() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        try {
            String boom = paramDescriptionSingle.getNameValueString(new TestObj(), null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectEmptyMap_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        TestObj testObj = new TestObj();
        testObj.setTheTestObj(testObj);
        String expectedStart = "theTestObj='EasyOverrider.TestObj@";
        String expectedEnd = " [" +
                          "theBoolean='false', " +
                          "theInt='0', " +
                          "theString=null, " +
                          "theOtherString=null, " +
                          "theCollectionString=null, " +
                          "theMapStringInt=null, " +
                          "theTestObj=..., " +
                          "theCollectionTestObj=null, " +
                          "theMapStringTestObj=null]'";
        String actual = paramDescriptionSingle.getNameValueString(testObj, new HashMap<>());
        assertTrue("Start of toString result is incorret.", actual.contains(expectedStart));
        assertTrue("End of toString result is incorret.", actual.contains(expectedEnd));
    }

    @Test
    public void getNameValueString_objectMapWithAlreadySeen_returnsExpectedValue() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_ALL, easyOverriderService, false);
        TestObj testObj = new TestObj();
        testObj.setTheTestObj(testObj);
        Map<Class, Set<Integer>> seen = new HashMap<>();
        seen.put(TestObj.class, new HashSet<>());
        seen.get(TestObj.class).add(testObj.hashCode());
        String expected = "theTestObj=...";
        String actual = paramDescriptionSingle.getNameValueString(testObj, seen);
        assertEquals(expected, actual);
    }

    @Test
    public void equals_sameObject_true() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        assertTrue(paramDescriptionSingle.equals(paramDescriptionSingle));
    }

    @Test
    public void equals_sameConstructorParameters_true() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        assertTrue("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertTrue("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptNames_false() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj1",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj2",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        assertFalse("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertFalse("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptGetter_true() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheOtherString, INCLUDED_IN_ALL, easyOverriderService, false);
        assertTrue("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertTrue("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptParameter_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theString",
                                        TestObj::getTheInt, INCLUDED_IN_ALL, easyOverriderService, false);
        assertNotEquals("1, 2", paramDescriptionSingle1, paramDescriptionSingle2);
        assertNotEquals("2, 1", paramDescriptionSingle2, paramDescriptionSingle1);
    }

    @Test
    public void equals_sameConstructorParametersExceptParamMethodRestriction_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, IGNORED_FOR_ALL, easyOverriderService, false);
        assertFalse("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertFalse("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptParentClass_false() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        ParamDescriptionSingle<ParamDescription, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<ParamDescription, String>(
                                        ParamDescription.class, String.class, "theString",
                                        ParamDescription::getName, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        assertFalse("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertFalse("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void equals_sameConstructorParametersExceptService_true() {
        EasyOverriderService service = new EasyOverriderServiceImpl();
        service.getConfig().setStringForEmptyParamList("nothing");
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, service, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        assertFalse("1.equals(2)", paramDescriptionSingle1.equals(paramDescriptionSingle2));
        assertFalse("2.equals(1)", paramDescriptionSingle2.equals(paramDescriptionSingle1));
    }

    @Test
    public void hashCode_runTwice_same() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        int actual = paramDescriptionSingle.hashCode();
        int expected = paramDescriptionSingle.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void hashCode_sameConstructorParameters_same() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        assertEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptNames_different() {
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj1",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj2",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptGetter_same() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheOtherString, INCLUDED_IN_ALL, easyOverriderService, false);
        assertEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParameter_different() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, Integer> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, Integer>(
                                        TestObj.class, Integer.class, "theString",
                                        TestObj::getTheInt, INCLUDED_IN_ALL, easyOverriderService, false);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParamMethodRestriction_different() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, IGNORED_FOR_ALL, easyOverriderService, false);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParentClass_different() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "Stringy",
                                        TestObj::getTheString, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        ParamDescriptionSingle<ParamDescription, String> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<ParamDescription, String>(
                                        ParamDescription.class, String.class, "Stringy",
                                        ParamDescription::getName, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptService_different() {
        EasyOverriderService service = new EasyOverriderServiceImpl();
        service.getConfig().setParameterDelimiter(":");
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, service, false);
        ParamDescriptionSingle<TestObj, TestObj> paramDescriptionSingle2 =
                        new ParamDescriptionSingle<TestObj, TestObj>(
                                        TestObj.class, TestObj.class, "theTestObj",
                                        TestObj::getTheTestObj, INCLUDED_IN_TOSTRING_ONLY, easyOverriderService, false);
        assertNotEquals(paramDescriptionSingle1.hashCode(), paramDescriptionSingle2.hashCode());
    }

    @Test
    public void toString_stringParam_containsParamDescriptionSingle() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual.contains("ParamDescriptionSingle"));
    }

    @Test
    public void toString_stringParam_containsParentClass() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("parentClass"));
    }

    @Test
    public void toString_stringParam_containsParentClassName() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("TestObj"));
    }

    @Test
    public void toString_stringParam_containsParamClass() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("paramClass"));
    }

    @Test
    public void toString_stringParam_containsParamClassName() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("String"));
    }

    @Test
    public void toString_stringParam_containsName() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("name"));
    }

    @Test
    public void toString_stringParam_containsNameValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("theString"));
    }

    @Test
    public void toString_stringParam_containsGetter() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("getter"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestriction() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("paramMethodRestriction"));
    }

    @Test
    public void toString_stringParam_containsParamMethodRestrictionValue() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("INCLUDED_IN_ALL"));
    }

    @Test
    public void toString_stringParam_containsEasyOverriderService() {
        ParamDescriptionSingle<TestObj, String> paramDescriptionSingle1 =
                        new ParamDescriptionSingle<TestObj, String>(
                                        TestObj.class, String.class, "theString",
                                        TestObj::getTheString, INCLUDED_IN_ALL, easyOverriderService, false);
        String actual = paramDescriptionSingle1.toString();
        assertTrue(actual, actual.contains("easyOverriderService"));
    }
}

