package EasyOverrider;

import static EasyOverrider.ParamUsage.TOSTRING_ONLY;
import static EasyOverrider.EasyOverriderUtils.requireNonNull;

import java.util.IllegalFormatException;
import java.util.function.Function;

/**
 * This houses any configuration pieces that should be available to the ParamListService.<br>
 */
public class ParamListServiceConfig {

    private String stringForNull = "null";
    private String stringForRecursionPrevented = "...";
    private String stringForEmptyParamList = " ";
    private String parameterDelimiter = ", ";
    private String nameValueFormat = "%1$s=%2$s";
    private String parameterValueFormat = "'%1$s'";
    private String toStringFormat = "%1$s@%2$s [%3$s]";
    private Function<Class, String> classNameGetter = Class::getSimpleName;
    private Function<Integer, String> hashCodeToString = Integer::toHexString;

    private static ParamList<ParamListServiceConfig> paramList;

    private static ParamList<ParamListServiceConfig> getParamList() {
        if (paramList == null) {
            paramList = ParamList.forClass(ParamListServiceConfig.class)
                                 .withParam("stringForNull", ParamListServiceConfig::getStringForNull, String.class)
                                 .withParam("stringForRecursionPrevented",
                                            ParamListServiceConfig::getStringForRecursionPrevented,
                                            String.class)
                                 .withParam("stringForEmptyParamList",
                                            ParamListServiceConfig::getStringForEmptyParamList,
                                            String.class)
                                 .withParam("parameterDelimiter", ParamListServiceConfig::getParameterDelimiter, String.class)
                                 .withParam("nameValueFormat", ParamListServiceConfig::getNameValueFormat, String.class)
                                 .withParam("parameterValueFormat", ParamListServiceConfig::getParameterValueFormat, String.class)
                                 .withParam("toStringFormat", ParamListServiceConfig::getToStringFormat, String.class)
                                 .withParam("classNameGetter",
                                            ParamListServiceConfig::getClassNameGetter, TOSTRING_ONLY,
                                            Function.class)
                                 .withParam("hashCodeToString",
                                            ParamListServiceConfig::getHashCodeToString, TOSTRING_ONLY,
                                            Function.class)
                                 .andThatsIt();
        }
        return paramList;
    }

    /**
     * Default empty constructor using default values.<br>
     */
    public ParamListServiceConfig() { }

    /**
     * Full constructor including all values.<br>
     *
     * Any parameter passed in as null will maintain the default value.
     *
     * @param stringForNull  the String to use for a null value - default is <code>"null"</code>
     * @param stringForRecursionPrevented  the String to use to indicate recursion was prevented - default is <code>"..."</code>
     * @param stringForEmptyParamList  the String to use when there are no parameters - default is <code>" "</code>
     * @param parameterDelimiter  the String to use as a delimiter between parameters - default is <code>", "</code>
     * @param nameValueFormat  the format to use to create name/value pair Strings - default is <code>"%1$s=%2$s"</code>
     * @param parameterValueFormat  the format to use for each parameter - default is <code>"'%1$s'"</code>
     * @param toStringFormat  the format to use for the final toString result - default is <code>"%1$s@%2$s [%3$s]"</code>
     * @param classNameGetter  the Function that converts a class into a String - default is <code>Class::getSimpleName</code>
     * @param hashCodeToString  the function that converts a hashCode int to a String - default is <code>Integer::toHexString</code>
     * @throws IllegalArgumentException if a provided format String is invalid.
     * @see #setStringForNull(String)
     * @see #setStringForRecursionPrevented(String)
     * @see #setStringForEmptyParamList(String)
     * @see #setParameterDelimiter(String)
     * @see #setNameValueFormat(String)
     * @see #setParameterValueFormat(String)
     * @see #setToStringFormat(String)
     * @see #setClassNameGetter(Function)
     * @see #setHashCodeToString(Function)
     */
    public ParamListServiceConfig(String stringForNull, String stringForRecursionPrevented, String stringForEmptyParamList,
                                  String parameterDelimiter, String nameValueFormat, String parameterValueFormat, String toStringFormat,
                                  Function<Class, String> classNameGetter, Function<Integer, String> hashCodeToString) {
        EasyOverriderUtils.runSetterIfNotNull(this, ParamListServiceConfig::setStringForNull, stringForNull);
        EasyOverriderUtils.runSetterIfNotNull(this, ParamListServiceConfig::setStringForRecursionPrevented, stringForRecursionPrevented);
        EasyOverriderUtils.runSetterIfNotNull(this, ParamListServiceConfig::setStringForEmptyParamList, stringForEmptyParamList);
        EasyOverriderUtils.runSetterIfNotNull(this, ParamListServiceConfig::setParameterDelimiter, parameterDelimiter);
        EasyOverriderUtils.runSetterIfNotNull(this, ParamListServiceConfig::setNameValueFormat, nameValueFormat);
        EasyOverriderUtils.runSetterIfNotNull(this, ParamListServiceConfig::setParameterValueFormat, parameterValueFormat);
        EasyOverriderUtils.runSetterIfNotNull(this, ParamListServiceConfig::setToStringFormat, toStringFormat);
        EasyOverriderUtils.runSetterIfNotNull(this, ParamListServiceConfig::setClassNameGetter, classNameGetter);
        EasyOverriderUtils.runSetterIfNotNull(this, ParamListServiceConfig::setHashCodeToString, hashCodeToString);
    }

    /**
     * Getter for the String that is used in a toString when a value is null.<br>
     *
     * Default value is <code>"null"</code>.<br>
     *
     * @return A String
     */
    public String getStringForNull() {
        return stringForNull;
    }

    /**
     * Setter for the String that is used when a value is null.<br>
     *
     * Default value is <code>"null"</code>.<br>
     *
     * @param stringForNull  the string to use in a toString when a value is null - cannot be null
     * @return the current ParamListServiceConfig
     * @throws IllegalArgumentException if the provided String is null
     */
    public ParamListServiceConfig setStringForNull(final String stringForNull) {
        requireNonNull(stringForNull, 1, "stringForNull", "setStringForNull");
        this.stringForNull = stringForNull;
        return this;
    }

    /**
     * Getter for the String that is used in a toString when a previously seen object is seen again.<br>
     *
     * Default value is <code>"..."</code>.<br>
     *
     * @return A String
     */
    public String getStringForRecursionPrevented() {
        return stringForRecursionPrevented;
    }

    /**
     * Setter for the String that is used when a previously seen object is seen again.<br>
     *
     * Default value is <code>"..."</code>.<br>
     *
     * @param stringForRecursionPrevented  the string to use in a toString when a previously seen object is seen again - cannot be null
     * @return the current ParamListServiceConfig
     * @throws IllegalArgumentException if the provided String is null
     */
    public ParamListServiceConfig setStringForRecursionPrevented(final String stringForRecursionPrevented) {
        requireNonNull(stringForRecursionPrevented, 1, "stringForRecursionPrevented", "setStringForRecursionPrevented");
        this.stringForRecursionPrevented = stringForRecursionPrevented;
        return this;
    }

    /**
     * Getter for the String that is used in a toString when an empty ParamList is encountered.<br>
     *
     * Default value is <code>" "</code>.<br>
     *
     * @return A String
     */
    public String getStringForEmptyParamList() {
        return stringForEmptyParamList;
    }

    /**
     * Setter for the String that is used when an empty ParamList is encountered.<br>
     *
     * Default value is <code>" "</code>.<br>
     *
     * @param stringForEmptyParamList  the string to use in a toString when an empty ParamList is encountered - cannot be null
     * @return the current ParamListServiceConfig
     * @throws IllegalArgumentException if the provided String is null
     */
    public ParamListServiceConfig setStringForEmptyParamList(final String stringForEmptyParamList) {
        requireNonNull(stringForEmptyParamList, 1, "stringForEmptyParamList", "setStringForEmptyParamList");
        this.stringForEmptyParamList = stringForEmptyParamList;
        return this;
    }

    /**
     * Getter for the String that is used in a toString between parameters.<br>
     *
     * Default value is <code>", "</code>.<br>
     *
     * @return A String
     */
    public String getParameterDelimiter() {
        return parameterDelimiter;
    }

    /**
     * Setter for the String that is used between parameters.<br>
     *
     * Default value is <code>", "</code>.<br>
     *
     * @param parameterDelimiter  the string to use in a toString between parameters - cannot be null
     * @return the current ParamListServiceConfig
     * @throws IllegalArgumentException if the provided String is null
     */
    public ParamListServiceConfig setParameterDelimiter(final String parameterDelimiter) {
        requireNonNull(parameterDelimiter, 1, "parameterDelimiter", "setParameterDelimiter");
        this.parameterDelimiter = parameterDelimiter;
        return this;
    }

    /**
     * Getter for the format String that is used in a toString to create a name/value string.<br>
     *
     * Default value is <code>"%1$s=%2$s"</code>.<br>
     *
     * @return A String
     */
    public String getNameValueFormat() {
        return nameValueFormat;
    }

    /**
     * Setter for the format String that is used in a toString to create a name/value strings.<br>
     *
     * When using this format String, two values will be provided in this order: name, value.<br>
     *
     * Default value is <code>"%1$s=%2$s"</code>.<br>
     *
     * @param nameValueFormat  the format string to use in a toString to create a name/value string - cannot be null
     * @return the current ParamListServiceConfig
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     */
    public ParamListServiceConfig setNameValueFormat(final String nameValueFormat) {
        requireNonNull(nameValueFormat, 1, "nameValueFormat", "setNameValueFormat");
        try {
            String.format(nameValueFormat, "name", "value");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setNameValueFormat is not a valid format string.", e);
        }
        this.nameValueFormat = nameValueFormat;
        return this;
    }

    /**
     * Getter for the format String that is used in a toString on each parameter value.<br>
     *
     * Default value is <code>"'%1$s'"</code>.<br>
     *
     * @return A String
     */
    public String getParameterValueFormat() {
        return parameterValueFormat;
    }

    /**
     * Setter for the format String that is used in a toString on each parameter value.<br>
     *
     * When using this format String, one value will be provided.<br>
     *
     * Default value is <code>"'%1$s'"</code>.<br>
     *
     * @param parameterValueFormat  the format string to use in a toString on each parameter value - cannot be null
     * @return the current ParamListServiceConfig
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     */
    public ParamListServiceConfig setParameterValueFormat(final String parameterValueFormat) {
        requireNonNull(parameterValueFormat, 1, "parameterValueFormat", "setParameterValueFormat");
        try {
            String.format(parameterValueFormat, "parameter");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setParameterValueFormat is not a valid format string.", e);
        }
        this.parameterValueFormat = parameterValueFormat;
        return this;
    }

    /**
     * Getter for the format String that is used in a toString to create the final toString value.<br>
     *
     * Default value is <code>"%1$s@%2$s [%3$s]"</code>.<br>
     *
     * @return A String
     */
    public String getToStringFormat() {
        return toStringFormat;
    }

    /**
     * Setter for the format String that is used in a toString to create the final toString value.<br>
     *
     * When using this format String, three values will be provided in this order: class, hexed hash code, parameter name/value pairs.<br>
     *
     * Default value is <code>"%1$s@%2$s [%3$s]"</code>.<br>
     *
     * @param toStringFormat  the format string to use in a toString to create the final toString value - cannot be null
     * @return the current ParamListServiceConfig
     * @throws IllegalArgumentException if the provided String is null
     * @throws IllegalArgumentException if the provided format is invalid
     */
    public ParamListServiceConfig setToStringFormat(final String toStringFormat) {
        requireNonNull(toStringFormat, 1, "toStringFormat", "setToStringFormat");
        try {
            String.format(toStringFormat, "class", "hashcode", "paramslist");
        } catch (IllegalFormatException e) {
            throw new IllegalArgumentException("The string provided to setToStringFormat is not a valid format string.", e);
        }
        this.toStringFormat = toStringFormat;
        return this;
    }

    /**
     * Getter for the function that is used to get the class name from a Class object.<br>
     *
     * Default value is <code>Class::getSimpleName</code>.<br>
     *
     * @return A String
     */
    public Function<Class, String> getClassNameGetter() {
        return classNameGetter;
    }

    /**
     * Setter for the function that is used to get the class name from a Class object.<br>
     *
     * Default value is <code>Class::getSimpleName</code>.<br>
     *
     * @param classNameGetter  the function to use to get the class name from a Class object - cannot be null
     * @return the current ParamListServiceConfig
     * @throws IllegalArgumentException if the provided Function is null
     */
    public ParamListServiceConfig setClassNameGetter(final Function<Class, String> classNameGetter) {
        requireNonNull(classNameGetter, 1, "classNameGetter", "setClassNameGetter");
        this.classNameGetter = classNameGetter;
        return this;
    }

    /**
     * Getter for the function that is used to convert the hashCode to a String for the toString method.<br>
     *
     * Default value is <code>Integer::toHexString</code>.<br>
     *
     * @return A String
     */
    public Function<Integer, String> getHashCodeToString() {
        return hashCodeToString;
    }

    /**
     * Setter for the function that is used to convert the hashCode to a String for the toString method.<br>
     *
     * Default value is <code>Integer::toHexString</code>.<br>
     *
     * @param hashCodeToString  the function to use to convert the hashCode to a String - cannot be null
     * @return the current ParamListServiceConfig
     * @throws IllegalArgumentException if the provided Function is null
     */
    public ParamListServiceConfig setHashCodeToString(final Function<Integer, String> hashCodeToString) {
        requireNonNull(hashCodeToString, 1, "hashCodeToString", "setHashCodeToString");
        this.hashCodeToString = hashCodeToString;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return getParamList().equals(this, obj);
    }

    @Override
    public int hashCode() {
        return getParamList().hashCode(this);
    }

    @Override
    public String toString() {
        return getParamList().toString(this);
    }
}
