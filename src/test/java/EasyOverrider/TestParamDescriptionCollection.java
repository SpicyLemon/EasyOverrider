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

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class TestParamDescriptionCollection {

    static EasyOverriderService easyOverriderService = null;

    @Before
    public void initTestStuff() {
        if (easyOverriderService == null) {
            easyOverriderService = new EasyOverriderServiceImpl();
        }
    }

    private ParamDescriptionCollection<TestObj, String, ?> getParamCollectionString(String name, ParamMethodRestriction pmr) {
        ParamDescriptionCollection<TestObj, String, ?> retval =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, String.class, name,
                                        TestObj::getTheCollectionString, pmr, easyOverriderService);
        return retval;
    }

    private ParamDescriptionCollection<TestObj, TestObj, ?> getParamListTestObj(String name, ParamMethodRestriction pmr) {
        ParamDescriptionCollection<TestObj, TestObj, ?> retval =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, name,
                                        TestObj::getTheCollectionTestObj, pmr, easyOverriderService);
        return retval;
    }

    //TODO: Clean up and make sure there's enough test coverage.

    @Test
    public void equals_sameObject_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        assertTrue(paramDescriptionCollection.equals(paramDescriptionCollection));
    }

    @Test
    public void equals_sameConstructorParameters_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        assertTrue("1.equals(2)", paramDescriptionCollection1.equals(paramDescriptionCollection2));
        assertTrue("2.equals(1)", paramDescriptionCollection2.equals(paramDescriptionCollection1));
    }

    @Test
    public void equals_sameConstructorParametersExceptNames_false() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamCollectionString("theCollectionString2", INCLUDED_IN_ALL);
        assertFalse("1.equals(2)", paramDescriptionCollection1.equals(paramDescriptionCollection2));
        assertFalse("2.equals(1)", paramDescriptionCollection2.equals(paramDescriptionCollection1));
    }

    @Test
    public void equals_sameConstructorParametersExceptParamMethodRestriction_false() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamCollectionString("theCollectionString", IGNORED_FOR_ALL);
        assertFalse("1.equals(2)", paramDescriptionCollection1.equals(paramDescriptionCollection2));
        assertFalse("2.equals(1)", paramDescriptionCollection2.equals(paramDescriptionCollection1));
    }

    @Test
    public void hashCode_runTwice_same() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        int actual = paramDescriptionCollection1.hashCode();
        int expected = paramDescriptionCollection1.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void hashCode_sameConstructorParameters_same() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        assertEquals(paramDescriptionCollection1.hashCode(), paramDescriptionCollection2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptNames_different() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamCollectionString("theCollectionString2", INCLUDED_IN_ALL);
        assertNotEquals(paramDescriptionCollection1.hashCode(), paramDescriptionCollection2.hashCode());
    }

    @Test
    public void hashCode_sameConstructorParametersExceptParamMethodRestriction_different() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection1 =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection2 =
                        getParamCollectionString("theCollectionString2", INCLUDED_IN_EQUALS_ONLY__UNSAFE);
        assertNotEquals(paramDescriptionCollection1.hashCode(), paramDescriptionCollection2.hashCode());
    }

    @Test
    public void toString_collectionStringParam_containsParamDescriptionCollection() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        assertTrue(paramDescriptionCollection.toString().contains("ParamDescriptionCollection"));
    }

    @Test
    public void toString_collectionStringParam_containsParentClass() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("parentClass"));
    }

    @Test
    public void toString_collectionStringParam_containsParentClassName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("TestObj"));
    }

    @Test
    public void toString_collectionStringParam_containsParamClass() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("paramClass"));
    }

    @Test
    public void toString_collectionStringParam_containsParamClassName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("Collection"));
    }

    @Test
    public void toString_collectionStringParam_containsEntryClass() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("entryClass"));
    }

    @Test
    public void toString_collectionStringParam_containsEntryClassName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("String"));
    }

    @Test
    public void toString_collectionStringParam_containsName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("name"));
    }

    @Test
    public void toString_collectionStringParam_containsParamName() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("theCollectionString1"));
    }

    @Test
    public void toString_collectionStringParam_containsGetter() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("getter"));
    }

    @Test
    public void toString_collectionStringParam_containsParamMethodRestriction() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("paramMethodRestriction"));
    }

    @Test
    public void toString_collectionStringParam_containsParamMethodRestrictionValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.toString();
        assertTrue(actual, actual.contains("INCLUDED_IN_ALL"));
    }

    @Test
    public void getParentClass_testObj_returnsCorrectValue() {
        Class<TestObj> expected = TestObj.class;
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        Class<TestObj> actual = paramDescriptionCollection.getParentClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamClass_string_returnsCorrectValue() {
        Class<? extends Collection> expected = Collection.class;
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        Class<? extends Collection> actual = paramDescriptionCollection.getParamClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getEntryClass_string_returnsCorrectValue() {
        Class<String> expected = String.class;
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        Class<String> actual = paramDescriptionCollection.getEntryClass();
        assertEquals(expected, actual);
    }

    @Test
    public void getName_string_returnsCorrectValue() {
        String expected = "myCustomStringNameJustForThisTest";
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString(expected, INCLUDED_IN_ALL);
        String actual = paramDescriptionCollection.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getParamMethodRestriction_includedInHashCodeOnly_returnsCorrectValue() {
        ParamMethodRestriction expected = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("some name or thing", expected);
        ParamMethodRestriction actual = paramDescriptionCollection.getParamMethodRestriction();
        assertEquals(expected, actual);
    }

    @Test
    public void isEqualsIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsIgnore();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamCollectionString("theCollectionString1", pmr);
            boolean actual = paramDescriptionCollection.isEqualsIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isEqualsInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isEqualsInclude();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamCollectionString("theCollectionString1", pmr);
            boolean actual = paramDescriptionCollection.isEqualsInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeIgnore();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamCollectionString("theCollectionString1", pmr);
            boolean actual = paramDescriptionCollection.isHashCodeIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isHashCodeInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isHashCodeInclude();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamCollectionString("theCollectionString1", pmr);
            boolean actual = paramDescriptionCollection.isHashCodeInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringIgnore_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringIgnore();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamCollectionString("theCollectionString1", pmr);
            boolean actual = paramDescriptionCollection.isToStringIgnore();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void isToStringInclude_allParamMethodRestrictions_matchesParamMethodRestriction() {
        for(ParamMethodRestriction pmr : ParamMethodRestriction.values()) {
            boolean expected = pmr.isToStringInclude();
            ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                            getParamCollectionString("theCollectionString1", pmr);
            boolean actual = paramDescriptionCollection.isToStringInclude();
            assertEquals(pmr.toString(), expected, actual);
        }
    }

    @Test
    public void get_collectionValue_matchesObjectValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        Collection<String> expected = Arrays.asList("one", "two", "three");
        testObj.setTheCollectionString(expected);
        Collection<String> actual = (Collection<String>)paramDescriptionCollection.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void get_nullStringValue_matchesObjectValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        Collection<String> expected = null;
        testObj.setTheCollectionString(expected);
        Collection<String> actual = (Collection<String>)paramDescriptionCollection.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void get_nullObj_blowsUp() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            Collection<String> actual = (Collection<String>)paramDescriptionCollection.get(testObj);
            fail("IllegalArgumentException should have been thrown calling get(null).");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void safeGet_collectionValue_matchesObjectValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        Collection<String> expected = Arrays.asList("one", "two", "three", "four");
        testObj.setTheCollectionString(expected);
        Collection<String> actual = (Collection<String>)paramDescriptionCollection.get(testObj);
        assertEquals(expected, actual);
    }

    @Test
    public void paramsAreEqual_nullNull_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        TestObj testObj1 = null;
        TestObj testObj2 = null;
        assertTrue(paramDescriptionCollection.paramsAreEqual(testObj1, testObj2));
    }

    @Test
    public void paramsAreEqual_nullVsNullParam_false() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        TestObj testObj1 = null;
        TestObj testObj2 = new TestObj();
        testObj2.setTheCollectionString(null);
        assertFalse("1, 2", paramDescriptionCollection.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionCollection.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramsAreEqual_same_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        Collection<String> expected = Arrays.asList("five", "two", "three", "four");
        testObj.setTheCollectionString(expected);
        assertTrue(paramDescriptionCollection.paramsAreEqual(testObj, testObj));
    }

    @Test
    public void paramsAreEqual_differentObjectsSameValue_true() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
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
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
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
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        TestObj testObj1 = new TestObj();
        testObj1.setTheCollectionString(Arrays.asList("one", "two"));
        TestObj testObj2 = new TestObj();
        testObj2.setTheCollectionString(null);
        assertFalse("1, 2", paramDescriptionCollection.paramsAreEqual(testObj1, testObj2));
        assertFalse("2, 1", paramDescriptionCollection.paramsAreEqual(testObj2, testObj1));
    }

    @Test
    public void paramValueToString_objectFalse_returnsExpectedValue() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        Collection<String> collection = Arrays.asList("one", "two", "ten");
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(collection);
        assertEquals(collection.toString(), paramDescriptionCollection.paramValueToString(testObj, new HashMap<>()));
    }

    @Test
    public void paramValueToString_nullObjectNullMap_blowsUP() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString1", INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.paramValueToString(testObj, null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void paramValueToString_nullObjectEmptyMap_blowsUP() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        getParamListTestObj("theCollectionTestObj", INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.paramValueToString(testObj, new HashMap<>());
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_nullObjectNull_blowsUP() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.getNameValueString(testObj, null);
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }

    @Test
    public void getNameValueString_objectInSeen_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        getParamListTestObj("theCollectionTestObj", INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        testObj.setTheCollectionTestObj(Arrays.asList(testObj, testObj));
        HashMap<Class, Set<Integer>> seen = new HashMap<>();
        seen.put(TestObj.class, new HashSet<>());
        seen.get(TestObj.class).add(testObj.hashCode());
        String expected = "theCollectionTestObj='[..., ...]'";
        assertEquals(expected, paramDescriptionCollection.getNameValueString(testObj, seen));
    }

    @Test
    public void getNameValueString_objectWithNullInSeen_returnsExpectedValueAndPreventsRecursion() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        getParamListTestObj("theCollectionTestObj", INCLUDED_IN_ALL);
        TestObj testObj = new TestObj();
        testObj.setTheCollectionTestObj(null);
        HashMap<Class, Set<Integer>> seen = new HashMap<>();
        seen.put(TestObj.class, new HashSet<>());
        seen.get(TestObj.class).add(testObj.hashCode());
        String expected = "theCollectionTestObj=null";
        assertEquals(expected, paramDescriptionCollection.getNameValueString(testObj, seen));
    }

    @Test
    public void getNameValueString_nullObjectEmptyMap_blowsUP() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        getParamListTestObj("theCollectionTestObj", INCLUDED_IN_ALL);
        TestObj testObj = null;
        try {
            String boom = paramDescriptionCollection.getNameValueString(testObj, new HashMap<>());
            fail("IllegalArgumentException should have been thrown here.");
        } catch (IllegalArgumentException iae) {
            //expected
        }
    }
}
