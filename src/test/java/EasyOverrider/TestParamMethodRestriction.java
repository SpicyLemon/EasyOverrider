package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_EQUALS_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;
import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_EQUALS__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_HASHCODE__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_TOSTRING;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests making sure things in the ParamMethodRestriction enum are working as intended.
 */
public class TestParamMethodRestriction {
    @Test
    public void isEqualsIgnore_ignoreForAll_true() {
        assertEquals(true, IGNORED_FOR_ALL.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_ignoreForAll_false() {
        assertEquals(false, IGNORED_FOR_ALL.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_ignoreForAll_true() {
        assertEquals(true, IGNORED_FOR_ALL.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_ignoreForAll_false() {
        assertEquals(false, IGNORED_FOR_ALL.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_ignoreForAll_true() {
        assertEquals(true, IGNORED_FOR_ALL.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_ignoreForAll_false() {
        assertEquals(false, IGNORED_FOR_ALL.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_includedInEqualsOnly_false() {
        assertEquals(false, INCLUDED_IN_EQUALS_ONLY__UNSAFE.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_includedInEqualsOnly_true() {
        assertEquals(true, INCLUDED_IN_EQUALS_ONLY__UNSAFE.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_includedInEqualsOnly_true() {
        assertEquals(true, INCLUDED_IN_EQUALS_ONLY__UNSAFE.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_includedInEqualsOnly_false() {
        assertEquals(false, INCLUDED_IN_EQUALS_ONLY__UNSAFE.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_includedInEqualsOnly_true() {
        assertEquals(true, INCLUDED_IN_EQUALS_ONLY__UNSAFE.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_includedInEqualsOnly_false() {
        assertEquals(false, INCLUDED_IN_EQUALS_ONLY__UNSAFE.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_includedInHashCodeOnly_true() {
        assertEquals(true, INCLUDED_IN_HASHCODE_ONLY__UNSAFE.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_includedInHashCodeOnly_false() {
        assertEquals(false, INCLUDED_IN_HASHCODE_ONLY__UNSAFE.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_includedInHashCodeOnly_false() {
        assertEquals(false, INCLUDED_IN_HASHCODE_ONLY__UNSAFE.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_includedInHashCodeOnly_true() {
        assertEquals(true, INCLUDED_IN_HASHCODE_ONLY__UNSAFE.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_includedInHashCodeOnly_true() {
        assertEquals(true, INCLUDED_IN_HASHCODE_ONLY__UNSAFE.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_includedInHashCodeOnly_false() {
        assertEquals(false, INCLUDED_IN_HASHCODE_ONLY__UNSAFE.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_includedInToStringOnly_true() {
        assertEquals(true, INCLUDED_IN_TOSTRING_ONLY.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_includedInToStringOnly_false() {
        assertEquals(false, INCLUDED_IN_TOSTRING_ONLY.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_includedInToStringOnly_true() {
        assertEquals(true, INCLUDED_IN_TOSTRING_ONLY.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_includedInToStringOnly_false() {
        assertEquals(false, INCLUDED_IN_TOSTRING_ONLY.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_includedInToStringOnly_false() {
        assertEquals(false, INCLUDED_IN_TOSTRING_ONLY.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_includedInToStringOnly_true() {
        assertEquals(true, INCLUDED_IN_TOSTRING_ONLY.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_ignoredForEquals_true() {
        assertEquals(true, IGNORED_FOR_EQUALS__UNSAFE.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_ignoredForEquals_false() {
        assertEquals(false, IGNORED_FOR_EQUALS__UNSAFE.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_ignoredForEquals_false() {
        assertEquals(false, IGNORED_FOR_EQUALS__UNSAFE.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_ignoredForEquals_true() {
        assertEquals(true, IGNORED_FOR_EQUALS__UNSAFE.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_ignoredForEquals_false() {
        assertEquals(false, IGNORED_FOR_EQUALS__UNSAFE.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_ignoredForEquals_true() {
        assertEquals(true, IGNORED_FOR_EQUALS__UNSAFE.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_ignoredForHashCode_false() {
        assertEquals(false, IGNORED_FOR_HASHCODE__UNSAFE.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_ignoredForHashCode_true() {
        assertEquals(true, IGNORED_FOR_HASHCODE__UNSAFE.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_ignoredForHashCode_true() {
        assertEquals(true, IGNORED_FOR_HASHCODE__UNSAFE.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_ignoredForHashCode_false() {
        assertEquals(false, IGNORED_FOR_HASHCODE__UNSAFE.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_ignoredForHashCode_false() {
        assertEquals(false, IGNORED_FOR_HASHCODE__UNSAFE.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_ignoredForHashCode_true() {
        assertEquals(true, IGNORED_FOR_HASHCODE__UNSAFE.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_ignoredForToString_false() {
        assertEquals(false, IGNORED_FOR_TOSTRING.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_ignoredForToString_true() {
        assertEquals(true, IGNORED_FOR_TOSTRING.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_ignoredForToStringl_false() {
        assertEquals(false, IGNORED_FOR_TOSTRING.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_ignoredForToString_true() {
        assertEquals(true, IGNORED_FOR_TOSTRING.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_ignoredForToString_true() {
        assertEquals(true, IGNORED_FOR_TOSTRING.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_ignoredForToString_false() {
        assertEquals(false, IGNORED_FOR_TOSTRING.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_includedInAll_false() {
        assertEquals(false, INCLUDED_IN_ALL.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_includedInAll_true() {
        assertEquals(true, INCLUDED_IN_ALL.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_includedInAll_false() {
        assertEquals(false, INCLUDED_IN_ALL.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_includedInAll_true() {
        assertEquals(true, INCLUDED_IN_ALL.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_includedInAll_false() {
        assertEquals(false, INCLUDED_IN_ALL.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_includedInAll_true() {
        assertEquals(true, INCLUDED_IN_ALL.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_ignoredForAll_containsEnumName() {
        ParamMethodRestriction toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_ignoredForAll_containsEquals() {
        ParamMethodRestriction toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_ignoredForAll_containsHashCode() {
        ParamMethodRestriction toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_ignoredForAll_containsToString() {
        ParamMethodRestriction toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"paramValueToString\".", actual.contains("paramValueToString"));
    }

    @Test
    public void toString_ignoredForAll_doesNotContainIncluded() {
        ParamMethodRestriction toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertFalse(name + ".paramValueToString() contains \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_ignoredForAll_containsIgnored() {
        ParamMethodRestriction toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_includedInEqualsOnly_containsEnumName() {
        ParamMethodRestriction toTest = INCLUDED_IN_EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_includedInEqualsOnly_containsEquals() {
        ParamMethodRestriction toTest = INCLUDED_IN_EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_includedInEqualsOnly_containsHashCode() {
        ParamMethodRestriction toTest = INCLUDED_IN_EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_includedInEqualsOnly_containsToString() {
        ParamMethodRestriction toTest = INCLUDED_IN_EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"paramValueToString\".", actual.contains("paramValueToString"));
    }

    @Test
    public void toString_includedInEqualsOnly_containsIncluded() {
        ParamMethodRestriction toTest = INCLUDED_IN_EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_includedInEqualsOnly_containsIgnored() {
        ParamMethodRestriction toTest = INCLUDED_IN_EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_includedInHashCodeOnly_containsEnumName() {
        ParamMethodRestriction toTest = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsEquals() {
        ParamMethodRestriction toTest = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsHashCode() {
        ParamMethodRestriction toTest = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsToString() {
        ParamMethodRestriction toTest = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"paramValueToString\".", actual.contains("paramValueToString"));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsIncluded() {
        ParamMethodRestriction toTest = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsIgnored() {
        ParamMethodRestriction toTest = INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_includedInToStringOnly_containsEnumName() {
        ParamMethodRestriction toTest = INCLUDED_IN_TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_includedInToStringOnly_containsEquals() {
        ParamMethodRestriction toTest = INCLUDED_IN_TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_includedInToStringOnly_containsHashCode() {
        ParamMethodRestriction toTest = INCLUDED_IN_TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_includedInToStringOnly_containsToString() {
        ParamMethodRestriction toTest = INCLUDED_IN_TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"paramValueToString\".", actual.contains("paramValueToString"));
    }

    @Test
    public void toString_includedInToStringOnly_containsIncluded() {
        ParamMethodRestriction toTest = INCLUDED_IN_TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_includedInToStringOnly_containsIgnored() {
        ParamMethodRestriction toTest = INCLUDED_IN_TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_ignoredForEquals_containsEnumName() {
        ParamMethodRestriction toTest = IGNORED_FOR_EQUALS__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_ignoredForEquals_containsEquals() {
        ParamMethodRestriction toTest = IGNORED_FOR_EQUALS__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_ignoredForEquals_containsHashCode() {
        ParamMethodRestriction toTest = IGNORED_FOR_EQUALS__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_ignoredForEquals_containsToString() {
        ParamMethodRestriction toTest = IGNORED_FOR_EQUALS__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"paramValueToString\".", actual.contains("paramValueToString"));
    }

    @Test
    public void toString_ignoredForEquals_containsIncluded() {
        ParamMethodRestriction toTest = IGNORED_FOR_EQUALS__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_ignoredForEquals_containsIgnored() {
        ParamMethodRestriction toTest = IGNORED_FOR_EQUALS__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_ignoredForHashCode_containsEnumName() {
        ParamMethodRestriction toTest = IGNORED_FOR_HASHCODE__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_ignoredForHashCode_containsEquals() {
        ParamMethodRestriction toTest = IGNORED_FOR_HASHCODE__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_ignoredForHashCode_containsHashCode() {
        ParamMethodRestriction toTest = IGNORED_FOR_HASHCODE__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_ignoredForHashCode_containsToString() {
        ParamMethodRestriction toTest = IGNORED_FOR_HASHCODE__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"paramValueToString\".", actual.contains("paramValueToString"));
    }

    @Test
    public void toString_ignoredForHashCode_containsIncluded() {
        ParamMethodRestriction toTest = IGNORED_FOR_HASHCODE__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_ignoredForHashCode_containsIgnored() {
        ParamMethodRestriction toTest = IGNORED_FOR_HASHCODE__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_ignoredForToString_containsEnumName() {
        ParamMethodRestriction toTest = IGNORED_FOR_TOSTRING;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_ignoredForToString_containsEquals() {
        ParamMethodRestriction toTest = IGNORED_FOR_TOSTRING;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_ignoredForToString_containsHashCode() {
        ParamMethodRestriction toTest = IGNORED_FOR_TOSTRING;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_ignoredForToString_containsToString() {
        ParamMethodRestriction toTest = IGNORED_FOR_TOSTRING;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"paramValueToString\".", actual.contains("paramValueToString"));
    }

    @Test
    public void toString_ignoredForToString_containsIncluded() {
        ParamMethodRestriction toTest = IGNORED_FOR_TOSTRING;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_ignoredForToString_containsIgnored() {
        ParamMethodRestriction toTest = IGNORED_FOR_TOSTRING;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_includedInAll_containsEnumName() {
        ParamMethodRestriction toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_includedInAll_containsEquals() {
        ParamMethodRestriction toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_includedInAll_containsHashCode() {
        ParamMethodRestriction toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_includedInAll_containsToString() {
        ParamMethodRestriction toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"paramValueToString\".", actual.contains("paramValueToString"));
    }

    @Test
    public void toString_includedInAll_containsIncluded() {
        ParamMethodRestriction toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".paramValueToString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_includedInAll_doesNotContainIgnored() {
        ParamMethodRestriction toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertFalse(name + ".paramValueToString() contains \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void values_length_equals8() {
        //If this test fails, it means a value was added to or removed from the ParamMethodRestricition enum.
        //That means that appropriate tests need to be added or removed in here as well as updating this test appropriately.
        assertEquals(8, ParamMethodRestriction.values().length);
    }
}
