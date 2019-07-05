package EasyOverrider;

import static EasyOverrider.TestingUtils.Helpers.testSetterGetter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.IllegalFormatException;
import java.util.function.Function;

public class TestParamListServiceConfig {

    //TODO: Finish writing tests.

    @Test
    public void constructor_noArgs_allDefaults() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        assertEquals("null", config.getStringForNull());
        assertEquals("...", config.getStringForRecursionPrevented());
        assertEquals(" ", config.getStringForEmptyParamList());
        assertEquals(", ", config.getParameterDelimiter());
        assertEquals("%1$s=%2$s", config.getNameValueFormat());
        assertEquals("'%1$s'", config.getParameterValueFormat());
        assertEquals("%1$s@%2$s [%3$s]", config.getToStringFormat());
        assertEquals("String", config.getClassNameGetter().apply(String.class));
        assertEquals("b", config.getHashCodeToString().apply(11));
    }

    @Test
    public void constructor_allArgs_correctlySet() {
        String expectedNull = "<NULL>";
        String expectedRecursive = "LOOPY";
        String expectedEmpty = "NOPE";
        String expectedDelim = " <|> ";
        String expectedNVF = "%2$s is the value in %1$s";
        String expectedPVF = "{[{[%s]}]}";
        String expectedTSF = "%2$s is a %1$s with value -%3$s-";
        Function<Class, String> expectedCNG = Class::getCanonicalName;
        Function<Integer, String> expectedHCTS = String::valueOf;
        ParamListServiceConfig config = new ParamListServiceConfig(
                        expectedNull, expectedRecursive, expectedEmpty, expectedDelim,
                        expectedNVF, expectedPVF, expectedTSF, expectedCNG, expectedHCTS);
        assertEquals(expectedNull, config.getStringForNull());
        assertEquals(expectedRecursive, config.getStringForRecursionPrevented());
        assertEquals(expectedEmpty, config.getStringForEmptyParamList());
        assertEquals(expectedDelim, config.getParameterDelimiter());
        assertEquals(expectedNVF, config.getNameValueFormat());
        assertEquals(expectedPVF, config.getParameterValueFormat());
        assertEquals(expectedTSF, config.getToStringFormat());
        assertEquals(expectedCNG, config.getClassNameGetter());
        assertEquals(expectedHCTS, config.getHashCodeToString());
    }

    @Test
    public void constructor_nullArgs_keepDefaultValues() {
        ParamListServiceConfig config = new ParamListServiceConfig(null, null, null, null, null, null, null, null, null);
        assertEquals("null", config.getStringForNull());
        assertEquals("...", config.getStringForRecursionPrevented());
        assertEquals(" ", config.getStringForEmptyParamList());
        assertEquals(", ", config.getParameterDelimiter());
        assertEquals("%1$s=%2$s", config.getNameValueFormat());
        assertEquals("'%1$s'", config.getParameterValueFormat());
        assertEquals("%1$s@%2$s [%3$s]", config.getToStringFormat());
        assertEquals("String", config.getClassNameGetter().apply(String.class));
        assertEquals("b", config.getHashCodeToString().apply(11));
    }

    @Test
    public void constructor_badNameValueFormat_boom() {
        try {
            ParamListServiceConfig config = new ParamListServiceConfig(null, null, null, null, "%s%s%s%s%s", null, null, null, null);
            fail("Should have died.");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain setNameValueFormat", e.getMessage().contains("setNameValueFormat"));
            assertNotNull("Cause should have something.", e.getCause());
            assertTrue("Cause should be an IllegalFormatException", e.getCause() instanceof IllegalFormatException);
        }
    }

    @Test
    public void constructor_badParameterValueFormat_boom() {
        try {
            ParamListServiceConfig config = new ParamListServiceConfig(null, null, null, null, null, "%s%s%s%s%s", null, null, null);
            fail("Should have died.");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain setParameterValueFormat", e.getMessage().contains("setParameterValueFormat"));
            assertNotNull("Cause should have something.", e.getCause());
            assertTrue("Cause should be an IllegalFormatException", e.getCause() instanceof IllegalFormatException);
        }
    }

    @Test
    public void constructor_badToStringFormat_boom() {
        try {
            ParamListServiceConfig config = new ParamListServiceConfig(null, null, null, null, null, null, "%s%s%s%s%s", null, null);
            fail("Should have died.");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain setToStringFormat", e.getMessage().contains("setToStringFormat"));
            assertNotNull("Cause should have something.", e.getCause());
            assertTrue("Cause should be an IllegalFormatException", e.getCause() instanceof IllegalFormatException);
        }
    }

    @Test
    public void constructor_copy_argsCopied() {
        ParamListServiceConfig config = new ParamListServiceConfig()
                        .setStringForNull("String for null")
                        .setStringForRecursionPrevented("String for recursion Prevented");
        ParamListServiceConfig copy = new ParamListServiceConfig(config);
        assertEquals("String for null", config.getStringForNull());
        assertEquals("String for recursion Prevented", config.getStringForRecursionPrevented());
        assertEquals(" ", config.getStringForEmptyParamList());
        assertEquals(", ", config.getParameterDelimiter());
        assertEquals("%1$s=%2$s", config.getNameValueFormat());
        assertEquals("'%1$s'", config.getParameterValueFormat());
        assertEquals("%1$s@%2$s [%3$s]", config.getToStringFormat());
        assertEquals("String", config.getClassNameGetter().apply(String.class));
        assertEquals("b", config.getHashCodeToString().apply(11));
    }

    @Test
    public void copyOf_aFewSet_argsCopied() {
        ParamListServiceConfig config = new ParamListServiceConfig()
                        .setStringForNull("String for null 2")
                        .setStringForRecursionPrevented("String for recursion Prevented 2");
        ParamListServiceConfig copy = config.copyOf();
        assertEquals("String for null 2", config.getStringForNull());
        assertEquals("String for recursion Prevented 2", config.getStringForRecursionPrevented());
        assertEquals(" ", config.getStringForEmptyParamList());
        assertEquals(", ", config.getParameterDelimiter());
        assertEquals("%1$s=%2$s", config.getNameValueFormat());
        assertEquals("'%1$s'", config.getParameterValueFormat());
        assertEquals("%1$s@%2$s [%3$s]", config.getToStringFormat());
        assertEquals("String", config.getClassNameGetter().apply(String.class));
        assertEquals("b", config.getHashCodeToString().apply(11));
    }

    @Test
    public void stringForNullGetterSetter_newValue_getsSet() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        testSetterGetter("stringForNull", config, ParamListServiceConfig::setStringForNull,
                         "NIL", ParamListServiceConfig::getStringForNull);
    }

    @Test
    public void setStringForNull_null_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setStringForNull(null);
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain position.", e.getMessage().contains(" 1 "));
            assertTrue("Message should contain paramName.", e.getMessage().contains("stringForNull"));
            assertTrue("Message should contain methodName.", e.getMessage().contains("setStringForNull"));
        }
    }

    @Test
    public void stringForRecursionPreventedGetterSetter_newValue_getsSet() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        testSetterGetter("stringForRecursionPrevented", config, ParamListServiceConfig::setStringForRecursionPrevented,
                         "Booyah!", ParamListServiceConfig::getStringForRecursionPrevented);
    }

    @Test
    public void setStringForRecursionPrevented_null_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setStringForRecursionPrevented(null);
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain position.", e.getMessage().contains(" 1 "));
            assertTrue("Message should contain paramName.", e.getMessage().contains("stringForRecursionPrevented"));
            assertTrue("Message should contain methodName.", e.getMessage().contains("setStringForRecursionPrevented"));
        }
    }

    @Test
    public void stringForEmptyParamListGetterSetter_newValue_getsSet() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        testSetterGetter("stringForEmptyParamList", config, ParamListServiceConfig::setStringForEmptyParamList,
                         "Nope", ParamListServiceConfig::getStringForEmptyParamList);
    }

    @Test
    public void setStringForEmptyParamList_null_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setStringForEmptyParamList(null);
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain position.", e.getMessage().contains(" 1 "));
            assertTrue("Message should contain paramName.", e.getMessage().contains("stringForEmptyParamList"));
            assertTrue("Message should contain methodName.", e.getMessage().contains("setStringForEmptyParamList"));
        }
    }

    @Test
    public void parameterDelimiterGetterSetter_newValue_getsSet() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        testSetterGetter("parameterDelimiter", config, ParamListServiceConfig::setParameterDelimiter,
                         "**", ParamListServiceConfig::getParameterDelimiter);
    }

    @Test
    public void setParameterDelimiter_null_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setParameterDelimiter(null);
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain position.", e.getMessage().contains(" 1 "));
            assertTrue("Message should contain paramName.", e.getMessage().contains("parameterDelimiter"));
            assertTrue("Message should contain methodName.", e.getMessage().contains("setParameterDelimiter"));
        }
    }

    @Test
    public void nameValueFormatGetterSetter_newValue_getsSet() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        testSetterGetter("nameValueFormat", config, ParamListServiceConfig::setNameValueFormat,
                         "name=%s, value=%s", ParamListServiceConfig::getNameValueFormat);
    }

    @Test
    public void setNameValueFormat_null_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setNameValueFormat(null);
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain position.", e.getMessage().contains(" 1 "));
            assertTrue("Message should contain paramName.", e.getMessage().contains("nameValueFormat"));
            assertTrue("Message should contain methodName.", e.getMessage().contains("setNameValueFormat"));
        }
    }

    @Test
    public void setNameValueFormat_bad_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setNameValueFormat("%s%s%s%s%s%s%s%s");
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain methodName.", e.getMessage().contains("setNameValueFormat"));
            assertNotNull("There must be a cause.", e.getCause());
            assertTrue("Cause should be an IllegalFormatException.", e.getCause() instanceof IllegalFormatException);
        }
    }

    @Test
    public void parameterValueFormatGetterSetter_newValue_getsSet() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        testSetterGetter("parameterValueFormat", config, ParamListServiceConfig::setParameterValueFormat,
                         "^%s^", ParamListServiceConfig::getParameterValueFormat);
    }

    @Test
    public void setParameterValueFormat_null_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setParameterValueFormat(null);
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain position.", e.getMessage().contains(" 1 "));
            assertTrue("Message should contain paramName.", e.getMessage().contains("parameterValueFormat"));
            assertTrue("Message should contain methodName.", e.getMessage().contains("setParameterValueFormat"));
        }
    }

    @Test
    public void setParameterValueFormat_bad_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setParameterValueFormat("%s%s%s%s%s%s%s%s");
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain methodName.", e.getMessage().contains("setParameterValueFormat"));
            assertNotNull("There must be a cause.", e.getCause());
            assertTrue("Cause should be an IllegalFormatException.", e.getCause() instanceof IllegalFormatException);
        }
    }

    @Test
    public void toStringFormatGetterSetter_newValue_getsSet() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        testSetterGetter("toStringFormat", config, ParamListServiceConfig::setToStringFormat,
                         "^%s^ !%s! :%s:", ParamListServiceConfig::getToStringFormat);
    }

    @Test
    public void setToStringFormat_null_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setToStringFormat(null);
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain position.", e.getMessage().contains(" 1 "));
            assertTrue("Message should contain paramName.", e.getMessage().contains("toStringFormat"));
            assertTrue("Message should contain methodName.", e.getMessage().contains("setToStringFormat"));
        }
    }

    @Test
    public void setToStringFormat_bad_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setToStringFormat("%s%s%s%s%s%s%s%s");
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain methodName.", e.getMessage().contains("setToStringFormat"));
            assertNotNull("There must be a cause.", e.getCause());
            assertTrue("Cause should be an IllegalFormatException.", e.getCause() instanceof IllegalFormatException);
        }
    }

    @Test
    public void setClassNameGetterGetterSetter_newValue_getsSet() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        testSetterGetter("classNameGetter", config, ParamListServiceConfig::setClassNameGetter,
                         (c) -> ">" + c.getSimpleName() + "<", ParamListServiceConfig::getClassNameGetter);
    }

    @Test
    public void setClassNameGetter_null_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setClassNameGetter(null);
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain position.", e.getMessage().contains(" 1 "));
            assertTrue("Message should contain paramName.", e.getMessage().contains("classNameGetter"));
            assertTrue("Message should contain methodName.", e.getMessage().contains("setClassNameGetter"));
        }
    }

    @Test
    public void setHashCodeToStringGetterSetter_newValue_getsSet() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        testSetterGetter("hashCodeToString", config, ParamListServiceConfig::setHashCodeToString,
                         (i) -> "eight + " + i, ParamListServiceConfig::getHashCodeToString);
    }

    @Test
    public void setHashCodeToString_null_boom() {
        ParamListServiceConfig config = new ParamListServiceConfig();
        try {
            config.setHashCodeToString(null);
            fail("Should boom");
        } catch (IllegalArgumentException e) {
            assertTrue("Message should contain position.", e.getMessage().contains(" 1 "));
            assertTrue("Message should contain paramName.", e.getMessage().contains("hashCodeToString"));
            assertTrue("Message should contain methodName.", e.getMessage().contains("setHashCodeToString"));
        }
    }
}
