package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static EasyOverrider.ParamMethodRestriction.EQUALS_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.TestingUtils.Helpers.getConfig;
import static EasyOverrider.TestingUtils.Helpers.objectToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import EasyOverrider.TestingUtils.TestObj;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class TestParamDescriptionCollection {

    private ParamDescriptionCollection<TestObj, String, ?> getParamCollectionString(String name, ParamMethodRestriction pmr) {
        return new ParamDescriptionCollection<>(TestObj.class, Collection.class, String.class, name,
                                                TestObj::getTheCollectionString, pmr);
    }

    private ParamDescriptionCollection<TestObj, TestObj, ?> getParamListTestObj(String name, ParamMethodRestriction pmr) {
        return new ParamDescriptionCollection<>(TestObj.class, Collection.class, TestObj.class, name,
                                                TestObj::getTheCollectionTestObj, pmr);
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
            ParamDescriptionCollection<TestObj, String, ?> pdc =
                            new ParamDescriptionCollection<>(null, Collection.class, String.class, "pdc",
                                                             TestObj::getTheCollectionString, INCLUDED_IN_ALL);
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
            ParamDescriptionCollection<TestObj, String, ?> pdc =
                            new ParamDescriptionCollection<>(TestObj.class, null, String.class, "pdc",
                                                             TestObj::getTheCollectionString, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 2 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("paramClass"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }
    @Test
    public void constructor_nullEntryClass_boom() {
        try {
            ParamDescriptionCollection<TestObj, String, ?> pdc =
                            new ParamDescriptionCollection<>(TestObj.class, Collection.class, null, "pdc",
                                                             TestObj::getTheCollectionString, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 3 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("entryClass"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullName_boom() {
        try {
            ParamDescriptionCollection<TestObj, String, ?> pdc =
                            new ParamDescriptionCollection<>(TestObj.class, Collection.class, String.class, null,
                                                             TestObj::getTheCollectionString, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 4 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("name"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullGetter_boom() {
        try {
            ParamDescriptionCollection<TestObj, String, ?> pdc =
                            new ParamDescriptionCollection<>(TestObj.class, Collection.class, String.class, "pdc",
                                                             null, INCLUDED_IN_ALL);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 5 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("getter"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
    }

    @Test
    public void constructor_nullParamMethodRestriction_boom() {
        try {
            ParamDescriptionCollection<TestObj, String, ?> pdc =
                            new ParamDescriptionCollection<>(TestObj.class, Collection.class, String.class, "pdc",
                                                             TestObj::getTheCollectionString, null);
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message does not contain parameter index.", e.getMessage().contains(" 6 "));
            assertTrue("Exception message does not contain parameter name.", e.getMessage().contains("paramMethodRestriction"));
            assertTrue("Exception message does not contain method name.", e.getMessage().contains("ParamDescription"));
            assertTrue("Exception message does not contain 'constructor'", e.getMessage().contains("constructor"));
        }
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
    @Ignore
    public void getGetter_getCollection_returnsCorrectValue() {
        //TODO: Figure out how to test this.
        //Problem: The getter is a Function<TestObj, Collection<String>>.
        // However, in the constructor, we can only send in Collection.class.
        // So P must be Collection and not Collection<String>, and same with the getter.
        // But then, we can only use a wildcard in the variable type for P because it must
        // simultaneously be a raw Collection and a Collection<String>.
        // That means that when we call getGetter, it doesn't know what the return value for the function is.
        // If we go back and try to tell it it's a Collection<String>, then we run into the whole
        // Collection.class parameter thing, and not being able to do Collection<String>.class.
        //
        //Function<TestObj, Collection> expected = TestObj::getTheCollectionString;
        //ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
        //                new ParamDescriptionCollection<>(
        //                                TestObj.class, Collection.class, String.class,
        //                                "theCollectionOrSomething", expected, INCLUDED_IN_ALL);
        //Function<TestObj, Collection> actual = paramDescriptionCollection.getGetter();
        //assertEquals(expected, actual);
        assertTrue(true);
    }

    @Test
    public void getParamMethodRestriction_includedInHashCodeOnly_returnsCorrectValue() {
        ParamMethodRestriction expected = HASHCODE_ONLY__UNSAFE;
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("some name or thing", expected);
        ParamMethodRestriction actual = paramDescriptionCollection.getParamMethodRestriction();
        assertEquals(expected, actual);
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
    public void getParamString_stringFourEntries_equalsExpected() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        String expected = "[one, two, three, one]";
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(new ArrayList<>());
        testObj.getTheCollectionString().add("one");
        testObj.getTheCollectionString().add("two");
        testObj.getTheCollectionString().add("three");
        testObj.getTheCollectionString().add("one");
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionCollection.getParamString(testObj, (p, c) -> objectToString(p, c, seen));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_nullString_equalsExpected() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        String expected = config.getStringForNull();
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(null);
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionCollection.getParamString(testObj, (p, c) -> objectToString(p, c, seen));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_empty_equalsExpected() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        String expected = "[]";
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(new ArrayList<>());
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionCollection.getParamString(testObj, (p, c) -> objectToString(p, c, seen));
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_twoEntriesCustomBiFunction_equalsExpected() {
        ParamDescriptionCollection<TestObj, String, ?> paramDescriptionCollection =
                        getParamCollectionString("theCollectionString", INCLUDED_IN_ALL);
        String expected = "[String of 'ten', String of 'eleven']";
        TestObj testObj = new TestObj();
        testObj.setTheCollectionString(new ArrayList<>());
        testObj.getTheCollectionString().add("ten");
        testObj.getTheCollectionString().add("eleven");
        String actual = paramDescriptionCollection.getParamString(testObj, (p, c) -> c.getSimpleName() + " of '" + p.toString() + "'");
        assertEquals(expected, actual);
    }

    @Test
    public void getParamString_ListTestObjDeepRecursion_equalsExpected() {
        ParamDescriptionCollection<TestObj, TestObj, ?> paramDescriptionCollection =
                        getParamListTestObj("theListTestObj", INCLUDED_IN_ALL);
        String expected = "[TestObj@HASHCODE [theBoolean='false', theInt='1', theString='one', theOtherString=null, " +
                                        "theCollectionString=null, theMapStringInt=null, theTestObj=null, theCollectionTestObj='[" +
                             "TestObj@HASHCODE [theInt='1'...], " +
                             "TestObj@HASHCODE [theBoolean='false', theInt='2', theString='two', theOtherString=null, " +
                                        "theCollectionString=null, theMapStringInt=null, theTestObj=null, theCollectionTestObj='[" +
                               "TestObj@HASHCODE [theInt='1'...], " +
                               "TestObj@HASHCODE [theInt='2'...], " +
                               "TestObj@HASHCODE [theBoolean='false', theInt='3', theString='three', theOtherString=null, " +
                                        "theCollectionString=null, theMapStringInt=null, theTestObj=null, theCollectionTestObj='[" +
                                 "TestObj@HASHCODE [theInt='1'...], " +
                                 "TestObj@HASHCODE [theInt='2'...], " +
                                 "TestObj@HASHCODE [theInt='3'...]]', theMapStringTestObj=null]]', theMapStringTestObj=null], " +
                             "TestObj@HASHCODE [theInt='3'...]]', theMapStringTestObj=null], " +
                           "TestObj@HASHCODE [theInt='2'...], " +
                           "TestObj@HASHCODE [theInt='3'...]]";
        TestObj testObj1 = new TestObj();
        TestObj testObj2 = new TestObj();
        TestObj testObj3 = new TestObj();
        testObj1.setTheInt(1);
        testObj2.setTheInt(2);
        testObj3.setTheInt(3);
        testObj1.setTheString("one");
        testObj2.setTheString("two");
        testObj3.setTheString("three");
        List<TestObj> theList = new ArrayList<>();
        theList.add(testObj1);
        theList.add(testObj2);
        theList.add(testObj3);
        testObj1.setTheCollectionTestObj(theList);
        testObj2.setTheCollectionTestObj(theList);
        testObj3.setTheCollectionTestObj(theList);
        Map<Class, Set<Integer>> seen = new HashMap<>();
        String actual = paramDescriptionCollection.getParamString(testObj3, (p, c) -> objectToString(p, c, seen));
        assertEquals(expected, actual);
    }

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
                        getParamCollectionString("theCollectionString2", EQUALS_ONLY__UNSAFE);
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
}
