package EasyOverrider;

import static models.easyOverrider.ParamMethodRestriction.IGNORED_FOR_ALL;
import static models.easyOverrider.ParamMethodRestriction.IGNORED_FOR_EQUALS__UNSAFE;
import static models.easyOverrider.ParamMethodRestriction.IGNORED_FOR_HASHCODE__UNSAFE;
import static models.easyOverrider.ParamMethodRestriction.IGNORED_FOR_TOSTRING;
import static models.easyOverrider.ParamMethodRestriction.INCLUDED_IN_ALL;
import static models.easyOverrider.ParamMethodRestriction.INCLUDED_IN_EQUALS_ONLY__UNSAFE;
import static models.easyOverrider.ParamMethodRestriction.INCLUDED_IN_HASHCODE_ONLY__UNSAFE;
import static models.easyOverrider.ParamMethodRestriction.INCLUDED_IN_TOSTRING_ONLY;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * This enum dictates which {@link ParamMethodRestriction} values are available for use.
 * It is here so that using the unsafe ones requires effort and thought before being used.<br?
 * The default value is {@link #SAFE_ONLY}, but if, for some reason, you decided that you want
 * the <code>__UNSAFE</code> values, you can use {@link #ALLOW_UNSAFE}.
 *
 */
public enum ParamMethodRestrictionRestriction {
    /**
     * This is the default. It only allows safe, normal {@link ParamMethodRestriction} values:
     * <li>{@link ParamMethodRestriction#IGNORED_FOR_ALL}</li>
     * <li>{@link ParamMethodRestriction#INCLUDED_IN_TOSTRING_ONLY}</li>
     * <li>{@link ParamMethodRestriction#IGNORED_FOR_TOSTRING}</li>
     * <li>{@link ParamMethodRestriction#INCLUDED_IN_ALL}</li>
     * If, for some weird reason, you think you need the other values too, use {@link #ALLOW_UNSAFE}.
     */
    SAFE_ONLY(EnumSet.of(IGNORED_FOR_ALL, INCLUDED_IN_TOSTRING_ONLY, IGNORED_FOR_TOSTRING, INCLUDED_IN_ALL)),

    /**
     * This allows all the {@link ParamMethodRestriction} values to be available, including the <code>__UNSAFE</code> ones.
     * <B>It is not recommended that you use this, ever.</B> If you do, there should be a very good and well-thought-out reason for it.<br>
     * What you probably want is {@link #SAFE_ONLY}.
     */
    ALLOW_UNSAFE(EnumSet.of(IGNORED_FOR_ALL, INCLUDED_IN_TOSTRING_ONLY, IGNORED_FOR_TOSTRING, INCLUDED_IN_ALL,
                            INCLUDED_IN_EQUALS_ONLY__UNSAFE, INCLUDED_IN_HASHCODE_ONLY__UNSAFE,
                            IGNORED_FOR_EQUALS__UNSAFE, IGNORED_FOR_HASHCODE__UNSAFE));

    private Set<ParamMethodRestriction> allowedParamMethodRestrictions;

    ParamMethodRestrictionRestriction(EnumSet<ParamMethodRestriction> allowedParamMethodRestrictions) {
        this.allowedParamMethodRestrictions = allowedParamMethodRestrictions;
    }

    public Set<ParamMethodRestriction> getAllowedParamMethodRestrictions() {
        return  Collections.unmodifiableSet(allowedParamMethodRestrictions);
    }

    public boolean allows(ParamMethodRestriction paramMethodRestriction) {
        return allowedParamMethodRestrictions.contains(paramMethodRestriction);
    }

    @Override
    public String toString() {
        return this.name();
    }
}
