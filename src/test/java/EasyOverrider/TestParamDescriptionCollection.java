package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_EQUALS_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.TestingUtils.Helpers.getConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class TestParamDescriptionCollection {

    private ParamDescriptionCollection<TestObj, String, ?> getParamCollectionString(String name, ParamMethodRestriction pmr) {
        ParamDescriptionCollection<TestObj, String, ?> retval =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, String.class, name,
                                        TestObj::getTheCollectionString, pmr);
        return retval;
    }

    private ParamDescriptionCollection<TestObj, TestObj, ?> getParamListTestObj(String name, ParamMethodRestriction pmr) {
        ParamDescriptionCollection<TestObj, TestObj, ?> retval =
                        new ParamDescriptionCollection<>(
                                        TestObj.class, Collection.class, TestObj.class, name,
                                        TestObj::getTheCollectionTestObj, pmr);
        return retval;
    }

    public ParamListServiceConfig config;

    @Before
    public void initTestParamDescriptionCollection() {
        if (config == null) {
            config = getConfig();
        }
    }

    //TODO: Clean up and make sure there's enough test coverage.

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
}
