package EasyOverrider;

import static EasyOverrider.ParamUsage.IGNORED_FOR_ALL;
import static EasyOverrider.ParamUsage.EQUALS_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.TOSTRING_ONLY;
import static EasyOverrider.ParamUsage.HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.EQUALS_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.EQUALS_AND_HASHCODE_ONLY;
import static EasyOverrider.ParamUsage.INCLUDED_IN_ALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests making sure things in the ParamUsage enum are working as intended.
 */
public class TestParamUsage {
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
        assertEquals(false, EQUALS_ONLY__UNSAFE.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_includedInEqualsOnly_true() {
        assertEquals(true, EQUALS_ONLY__UNSAFE.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_includedInEqualsOnly_true() {
        assertEquals(true, EQUALS_ONLY__UNSAFE.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_includedInEqualsOnly_false() {
        assertEquals(false, EQUALS_ONLY__UNSAFE.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_includedInEqualsOnly_true() {
        assertEquals(true, EQUALS_ONLY__UNSAFE.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_includedInEqualsOnly_false() {
        assertEquals(false, EQUALS_ONLY__UNSAFE.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_includedInHashCodeOnly_true() {
        assertEquals(true, HASHCODE_ONLY__UNSAFE.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_includedInHashCodeOnly_false() {
        assertEquals(false, HASHCODE_ONLY__UNSAFE.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_includedInHashCodeOnly_false() {
        assertEquals(false, HASHCODE_ONLY__UNSAFE.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_includedInHashCodeOnly_true() {
        assertEquals(true, HASHCODE_ONLY__UNSAFE.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_includedInHashCodeOnly_true() {
        assertEquals(true, HASHCODE_ONLY__UNSAFE.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_includedInHashCodeOnly_false() {
        assertEquals(false, HASHCODE_ONLY__UNSAFE.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_includedInToStringOnly_true() {
        assertEquals(true, TOSTRING_ONLY.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_includedInToStringOnly_false() {
        assertEquals(false, TOSTRING_ONLY.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_includedInToStringOnly_true() {
        assertEquals(true, TOSTRING_ONLY.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_includedInToStringOnly_false() {
        assertEquals(false, TOSTRING_ONLY.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_includedInToStringOnly_false() {
        assertEquals(false, TOSTRING_ONLY.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_includedInToStringOnly_true() {
        assertEquals(true, TOSTRING_ONLY.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_ignoredForEquals_true() {
        assertEquals(true, HASHCODE_AND_TOSTRING_ONLY__UNSAFE.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_ignoredForEquals_false() {
        assertEquals(false, HASHCODE_AND_TOSTRING_ONLY__UNSAFE.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_ignoredForEquals_false() {
        assertEquals(false, HASHCODE_AND_TOSTRING_ONLY__UNSAFE.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_ignoredForEquals_true() {
        assertEquals(true, HASHCODE_AND_TOSTRING_ONLY__UNSAFE.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_ignoredForEquals_false() {
        assertEquals(false, HASHCODE_AND_TOSTRING_ONLY__UNSAFE.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_ignoredForEquals_true() {
        assertEquals(true, HASHCODE_AND_TOSTRING_ONLY__UNSAFE.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_ignoredForHashCode_false() {
        assertEquals(false, EQUALS_AND_TOSTRING_ONLY__UNSAFE.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_ignoredForHashCode_true() {
        assertEquals(true, EQUALS_AND_TOSTRING_ONLY__UNSAFE.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_ignoredForHashCode_true() {
        assertEquals(true, EQUALS_AND_TOSTRING_ONLY__UNSAFE.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_ignoredForHashCode_false() {
        assertEquals(false, EQUALS_AND_TOSTRING_ONLY__UNSAFE.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_ignoredForHashCode_false() {
        assertEquals(false, EQUALS_AND_TOSTRING_ONLY__UNSAFE.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_ignoredForHashCode_true() {
        assertEquals(true, EQUALS_AND_TOSTRING_ONLY__UNSAFE.isToStringInclude());
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void isEqualsIgnore_ignoredForToString_false() {
        assertEquals(false, EQUALS_AND_HASHCODE_ONLY.isEqualsIgnore());
    }

    @Test
    public void isEqualsInclude_ignoredForToString_true() {
        assertEquals(true, EQUALS_AND_HASHCODE_ONLY.isEqualsInclude());
    }

    @Test
    public void isHashCodeIgnore_ignoredForToStringl_false() {
        assertEquals(false, EQUALS_AND_HASHCODE_ONLY.isHashCodeIgnore());
    }

    @Test
    public void isHashCodeInclude_ignoredForToString_true() {
        assertEquals(true, EQUALS_AND_HASHCODE_ONLY.isHashCodeInclude());
    }

    @Test
    public void isToStringIgnore_ignoredForToString_true() {
        assertEquals(true, EQUALS_AND_HASHCODE_ONLY.isToStringIgnore());
    }

    @Test
    public void isToStringInclude_ignoredForToString_false() {
        assertEquals(false, EQUALS_AND_HASHCODE_ONLY.isToStringInclude());
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
        ParamUsage toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_ignoredForAll_containsEquals() {
        ParamUsage toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_ignoredForAll_containsHashCode() {
        ParamUsage toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_ignoredForAll_containsToString() {
        ParamUsage toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"toString\".", actual.contains("toString"));
    }

    @Test
    public void toString_ignoredForAll_doesNotContainIncluded() {
        ParamUsage toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertFalse(name + ".toString() contains \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_ignoredForAll_containsIgnored() {
        ParamUsage toTest = IGNORED_FOR_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_includedInEqualsOnly_containsEnumName() {
        ParamUsage toTest = EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_includedInEqualsOnly_containsEquals() {
        ParamUsage toTest = EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_includedInEqualsOnly_containsHashCode() {
        ParamUsage toTest = EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_includedInEqualsOnly_containsToString() {
        ParamUsage toTest = EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"toString\".", actual.contains("toString"));
    }

    @Test
    public void toString_includedInEqualsOnly_containsIncluded() {
        ParamUsage toTest = EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_includedInEqualsOnly_containsIgnored() {
        ParamUsage toTest = EQUALS_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_includedInHashCodeOnly_containsEnumName() {
        ParamUsage toTest = HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsEquals() {
        ParamUsage toTest = HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsHashCode() {
        ParamUsage toTest = HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsToString() {
        ParamUsage toTest = HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"toString\".", actual.contains("toString"));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsIncluded() {
        ParamUsage toTest = HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_includedInHashCodeOnly_containsIgnored() {
        ParamUsage toTest = HASHCODE_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_includedInToStringOnly_containsEnumName() {
        ParamUsage toTest = TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_includedInToStringOnly_containsEquals() {
        ParamUsage toTest = TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_includedInToStringOnly_containsHashCode() {
        ParamUsage toTest = TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_includedInToStringOnly_containsToString() {
        ParamUsage toTest = TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"toString\".", actual.contains("toString"));
    }

    @Test
    public void toString_includedInToStringOnly_containsIncluded() {
        ParamUsage toTest = TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_includedInToStringOnly_containsIgnored() {
        ParamUsage toTest = TOSTRING_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_ignoredForEquals_containsEnumName() {
        ParamUsage toTest = HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_ignoredForEquals_containsEquals() {
        ParamUsage toTest = HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_ignoredForEquals_containsHashCode() {
        ParamUsage toTest = HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_ignoredForEquals_containsToString() {
        ParamUsage toTest = HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"toString\".", actual.contains("toString"));
    }

    @Test
    public void toString_ignoredForEquals_containsIncluded() {
        ParamUsage toTest = HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_ignoredForEquals_containsIgnored() {
        ParamUsage toTest = HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_ignoredForHashCode_containsEnumName() {
        ParamUsage toTest = EQUALS_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_ignoredForHashCode_containsEquals() {
        ParamUsage toTest = EQUALS_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_ignoredForHashCode_containsHashCode() {
        ParamUsage toTest = EQUALS_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_ignoredForHashCode_containsToString() {
        ParamUsage toTest = EQUALS_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"toString\".", actual.contains("toString"));
    }

    @Test
    public void toString_ignoredForHashCode_containsIncluded() {
        ParamUsage toTest = EQUALS_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_ignoredForHashCode_containsIgnored() {
        ParamUsage toTest = EQUALS_AND_TOSTRING_ONLY__UNSAFE;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_ignoredForToString_containsEnumName() {
        ParamUsage toTest = EQUALS_AND_HASHCODE_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_ignoredForToString_containsEquals() {
        ParamUsage toTest = EQUALS_AND_HASHCODE_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_ignoredForToString_containsHashCode() {
        ParamUsage toTest = EQUALS_AND_HASHCODE_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_ignoredForToString_containsToString() {
        ParamUsage toTest = EQUALS_AND_HASHCODE_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"toString\".", actual.contains("toString"));
    }

    @Test
    public void toString_ignoredForToString_containsIncluded() {
        ParamUsage toTest = EQUALS_AND_HASHCODE_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_ignoredForToString_containsIgnored() {
        ParamUsage toTest = EQUALS_AND_HASHCODE_ONLY;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void toString_includedInAll_containsEnumName() {
        ParamUsage toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"" + name + "\".", actual.contains(name));
    }

    @Test
    public void toString_includedInAll_containsEquals() {
        ParamUsage toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"equals\".", actual.contains("equals"));
    }

    @Test
    public void toString_includedInAll_containsHashCode() {
        ParamUsage toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"hashCode\".", actual.contains("hashCode"));
    }

    @Test
    public void toString_includedInAll_containsToString() {
        ParamUsage toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"toString\".", actual.contains("toString"));
    }

    @Test
    public void toString_includedInAll_containsIncluded() {
        ParamUsage toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertTrue(name + ".toString() does not contain \"Included\".", actual.contains("Included"));
    }

    @Test
    public void toString_includedInAll_doesNotContainIgnored() {
        ParamUsage toTest = INCLUDED_IN_ALL;
        String name = toTest.name();
        String actual = toTest.toString();
        assertFalse(name + ".toString() contains \"Ignored\".", actual.contains("Ignored"));
    }

    /* <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    @Test
    public void values_length_equals8() {
        //If this test fails, it means a value was added to or removed from the ParamMethodRestricition enum.
        //That means that appropriate tests need to be added or removed in here as well as updating this test appropriately.
        assertEquals(8, ParamUsage.values().length);
    }
}
