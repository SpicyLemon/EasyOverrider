package EasyOverrider;

import static EasyOverrider.ParamUsage.IGNORED_FOR_ALL;
import static EasyOverrider.ParamUsage.EQUALS_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.TOSTRING_ONLY;
import static EasyOverrider.ParamUsage.HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.EQUALS_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.EQUALS_AND_HASHCODE_ONLY;
import static EasyOverrider.ParamUsage.INCLUDED_IN_ALL;
import static EasyOverrider.ParamUsageRestriction.SAFE_ONLY;
import static EasyOverrider.ParamUsageRestriction.ALLOW_UNSAFE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestParamUsageRestriction {

    @Test
    public void values_exactlyTwoEntries() {
        assertEquals(2, ParamUsageRestriction.values().length);
    }

    @Test
    public void allows_safeOnly_ignoredForAll_true() {
        assertTrue(SAFE_ONLY.allows(IGNORED_FOR_ALL));
    }

    @Test
    public void allows_safeOnly_includedInToStringOnly_true() {
        assertTrue(SAFE_ONLY.allows(TOSTRING_ONLY));
    }

    @Test
    public void allows_safeOnly_ignoredForToString_true() {
        assertTrue(SAFE_ONLY.allows(EQUALS_AND_HASHCODE_ONLY));
    }

    @Test
    public void allows_safeOnly_includedInAll_true() {
        assertTrue(SAFE_ONLY.allows(INCLUDED_IN_ALL));
    }

    @Test
    public void allows_safeOnly_includedInEqualsOnly_false() {
        assertFalse(SAFE_ONLY.allows(EQUALS_ONLY__UNSAFE));
    }

    @Test
    public void allows_safeOnly_includedInHashCodeOnly_false() {
        assertFalse(SAFE_ONLY.allows(HASHCODE_ONLY__UNSAFE));
    }

    @Test
    public void allows_safeOnly_ignoredForEquals_false() {
        assertFalse(SAFE_ONLY.allows(HASHCODE_AND_TOSTRING_ONLY__UNSAFE));
    }

    @Test
    public void allows_safeOnly_ignoredForHashCode_false() {
        assertFalse(SAFE_ONLY.allows(EQUALS_AND_TOSTRING_ONLY__UNSAFE));
    }

    @Test
    public void getAllowedParamMethodRestrictions_safeOnly_sizeEquals4() {
        assertEquals(4, SAFE_ONLY.getAllowedParamMethodRestrictions().size());
    }

    @Test
    public void getAllowedParamMethodRestrictions_safeOnly_noEntriesEndInUnsafe() {
        SAFE_ONLY.getAllowedParamMethodRestrictions().forEach(pmr -> {
            if (pmr.name().endsWith("UNSAFE")) {
                fail(pmr.name() + " is in the SAFE_ONLY ParamUsageRestriction allowedParamMethodRestrictions list.");
            }
        });
    }

    @Test
    public void toString_safeOnly_equalsExpected() {
        List<String> expecteds = Arrays.asList(IGNORED_FOR_ALL.name(),
                                               EQUALS_AND_HASHCODE_ONLY.name(),
                                               INCLUDED_IN_ALL.name(),
                                               TOSTRING_ONLY.name());
        String actual = SAFE_ONLY.toString();
        for(String expected : expecteds) {
            assertTrue("'" + expected + "' not in '" + actual + "'", actual.contains(expected));
        }
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void allows_allowUnsafe_ignoredForAll_true() {
        assertTrue(ALLOW_UNSAFE.allows(IGNORED_FOR_ALL));
    }

    @Test
    public void allows_allowUnsafe_includedInToStringOnly_true() {
        assertTrue(ALLOW_UNSAFE.allows(TOSTRING_ONLY));
    }

    @Test
    public void allows_allowUnsafe_ignoredForToString_true() {
        assertTrue(ALLOW_UNSAFE.allows(EQUALS_AND_HASHCODE_ONLY));
    }

    @Test
    public void allows_allowUnsafe_includedInAll_true() {
        assertTrue(ALLOW_UNSAFE.allows(INCLUDED_IN_ALL));
    }

    @Test
    public void allows_allowUnsafe_includedInEqualsOnly_true() {
        assertTrue(ALLOW_UNSAFE.allows(EQUALS_ONLY__UNSAFE));
    }

    @Test
    public void allows_allowUnsafe_includedInHashCodeOnly_true() {
        assertTrue(ALLOW_UNSAFE.allows(HASHCODE_ONLY__UNSAFE));
    }

    @Test
    public void allows_allowUnsafe_ignoredForEquals_true() {
        assertTrue(ALLOW_UNSAFE.allows(HASHCODE_AND_TOSTRING_ONLY__UNSAFE));
    }

    @Test
    public void allows_allowUnsafe_ignoredForHashCode_true() {
        assertTrue(ALLOW_UNSAFE.allows(EQUALS_AND_TOSTRING_ONLY__UNSAFE));
    }

    @Test
    public void getAllowedParamMethodRestrictions_allowUnsafe_sizeEqualsParamMethodRestrictionsValuesLength() {
        assertEquals(ParamUsage.values().length, ALLOW_UNSAFE.getAllowedParamMethodRestrictions().size());
    }

    @Test
    public void toString_allowUnsafe_equalsExpected() {
        List<String> expecteds = Arrays.asList(IGNORED_FOR_ALL.name(),
                                               EQUALS_AND_HASHCODE_ONLY.name(),
                                               INCLUDED_IN_ALL.name(),
                                               TOSTRING_ONLY.name(),
                                               EQUALS_ONLY__UNSAFE.name(),
                                               HASHCODE_ONLY__UNSAFE.name(),
                                               EQUALS_AND_TOSTRING_ONLY__UNSAFE.name(),
                                               HASHCODE_AND_TOSTRING_ONLY__UNSAFE.name());
        String actual = ALLOW_UNSAFE.toString();
        for(String expected : expecteds) {
            assertTrue("'" + expected + "' not in '" + actual + "'", actual.contains(expected));
        }
    }
}
