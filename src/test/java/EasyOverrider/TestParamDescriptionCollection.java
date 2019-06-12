package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_EQUALS_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
public class TestParamDescriptionCollection {

    private ParamDescriptionCollection<TestObj, String, ?> getParamDescription(String name, ParamMethodRestriction pmr,
                                                                               BiFunction<String, Boolean, String> recursionPreventingToString) {
        ParamDescriptionCollection<TestObj, String, ?> retval =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, String.class, name,
                                        TestObj::getTheCollectionString, pmr, recursionPreventingToString);
        return retval;
    }

    @Test
    public void isCollection_something_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        assertTrue(paramDescriptionCollection.isCollection());
    }

    @Test
    public void isMap_something_false() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        assertFalse(paramDescriptionCollection.isMap());
    }

    @Test
    public void equals_sameObject_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        assertTrue(paramDescriptionCollection.equals(paramDescriptionCollection));
    }

    @Test
    public void equals_sameConstructorParameters_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        assertTrue("1.equals(2)", paramDescriptionCollection1.equals(paramDescriptionCollection2));
        assertTrue("2.equals(1)", paramDescriptionCollection2.equals(paramDescriptionCollection1));
    }

    @Test
    public void equals_sameConstructorParametersExceptRecursionPreventingToString_true() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection1 =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, null);
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection2 =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, TestObj::toString);
        assertTrue("1.equals(2)", paramDescriptionCollection1.equals(paramDescriptionCollection2));
        assertTrue("2.equals(1)", paramDescriptionCollection2.equals(paramDescriptionCollection1));
    }

    @Test
    public void equals_sameConstructorParametersExceptNames_false() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamDescription("theCollectionString2", INCLUDED_IN_ALL, null);
        assertFalse("1.equals(2)", paramDescriptionCollection1.equals(paramDescriptionCollection2));
        assertFalse("2.equals(1)", paramDescriptionCollection2.equals(paramDescriptionCollection1));
    }

    @Test
    public void equals_sameConstructorParametersExceptParamMethodRestriction_false() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamDescription("theCollectionString", IGNORED_FOR_ALL, null);
        assertFalse("1.equals(2)", paramDescriptionCollection1.equals(paramDescriptionCollection2));
        assertFalse("2.equals(1)", paramDescriptionCollection2.equals(paramDescriptionCollection1));
    }

    @Test
    public void hashCode_runTwice_same() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        int actual = paramDescriptionCollection1.hashCode();
        int expected = paramDescriptionCollection1.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void hashCode_sameConstructorParameters_same() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        assertEquals(paramDescriptionCollection1.hashCode(), paramDescriptionCollection2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptRecursionPreventingToString_same() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection1 =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, null);
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection2 =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, TestObj::toString);
        assertEquals(paramDescriptionCollection1.hashCode(), paramDescriptionCollection2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptNames_different() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamDescription("theCollectionString2", INCLUDED_IN_ALL, null);
        assertNotEquals(paramDescriptionCollection1.hashCode(), paramDescriptionCollection2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParamMethodRestriction_different() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamDescription("theCollectionString2", INCLUDED_IN_EQUALS_ONLY__UNSAFE, null);
        assertNotEquals(paramDescriptionCollection1.hashCode(), paramDescriptionCollection2.hashCode());
    }

    @Test
    public void toString_collectionStringParam_containsParamDescriptionCollection() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        assertTrue(paramDescriptionCollection.toString().contains("ParamDescriptionCollection"));
    }

    @Test
    public void toString_collectionStringParam_containsParentClass() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("parentClass"));
    }

    @Test
    public void toString_collectionStringParam_containsParentClassName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("TestObj"));
    }

    @Test
    public void toString_collectionStringParam_containsParamClass() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("paramClass"));
    }

    @Test
    public void toString_collectionStringParam_containsParamClassName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("Collection"));
    }

    @Test
    public void toString_collectionStringParam_containsEntryClass() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("entryClass"));
    }

    @Test
    public void toString_collectionStringParam_containsEntryClassName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("String"));
    }

    @Test
    public void toString_collectionStringParam_containsName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("name"));
    }

    @Test
    public void toString_collectionStringParam_containsParamName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("theCollectionString1"));
    }

    @Test
    public void toString_collectionStringParam_containsGetter() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("getter"));
    }

    @Test
    public void toString_collectionStringParam_containsParamMethodRestriction() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("paramMethodRestriction"));
    }

    @Test
    public void toString_collectionStringParam_containsParamMethodRestrictionValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("INCLUDED_IN_ALL"));
    }

    @Test
    public void toString_collectionStringParam_containsRecursionPreventingToString() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("recursionPreventingToString"));
    }

    @Test
    public void getParentClass_testObj_returnsCorrectValue() {
        Class<TestObj> expected = TestObj.class;
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        Class<TestObj> actual = paramDescriptionCollection.getParentClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamClass_string_returnsCorrectValue() {
        Class<? extends Collection> expected = Collection.class;
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        Class<? extends Collection> actual = paramDescriptionCollection.getParamClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getEntryClass_string_returnsCorrectValue() {
        Class<String> expected = String.class;
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        Class<String> actual = paramDescriptionCollection.getEntryClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getName_string_returnsCorrectValue() {
        String expected = "myCustomStringNameJustForThisTest";
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription(expected, INCLUDED_IN_ALL, null);
        String actual = paramDescriptionCollection.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamMethodRestriction_includedInHashCodeOnly_returnsCorrectValue() {
        ParamMethodRestriction expected = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("some name or thing", expected, null);
        ParamMethodRestriction actual = paramDescriptionCollection.getParamMethodRestriction();
        assertEquals(expected, actual);
    }

    @Test
    public void getRecursionPreventingToString_toString_returnsCorrectValue() {
        BiFunction<? super TestObj, Boolean, String> expected = TestObj::toString;
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection1 =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, expected);
        BiFunction<? super TestObj, Boolean, String> actual = paramDescriptionCollection1.getRecursionPreventingToString();
        assertEquals(actual, expected);
    }

    @Test
    public void isEqualsIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsIgnore();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamDescription("theCollectionString1", pmr, null);
            boolean actual = paramDescriptionCollection.isEqualsIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isEqualsInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsInclude();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamDescription("theCollectionString1", pmr, null);
            boolean actual = paramDescriptionCollection.isEqualsInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeIgnore();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamDescription("theCollectionString1", pmr, null);
            boolean actual = paramDescriptionCollection.isHashCodeIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeInclude();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamDescription("theCollectionString1", pmr, null);
            boolean actual = paramDescriptionCollection.isHashCodeInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringIgnore();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamDescription("theCollectionString1", pmr, null);
            boolean actual = paramDescriptionCollection.isToStringIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringInclude();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamDescription("theCollectionString1", pmr, null);
            boolean actual = paramDescriptionCollection.isToStringInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void get_collectionValue_matchesObjectValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Collection<String> expected = Arrays.asList("one", "two", "three");
        testObj.setTheCollectionString(expected);
        Collection<String> actual = paramDescriptionCollection.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void get_nullStringValue_matchesObjectValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Collection<String> expected = null;
        testObj.setTheCollectionString(expected);
        Collection<String> actual = paramDescriptionCollection.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void get_nullObj_blowsUp() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            Collection<String> actual = paramDescriptionCollection.get(testObj);
            fail("IllegalArgumentException should have been thrown calling get(null).");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void safeGet_collectionValue_matchesObjectValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Collection<String> expected = Arrays.asList("one", "two", "three", "four");
        testObj.setTheCollectionString(expected);
        Collection<String> actual = paramDescriptionCollection.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void safeGet_nullObj_returnsNull() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        Collection<String> expected = null;
        Collection<String> actual = paramDescriptionCollection.safeGet(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void paramsAreEqual_nullNull_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj1 = null;
        TestObj testObj2 = null;
        assertTrue(paramDescriptionCollection.paramsAreEqual(testObj1, testObj2));
    }

    @Test
    public void paramsAreEqual_nullVsNullParam_false() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        TestObj testObj1 = null;
        TestObj testObj2 = new TestObj();
        testObj2.setTheCollectionString(null);
        assertFalse("1, 2", paramDescriptionCollection.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionCollection.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_same_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        Collection<String> expected = Arrays.asList("five", "two", "three", "four");
        testObj.setTheCollectionString(expected);
        assertTrue(paramDescriptionCollection.paramsAreEqual(testObj, testObj));
    }

    @Test
    public void paramsAreEqual_differentObjectsSameValue_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        Collection<String> collection = Arrays.asList("five", "two", "three", "four");
        TestObj testObj1 = new TestObj();
        testObj1.setTheCollectionString(collection);
        TestObj testObj2 = new TestObj();
        testObj2.setTheCollectionString(collection);
        assertTrue(paramDescriptionCollection.paramsAreEqual(testObj1, testObj2));
    }

    @Test
    public void paramsAreEqual_differentObjectsDifferentValues_false() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj1 = new TestObj();
        testObj1.setTheCollectionString(Arrays.asList("one", "two"));
        TestObj testObj2 = new TestObj();
        testObj2.setTheCollectionString(Arrays.asList("one", "three"));
        assertFalse("1, 2", paramDescriptionCollection.paramsAreEqual(testObj1, testObj2));
        assertFalse("1, 2", paramDescriptionCollection.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_differentObjectsOneNullValue_false() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj1 = new TestObj();
        testObj1.setTheCollectionString(Arrays.asList("one", "two"));
        TestObj testObj2 = new TestObj();
        testObj2.setTheCollectionString(null);
        assertFalse("1, 2", paramDescriptionCollection.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionCollection.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void toString_object_returnsExpectedValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        Collection<String> collection = Arrays.asList("one", "two", "ten");
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(collection);
        assertEquals(collection.toString(), paramDescriptionCollection.toString(testObj));
    }

    @Test
    public void toString_nullObject_blowsUP() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.toString(testObj);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void toString_objectFalse_returnsExpectedValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        Collection<String> collection = Arrays.asList("one", "two", "ten");
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(collection);
        assertEquals(collection.toString(), paramDescriptionCollection.toString(testObj, false));
    }

    @Test
    public void toString_nullObjectFalse_blowsUP() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString1", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.toString(testObj, false);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void toString_objectTrue_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, TestObj::toString);
        String expected = "...";
        TestObj testObj = new TestObj();
        testObj.setTheCollectionTestObj(Arrays.asList(testObj, testObj, testObj));
        assertEquals(expected, paramDescriptionCollection.toString(testObj, true));
    }

    @Test
    public void toString_nullObjectTrue_blowsUP() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, TestObj::toString);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.toString(testObj, true);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_object_returnsExpectedValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        Collection<String> collection = Arrays.asList("420", "69");
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(collection);
        String expected = "theCollectionString='" + collection.toString() + "'";
        assertEquals(expected, paramDescriptionCollection.getNameValueString(testObj));
    }

    @Test
    public void getNameValueString_objectWithNull_returnsExpectedValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(null);
        String expected = "theCollectionString=null";
        assertEquals(expected, paramDescriptionCollection.getNameValueString(testObj));
    }

    @Test
    public void getNameValueString_nullObject_blowsUP() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.getNameValueString(testObj);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectFalse_returnsExpectedValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        Collection<String> collection = Arrays.asList("420", "69");
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(collection);
        String expected = "theCollectionString='" + collection.toString() + "'";
        assertEquals(expected, paramDescriptionCollection.getNameValueString(testObj, false));
    }

    @Test
    public void getNameValueString_objectWithNullFalse_returnsExpectedValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(null);
        String expected = "theCollectionString=null";
        assertEquals(expected, paramDescriptionCollection.getNameValueString(testObj, false));
    }

    @Test
    public void getNameValueString_nullObjectFalse_blowsUP() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamDescription("theCollectionString", INCLUDED_IN_ALL, null);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.getNameValueString(testObj, false);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectTrue_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, TestObj::toString);
        TestObj testObj = new TestObj();
        testObj.setTheCollectionTestObj(Arrays.asList(testObj, testObj));
        String expected = "theCollectionTestObj=...";
        assertEquals(expected, paramDescriptionCollection.getNameValueString(testObj, true));
    }

    @Test
    public void getNameValueString_objectWithNullTrue_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, TestObj::toString);
        TestObj testObj = new TestObj();
        testObj.setTheCollectionTestObj(null);
        String expected = "theCollectionTestObj=null";
        assertEquals(expected, paramDescriptionCollection.getNameValueString(testObj, true));
    }

    @Test
    public void getNameValueString_nullObjectTrue_blowsUP() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, "theCollectionTestObj",
                                        TestObj::getTheCollectionTestObj, INCLUDED_IN_ALL, TestObj::toString);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.getNameValueString(testObj, true);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }
}
