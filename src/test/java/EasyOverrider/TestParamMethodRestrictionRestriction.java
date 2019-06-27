package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_EQUALS_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;
import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_EQUALS__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_HASHCODE__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_TOSTRING;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static EasyOverrider.ParamMethodRestrictionRestriction.SAFE_ONLY;
import static EasyOverrider.ParamMethodRestrictionRestriction.ALLOW_UNSAFE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestParamMethodRestrictionRestriction {

    @Test
    public void values_exactlyTwoEntries() {
        assertEquals(2, ParamMethodRestrictionRestriction.values().length);
    }

    @Test
    public void allows_safeOnly_ignoredForAll_true() {
        assertTrue(SAFE_ONLY.allows(IGNORED_FOR_ALL));
    }

    @Test
    public void allows_safeOnly_includedInToStringOnly_true() {
        assertTrue(SAFE_ONLY.allows(INCLUDED_IN_TOSTRING_ONLY));
    }

    @Test
    public void allows_safeOnly_ignoredForToString_true() {
        assertTrue(SAFE_ONLY.allows(IGNORED_FOR_TOSTRING));
    }

    @Test
    public void allows_safeOnly_includedInAll_true() {
        assertTrue(SAFE_ONLY.allows(INCLUDED_IN_ALL));
    }

    @Test
    public void allows_safeOnly_includedInEqualsOnly_false() {
        assertFalse(SAFE_ONLY.allows(INCLUDED_IN_EQUALS_ONLY__UNSAFE));
    }

    @Test
    public void allows_safeOnly_includedInHashCodeOnly_false() {
        assertFalse(SAFE_ONLY.allows(INCLUDED_IN_HASHCODE_ONLY__UNSAFE));
    }

    @Test
    public void allows_safeOnly_ignoredForEquals_false() {
        assertFalse(SAFE_ONLY.allows(IGNORED_FOR_EQUALS__UNSAFE));
    }

    @Test
    public void allows_safeOnly_ignoredForHashCode_false() {
        assertFalse(SAFE_ONLY.allows(IGNORED_FOR_HASHCODE__UNSAFE));
    }

    @Test
    public void getAllowedParamMethodRestrictions_safeOnly_sizeEquals4() {
        assertEquals(4, SAFE_ONLY.getAllowedParamMethodRestrictions().size());
    }

    @Test
    public void getAllowedParamMethodRestrictions_safeOnly_noEntriesEndInUnsafe() {
        SAFE_ONLY.getAllowedParamMethodRestrictions().forEach(pmr -> {
            if (pmr.name().endsWith("UNSAFE")) {
                fail(pmr.name() + " is in the SAFE_ONLY ParamMethodRestrictionRestriction allowedParamMethodRestrictions list.");
            }
        });
    }

    @Test
    public void toString_safeOnly_equalsExpected() {
        String expected = "SAFE_ONLY[IGNORED_FOR_ALL,IGNORED_FOR_TOSTRING,INCLUDED_IN_ALL,INCLUDED_IN_TOSTRING_ONLY]";
        String actual = SAFE_ONLY.toString();
        assertEquals(expected, actual);
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void allows_allowUnsafe_ignoredForAll_true() {
        assertTrue(ALLOW_UNSAFE.allows(IGNORED_FOR_ALL));
    }

    @Test
    public void allows_allowUnsafe_includedInToStringOnly_true() {
        assertTrue(ALLOW_UNSAFE.allows(INCLUDED_IN_TOSTRING_ONLY));
    }

    @Test
    public void allows_allowUnsafe_ignoredForToString_true() {
        assertTrue(ALLOW_UNSAFE.allows(IGNORED_FOR_TOSTRING));
    }

    @Test
    public void allows_allowUnsafe_includedInAll_true() {
        assertTrue(ALLOW_UNSAFE.allows(INCLUDED_IN_ALL));
    }

    @Test
    public void allows_allowUnsafe_includedInEqualsOnly_true() {
        assertTrue(ALLOW_UNSAFE.allows(INCLUDED_IN_EQUALS_ONLY__UNSAFE));
    }

    @Test
    public void allows_allowUnsafe_includedInHashCodeOnly_true() {
        assertTrue(ALLOW_UNSAFE.allows(INCLUDED_IN_HASHCODE_ONLY__UNSAFE));
    }

    @Test
    public void allows_allowUnsafe_ignoredForEquals_true() {
        assertTrue(ALLOW_UNSAFE.allows(IGNORED_FOR_EQUALS__UNSAFE));
    }

    @Test
    public void allows_allowUnsafe_ignoredForHashCode_true() {
        assertTrue(ALLOW_UNSAFE.allows(IGNORED_FOR_HASHCODE__UNSAFE));
    }

    @Test
    public void getAllowedParamMethodRestrictions_allowUnsafe_sizeEqualsParamMethodRestrictionsValuesLength() {
        assertEquals(ParamMethodRestriction.values().length, ALLOW_UNSAFE.getAllowedParamMethodRestrictions().size());
    }

    @Test
    public void toString_allowUnsafe_equalsExpected() {
        String expected = "ALLOW_UNSAFE[IGNORED_FOR_ALL,IGNORED_FOR_EQUALS__UNSAFE,IGNORED_FOR_HASHCODE__UNSAFE,IGNORED_FOR_TOSTRING," +
                          "INCLUDED_IN_ALL,INCLUDED_IN_EQUALS_ONLY__UNSAFE,INCLUDED_IN_HASHCODE_ONLY__UNSAFE,INCLUDED_IN_TOSTRING_ONLY]";
        String actual = ALLOW_UNSAFE.toString();
        assertEquals(expected, actual);
    }
}
