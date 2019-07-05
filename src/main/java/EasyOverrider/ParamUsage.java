package EasyOverrider;

/**
 * Enum to make it easier to dictate which methods a parameter should be included in.
 * <ul>
 * <li>{@link #INCLUDED_IN_ALL}
 * <li>{@link #TOSTRING_ONLY}
 * <li>{@link #EQUALS_AND_HASHCODE_ONLY}
 * <li>{@link #IGNORED_FOR_ALL}
 * <li>{@link #EQUALS_ONLY__UNSAFE}
 * <li>{@link #HASHCODE_ONLY__UNSAFE}
 * <li>{@link #HASHCODE_AND_TOSTRING_ONLY__UNSAFE}
 * <li>{@link #EQUALS_AND_TOSTRING_ONLY__UNSAFE}
 * </ul>
 *
 * Entries that end in <code>__UNSAFE</code> should only be used in extreme circumstances.<br>
 *
 * The rule is that if <code>objA.equals(objB)</code> then <code>objA.hashCode()</code> must equal <code>objB.hashCode()</code>.
 * By including a parameter in equals but not hashCode, there's a very good chance of breaking that part of the contract.
 * By including a parameter in hashCode but not equals, you run the risk of things not working right when they rely on the hashCode.
 * For example, a hashSet might falsely identify two different entries as the same.
 */
public enum ParamUsage {

    /**
     * Indicates a parameter that should be included in all of the equals(), hashCode(), and toString() methods.
     */
    INCLUDED_IN_ALL(true, true, true),

    /**
     * Indicates a parameter that should be included in the equals() method, but not in the hashCode() or toString() methods.
     */
    EQUALS_ONLY__UNSAFE(true, false, false),

    /**
     * Indicates a parameter that should be included in the hashCode() method, but not in the equals() or toString() methods.
     */
    HASHCODE_ONLY__UNSAFE(false, true, false),

    /**
     * Indicates a parameter that should be included in the toString() method, but not in the equals() or hashCode() methods.
     */
    TOSTRING_ONLY(false, false, true),

    /**
     * Indicates a parameter that should be included in the equals() and hashCode() methods, but not in the toString() method.
     */
    EQUALS_AND_HASHCODE_ONLY(true, true, false),

    /**
     * Indicates a parameter that should be included in the equals() and toString() methods, but not in the hashCode() method.
     */
    EQUALS_AND_TOSTRING_ONLY__UNSAFE(true, false, true),

    /**
     * Indicates a parameter that should be included in the hashCode() and toString() methods, but not in the equals() method.
     */
    HASHCODE_AND_TOSTRING_ONLY__UNSAFE(false, true, true),

    /**
     * Indicates a parameter that should not be included in any of the equals(), hashCode(), or toString() methods.
     */
    IGNORED_FOR_ALL(false, false, false);

    private final boolean inEquals;
    private final boolean inHashCode;
    private final boolean inToString;

    ParamUsage(final boolean inEquals, final boolean inHashCode, final boolean inToString) {
        this.inEquals = inEquals;
        this.inHashCode = inHashCode;
        this.inToString = inToString;
    }

    /**
     * Whether or not the parameter associated with this should be ignored in the equals method.
     *
     * @return True if it should be ignored. False if it should be included.
     * @see #isEqualsInclude()
     */
    public boolean isEqualsIgnore() {
        return !inEquals;
    }

    /**
     * Whether or not the parameter associated with this should be included in the equals method.
     *
     * @return True if it should be included. False if it should be ignored.
     * @see #isEqualsIgnore()
     */
    public boolean isEqualsInclude() {
        return inEquals;
    }

    /**
     * Whether or not the parameter associated with this should be ignored in the hashCode() method.
     *
     * @return True if it should be ignored. False if it should be included.
     * @see #isHashCodeInclude()
     */
    public boolean isHashCodeIgnore() {
        return !inHashCode;
    }

    /**
     * Whether or not the parameter associated with this should be included in the hashCode() method.
     *
     * @return True if it should be included. False if it should be ignored.
     * @see #isEqualsIgnore()
     */
    public boolean isHashCodeInclude() {
        return inHashCode;
    }

    /**
     * Whether or not the parameter associated with this should be included in the toString() method.
     *
     * @return True if it should be ignored. False if it should be included.
     * @see #isToStringInclude()
     */
    public boolean isToStringIgnore() {
        return !inToString;
    }

    /**
     * Whether or not the parameter associated with this should be included in the toString() method.
     *
     * @return True if it should be included. False if it should be ignored.
     * @see #isToStringIgnore()
     */
    public boolean isToStringInclude() {
        return inToString;
    }

    /**
     * String representation of this enum.
     *
     * @return A string.
     */
    @Override
    public String toString() {
        return this.name() + "[" +
               "equals():" + flagToString(inEquals) + ", " +
               "hashCode():" + flagToString(inHashCode) + ", " +
               "toString():" + flagToString(inToString) +
               "]";
    }

    /**
     * Converts an ignore flag to either "Ignored" or "Included".
     *
     * @param flag  the ignore flag
     * @return "Ignored" if the flag is true, "Included" if the flag is false.
     */
    private static String flagToString(final boolean flag) {
        return flag ? "Included" : "Ignored";
    }
}
