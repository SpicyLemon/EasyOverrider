package EasyOverrider;

import static EasyOverrider.EasyOverriderUtils.getIndexOrDefault;
import static EasyOverrider.EasyOverriderUtils.requireNonNull;
import static EasyOverrider.EasyOverriderUtils.runSetterIfNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestEasyOverriderUtils {

    @Test
    public void requireNonNull_null_messageContainsPieces() {
        int expectedPosition = 34;
        String expectedParamName = "Really? There's that many?";
        String expectedMethodName = "Good God Man!";
        try {
            requireNonNull(null, expectedPosition, expectedParamName, expectedMethodName);
            fail("No exception was thrown!");
        } catch (IllegalArgumentException e) {
            assertTrue("The exception message should have the position.", e.getMessage().contains(String.valueOf(expectedPosition)));
            assertTrue("The exception message should have the paramName.", e.getMessage().contains(String.valueOf(expectedParamName)));
            assertTrue("The exception message should have the methodName.", e.getMessage().contains(String.valueOf(expectedMethodName)));
        }
    }

    @Test
    public void requireNonNull_withValue_noErrorThrown() {
        //Just making sure this doesn't throw an exception.
        requireNonNull("foo", 3, "bar", "baz");
    }

    @Test
    public void runSetterIfNotNull_nullNullNull_noProblem() {
        runSetterIfNotNull(null, null, null);
    }

    @Test
    public void runSetterIfNotNull_objectNullNull_noProblem() {
        TestObj testObj = new TestObj();
        runSetterIfNotNull(testObj, null, null);
    }

    @Test
    public void runSetterIfNotNull_nullSetterNull_noProblem() {
        runSetterIfNotNull(null, TestObj::setTheString, null);
    }

    @Test
    public void runSetterIfNotNull_nullNullObject_noProblem() {
        runSetterIfNotNull(null, null, "foo");
    }

    @Test
    public void runSetterIfNotNull_objectSetterNull_nothingChanged() {
        int expected = 5;
        TestObj testObj = new TestObj();
        testObj.setTheInt(expected);
        runSetterIfNotNull(testObj, TestObj::setTheInt, null);
        assertEquals(expected, testObj.getTheInt());
    }

    @Test
    public void runSetterIfNotNull_objectNullValue_nothingChanged() {
        int expected = 5;
        TestObj testObj = new TestObj();
        testObj.setTheInt(expected);
        runSetterIfNotNull(testObj, null, 3);
        assertEquals(expected, testObj.getTheInt());
    }

    @Test
    public void runSetterIfNotNull_nullSetterValue_nothingChanged() {
        runSetterIfNotNull(null, TestObj::setTheInt, 3);
    }

    @Test
    public void runSetterIfNotNull_objectSetterValue_valueChanges() {
        int notExpected = 5;
        int expected = 3;
        TestObj testObj = new TestObj();
        testObj.setTheInt(notExpected);
        runSetterIfNotNull(testObj, TestObj::setTheInt, expected);
        assertEquals(expected, testObj.getTheInt());
    }

    @Test
    public void getIndexOrDefault_boring_equalsExpected() {
        List<Integer> ix = Arrays.asList(1, 2, 3);
        int expected = 2;
        int actual = getIndexOrDefault(ix, expected);
        assertEquals(expected, actual);
    }

    @Test
    public void getIndexOrDefault_weird_equalsExpected() {
        int expected1 = 69;
        int expected2 = 311;
        int expected3 = 420;
        List<Integer> ix = Arrays.asList(expected1, expected2, expected3);
        int actual1 = getIndexOrDefault(ix, 1);
        int actual2 = getIndexOrDefault(ix, 2);
        int actual3 = getIndexOrDefault(ix, 3);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
    }

    @Test
    public void getIndexOrDefault_nullList_equalsExpected() {
        int expected = 3;
        int actual = getIndexOrDefault(null, expected);
        assertEquals(expected, actual);
    }

    @Test
    public void getIndexOrDefault_nullEntry_equalsExpected() {
        List<Integer> ix = Arrays.asList(8, null, 10);
        int expected = 2;
        int actual = getIndexOrDefault(null, expected);
        assertEquals(expected, actual);
    }

    @Test
    public void getIndexOrDefault_indexTooBig_equalsExpected() {
        List<Integer> ix = Arrays.asList(8, 9, 10);
        int expected = 12;
        int actual = getIndexOrDefault(null, expected);
        assertEquals(expected, actual);
    }
}
