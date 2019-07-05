package EasyOverrider;

import static EasyOverrider.ParamUsage.IGNORED_FOR_ALL;
import static EasyOverrider.ParamUsage.HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.EQUALS_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.EQUALS_AND_HASHCODE_ONLY;
import static EasyOverrider.ParamUsage.INCLUDED_IN_ALL;
import static EasyOverrider.ParamUsage.EQUALS_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.ParamUsage.TOSTRING_ONLY;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This enum dictates which {@link ParamUsage} values are available for use.<br>
 *
 * It is here so that using the unsafe ones requires effort and thought before being used.<br>
 * The default value is {@link #SAFE_ONLY}, but if, for some reason, you decided that you want
 * the <code>__UNSAFE</code> values, you can use {@link #ALLOW_UNSAFE}.
 */
public enum ParamMethodRestrictionRestriction {

    /**
     * This is the default. It only allows safe, {@link ParamUsage} values:
     * <ul>
     * <li>{@link ParamUsage#IGNORED_FOR_ALL}</li>
     * <li>{@link ParamUsage#TOSTRING_ONLY}</li>
     * <li>{@link ParamUsage#EQUALS_AND_HASHCODE_ONLY}</li>
     * <li>{@link ParamUsage#INCLUDED_IN_ALL}</li>
     * </ul>
     * If, for some weird reason, you think you need the other values too, use {@link #ALLOW_UNSAFE}.
     */
    SAFE_ONLY(EnumSet.of(IGNORED_FOR_ALL, TOSTRING_ONLY, EQUALS_AND_HASHCODE_ONLY, INCLUDED_IN_ALL)),

    /**
     * This allows all the {@link ParamUsage} values to be available including the <code>__UNSAFE</code> ones.<br>
     *
     * <B>It is recommended that you do not use this, ever.</B> If you do, there should be a very good and well-thought-out reason for it.
     * That reason should probably also be included in a comment so that others know why the decision was made.<br>
     *
     * What you probably want is {@link #SAFE_ONLY}.
     */
    ALLOW_UNSAFE(EnumSet.of(IGNORED_FOR_ALL, TOSTRING_ONLY, EQUALS_AND_HASHCODE_ONLY, INCLUDED_IN_ALL, EQUALS_ONLY__UNSAFE, HASHCODE_ONLY__UNSAFE,
                            HASHCODE_AND_TOSTRING_ONLY__UNSAFE, EQUALS_AND_TOSTRING_ONLY__UNSAFE));

    private Set<ParamUsage> allowedParamMethodRestrictions;

    ParamMethodRestrictionRestriction(final Set<ParamUsage> allowedParamMethodRestrictions) {
        this.allowedParamMethodRestrictions = allowedParamMethodRestrictions;
    }

    /**
     * Gets all of the ParamMethodRestriction values that are allowed for this ParamMethodRestrictionRestriction.
     *
     * @return A Set of {@link ParamUsage} values.
     */
    public Set<ParamUsage> getAllowedParamMethodRestrictions() {
        return Collections.unmodifiableSet(allowedParamMethodRestrictions);
    }

    /**
     * Tests if the provided ParamMethodRestriction is allowed for this ParamMethodRestrictionRestriction.
     *
     * @param paramMethodRestriction  the {@link ParamUsage} value to check on
     * @return True if the provided ParamMethodRestriction is allowed. False if not.
     */
    public boolean allows(final ParamUsage paramMethodRestriction) {
        return allowedParamMethodRestrictions.contains(paramMethodRestriction);
    }

    /**
     * toString method for a ParamMethodRestrictionRestriction.
     *
     * @return A String
     */
    @Override
    public String toString() {
        return this.name() +
               "[" + allowedParamMethodRestrictions.stream()
                                                   .map(ParamUsage::name)
                                                   .sorted()
                                                   .collect(Collectors.joining(",")) + "]";
    }
}
