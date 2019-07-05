package EasyOverrider;

import static EasyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
import static EasyOverrider.ParamMethodRestriction.HASHCODE_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.EQUALS_AND_TOSTRING_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.EQUALS_AND_HASHCODE_ONLY;
import static EasyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static EasyOverrider.ParamMethodRestriction.EQUALS_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.HASHCODE_ONLY__UNSAFE;
import static EasyOverrider.ParamMethodRestriction.TOSTRING_ONLY;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This enum dictates which {@link ParamMethodRestriction} values are available for use.<br>
 *
 * It is here so that using the unsafe ones requires effort and thought before being used.<br>
 * The default value is {@link #SAFE_ONLY}, but if, for some reason, you decided that you want
 * the <code>__UNSAFE</code> values, you can use {@link #ALLOW_UNSAFE}.
 */
public enum ParamMethodRestrictionRestriction {

    /**
     * This is the default. It only allows safe, {@link ParamMethodRestriction} values:
     * <ul>
     * <li>{@link ParamMethodRestriction#IGNORED_FOR_ALL}</li>
     * <li>{@link ParamMethodRestriction#TOSTRING_ONLY}</li>
     * <li>{@link ParamMethodRestriction#EQUALS_AND_HASHCODE_ONLY}</li>
     * <li>{@link ParamMethodRestriction#INCLUDED_IN_ALL}</li>
     * </ul>
     * If, for some weird reason, you think you need the other values too, use {@link #ALLOW_UNSAFE}.
     */
    SAFE_ONLY(EnumSet.of(IGNORED_FOR_ALL, TOSTRING_ONLY, EQUALS_AND_HASHCODE_ONLY, INCLUDED_IN_ALL)),

    /**
     * This allows all the {@link ParamMethodRestriction} values to be available including the <code>__UNSAFE</code> ones.<br>
     *
     * <B>It is recommended that you do not use this, ever.</B> If you do, there should be a very good and well-thought-out reason for it.
     * That reason should probably also be included in a comment so that others know why the decision was made.<br>
     *
     * What you probably want is {@link #SAFE_ONLY}.
     */
    ALLOW_UNSAFE(EnumSet.of(IGNORED_FOR_ALL, TOSTRING_ONLY, EQUALS_AND_HASHCODE_ONLY, INCLUDED_IN_ALL, EQUALS_ONLY__UNSAFE, HASHCODE_ONLY__UNSAFE,
                            HASHCODE_AND_TOSTRING_ONLY__UNSAFE, EQUALS_AND_TOSTRING_ONLY__UNSAFE));

    private Set<ParamMethodRestriction> allowedParamMethodRestrictions;

    ParamMethodRestrictionRestriction(final Set<ParamMethodRestriction> allowedParamMethodRestrictions) {
        this.allowedParamMethodRestrictions = allowedParamMethodRestrictions;
    }

    /**
     * Gets all of the ParamMethodRestriction values that are allowed for this ParamMethodRestrictionRestriction.
     *
     * @return A Set of {@link ParamMethodRestriction} values.
     */
    public Set<ParamMethodRestriction> getAllowedParamMethodRestrictions() {
        return Collections.unmodifiableSet(allowedParamMethodRestrictions);
    }

    /**
     * Tests if the provided ParamMethodRestriction is allowed for this ParamMethodRestrictionRestriction.
     *
     * @param paramMethodRestriction  the {@link ParamMethodRestriction} value to check on
     * @return True if the provided ParamMethodRestriction is allowed. False if not.
     */
    public boolean allows(final ParamMethodRestriction paramMethodRestriction) {
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
                                                   .map(ParamMethodRestriction::name)
                                                   .sorted()
                                                   .collect(Collectors.joining(",")) + "]";
    }
}
