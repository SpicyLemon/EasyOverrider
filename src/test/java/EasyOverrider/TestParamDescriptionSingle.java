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

import org.junit.Test;

import java.util.function.Function;

public class TestParamDescriptionSingle {

    //TODO: Clean up and make sure there's enough test coverage.

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

