package com.adelerobots.fioneg.util;

import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.validator.GenericValidator;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;


/**
 * Funciones de utilidad.
 */
public class FunctionUtils {

    /**
     * The empty String <code>""</code>.
     * @since 2.0
     */
    public static final String EMPTY_STRING = "";





    /**
     * <p><code>FunctionUtils</code> instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * <code>FunctionUtils.trim(" foo ");</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public FunctionUtils() {
        super();
    }





    // Defaulting
    //-----------------------------------------------------------------------
    /**
     * <p>Returns a default value if the object passed is
     * <code>null</code>.</p>
     * 
     * <pre>
     * FunctionUtils.defaultIfNull(null, null)      = null
     * FunctionUtils.defaultIfNull(null, "")        = ""
     * FunctionUtils.defaultIfNull(null, "zz")      = "zz"
     * FunctionUtils.defaultIfNull("abc", *)        = "abc"
     * FunctionUtils.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
     * </pre>
     *
     * @param object  the <code>Object</code> to test, may be <code>null</code>
     * @param defaultValue  the default value to return, may be <code>null</code>
     * @return <code>object</code> if it is not <code>null</code>, defaultValue otherwise
     */
    public static Object defaultIfNull(Object object, Object defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * <p>Compares two objects for equality, where either one or both
     * objects may be <code>null</code>.</p>
     *
     * <pre>
     * FunctionUtils.equals(null, null)                  = true
     * FunctionUtils.equals(null, "")                    = false
     * FunctionUtils.equals("", null)                    = false
     * FunctionUtils.equals("", "")                      = true
     * FunctionUtils.equals(Boolean.TRUE, null)          = false
     * FunctionUtils.equals(Boolean.TRUE, "true")        = false
     * FunctionUtils.equals(Boolean.TRUE, Boolean.TRUE)  = true
     * FunctionUtils.equals(Boolean.TRUE, Boolean.FALSE) = false
     * </pre>
     *
     * @param object1  the first object, may be <code>null</code>
     * @param object2  the second object, may be <code>null</code>
     * @return <code>true</code> if the values of both objects are the same
     */
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

    /**
     * <p>Gets the hash code of an object returning zero when the
     * object is <code>null</code>.</p>
     *
     * <pre>
     * FunctionUtils.hashCode(null)   = 0
     * FunctionUtils.hashCode(obj)    = obj.hashCode()
     * </pre>
     *
     * @param obj  the object to obtain the hash code of, may be <code>null</code>
     * @return the hash code of the object, or zero if null
     * @since 2.1
     */
    public static int hashCode(Object obj) {
        return (obj == null) ? 0 : obj.hashCode();
    }





    // Identity ToString
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the toString that would be produced by <code>Object</code>
     * if a class did not override toString itself. <code>null</code>
     * will return <code>null</code>.</p>
     *
     * <pre>
     * FunctionUtils.identityToString(null)         = null
     * FunctionUtils.identityToString("")           = "java.lang.String@1e23"
     * FunctionUtils.identityToString(Boolean.TRUE) = "java.lang.Boolean@7fa"
     * </pre>
     *
     * @param object  the object to create a toString for, may be
     *  <code>null</code>
     * @return the default toString text, or <code>null</code> if
     *  <code>null</code> passed in
     */
    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        identityToString(buffer, object);
        return buffer.toString();
    }

    /**
     * <p>Appends the toString that would be produced by <code>Object</code>
     * if a class did not override toString itself. <code>null</code>
     * will throw a NullPointerException for either of the two parameters. </p>
     *
     * <pre>
     * FunctionUtils.identityToString(buf, "")            = buf.append("java.lang.String@1e23"
     * FunctionUtils.identityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa"
     * FunctionUtils.identityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa")
     * </pre>
     *
     * @param buffer  the buffer to append to
     * @param object  the object to create a toString for
     * @since 2.4
     */
    public static void identityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        buffer.append(object.getClass().getName())
              .append('@')
              .append(Integer.toHexString(System.identityHashCode(object)));
    }





    // ToString
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the <code>toString</code> of an <code>Object</code> returning
     * an empty string ("") if <code>null</code> input.</p>
     * 
     * <pre>
     * FunctionUtils.toString(null)         = ""
     * FunctionUtils.toString("")           = ""
     * FunctionUtils.toString("bat")        = "bat"
     * FunctionUtils.toString(Boolean.TRUE) = "true"
     * </pre>
     * 
     * @see #defaultString(String)
     * @see String#valueOf(Object)
     * @param obj  the Object to <code>toString</code>, may be null
     * @return the passed in Object's toString, or nullStr if <code>null</code> input
     * @since 2.0
     */
    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * <p>Gets the <code>toString</code> of an <code>Object</code> returning
     * a specified text if <code>null</code> input.</p>
     * 
     * <pre>
     * FunctionUtils.toString(null, null)           = null
     * FunctionUtils.toString(null, "null")         = "null"
     * FunctionUtils.toString("", "null")           = ""
     * FunctionUtils.toString("bat", "null")        = "bat"
     * FunctionUtils.toString(Boolean.TRUE, "null") = "true"
     * </pre>
     * 
     * @see #defaultString(String,String)
     * @see String#valueOf(Object)
     * @param obj  the Object to <code>toString</code>, may be null
     * @param nullStr  the String to return if <code>null</code> input, may be null
     * @return the passed in Object's toString, or nullStr if <code>null</code> input
     * @since 2.0
     */
    public static String toString(Object obj, String nullStr) {
        return obj == null ? nullStr : obj.toString();
    }

    /**
     * Determine the RFC 3066 compliant language tag,
     * as used for the HTTP "Accept-Language" header.
     * @param locale the Locale to transform to a language tag
     * @return the RFC 3066 compliant language tag as String
     */
    public static String toLanguageTag(Locale locale) {
        if (locale == null) return null;
        return locale.getLanguage() + (isNotBlank(locale.getCountry()) ? "-" + locale.getCountry() : "");
    }

    /**
     * <p>Converts a String to a Locale.</p>
     *
     * <p>This method takes the string format of a locale and creates the
     * locale object from it.</p>
     *
     * <pre>
     *   LocaleUtils.toLocale("en")         = new Locale("en", "")
     *   LocaleUtils.toLocale("en_GB")      = new Locale("en", "GB")
     *   LocaleUtils.toLocale("en_GB_xxx")  = new Locale("en", "GB", "xxx")   (#)
     * </pre>
     *
     * <p>(#) The behaviour of the JDK variant constructor changed between JDK1.3 and JDK1.4.
     * In JDK1.3, the constructor upper cases the variant, in JDK1.4, it doesn't.
     * Thus, the result from getVariant() may vary depending on your JDK.</p>
     *
     * <p>This method validates the input strictly.
     * The language code must be lowercase.
     * The country code must be uppercase.
     * The separator must be an underscore.
     * The length must be correct.
     * </p>
     *
     * @param str  the locale String to convert, null returns null
     * @return a Locale, null if null input
     * @throws IllegalArgumentException if the string is an invalid format
     */
    public static Locale toLocale(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len != 2 && len != 5 && len < 7) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 2) {
            return new Locale(str, "");
        } else {
            if (str.charAt(2) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            char ch3 = str.charAt(3);
            if (ch3 == '_') {
                return new Locale(str.substring(0, 2), "", str.substring(4));
            }
            char ch4 = str.charAt(4);
            if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 5) {
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            } else {
                if (str.charAt(5) != '_') {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
            }
        }
    }





    // Min/Max
    //-----------------------------------------------------------------------
    /**
     * Null safe comparison of Comparables.
     * 
     * @param c1  the first comparable, may be null
     * @param c2  the second comparable, may be null
     * @return
     *  <ul>
     *   <li>If both objects are non-null and unequal, the lesser object.
     *   <li>If both objects are non-null and equal, c1.
     *   <li>If one of the comparables is null, the non-null object.
     *   <li>If both the comparables are null, null is returned.
     *  </ul>
     */
    public static Object min(Comparable c1, Comparable c2) {
        if (c1 != null && c2 != null) {
            return c1.compareTo(c2) < 1 ? c1 : c2;
        } else {
            return c1 != null ? c1 : c2;
        }                              
    }

    /**
     * Null safe comparison of Comparables.
     * 
     * @param c1  the first comparable, may be null
     * @param c2  the second comparable, may be null
     * @return
     *  <ul>
     *   <li>If both objects are non-null and unequal, the greater object.
     *   <li>If both objects are non-null and equal, c1.
     *   <li>If one of the comparables is null, the non-null object.
     *   <li>If both the comparables are null, null is returned.
     *  </ul>
     */
    public static Object max(Comparable c1, Comparable c2) {
        if (c1 != null && c2 != null) {
            return c1.compareTo(c2) >= 0 ? c1 : c2;
        } else {
            return c1 != null ? c1 : c2;
        }
    }





    // Empty checks
    //-----------------------------------------------------------------------


    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * FunctionUtils.isEmpty(null)      = true
     * FunctionUtils.isEmpty("")        = true
     * FunctionUtils.isEmpty(" ")       = false
     * FunctionUtils.isEmpty("bob")     = false
     * FunctionUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>Checks if a String is not empty ("") and not null.</p>
     *
     * <pre>
     * FunctionUtils.isNotEmpty(null)      = false
     * FunctionUtils.isNotEmpty("")        = false
     * FunctionUtils.isNotEmpty(" ")       = true
     * FunctionUtils.isNotEmpty("bob")     = true
     * FunctionUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * FunctionUtils.isBlank(null)      = true
     * FunctionUtils.isBlank("")        = true
     * FunctionUtils.isBlank(" ")       = true
     * FunctionUtils.isBlank("bob")     = false
     * FunctionUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(CharSequence str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
     *
     * <pre>
     * FunctionUtils.isNotBlank(null)      = false
     * FunctionUtils.isNotBlank("")        = false
     * FunctionUtils.isNotBlank(" ")       = false
     * FunctionUtils.isNotBlank("bob")     = true
     * FunctionUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is
     *  not empty and not null and not whitespace
     * @since 2.0
     */
    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }





    // Trim
    //-----------------------------------------------------------------------


    /**
     * <p>Removes control characters (char &lt;= 32) from both
     * ends of this String, handling <code>null</code> by returning
     * <code>null</code>.</p>
     *
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     * To strip whitespace use {@link #strip(String)}.</p>
     *
     * <p>To trim your choice of characters, use the
     * {@link #strip(String, String)} methods.</p>
     *
     * <pre>
     * FunctionUtils.trim(null)          = null
     * FunctionUtils.trim("")            = ""
     * FunctionUtils.trim("     ")       = ""
     * FunctionUtils.trim("abc")         = "abc"
     * FunctionUtils.trim("    abc    ") = "abc"
     * </pre>
     *
     * @param str  the String to be trimmed, may be null
     * @return the trimmed string, <code>null</code> if null String input
     */
    public static String trim(CharSequence str) {
        return str == null ? null : str.toString().trim();
    }

    /**
     * <p>Removes control characters (char &lt;= 32) from both
     * ends of this String returning <code>null</code> if the String is
     * empty ("") after the trim or if it is <code>null</code>.
     *
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     * To strip whitespace use {@link #stripToNull(String)}.</p>
     *
     * <pre>
     * FunctionUtils.trimToNull(null)          = null
     * FunctionUtils.trimToNull("")            = null
     * FunctionUtils.trimToNull("     ")       = null
     * FunctionUtils.trimToNull("abc")         = "abc"
     * FunctionUtils.trimToNull("    abc    ") = "abc"
     * </pre>
     *
     * @param str  the String to be trimmed, may be null
     * @return the trimmed String,
     *  <code>null</code> if only chars &lt;= 32, empty or null String input
     * @since 2.0
     */
    public static String trimToNull(CharSequence str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    /**
     * <p>Removes control characters (char &lt;= 32) from both
     * ends of this String returning an empty String ("") if the String
     * is empty ("") after the trim or if it is <code>null</code>.
     *
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     * To strip whitespace use {@link #stripToEmpty(String)}.</p>
     *
     * <pre>
     * FunctionUtils.trimToEmpty(null)          = ""
     * FunctionUtils.trimToEmpty("")            = ""
     * FunctionUtils.trimToEmpty("     ")       = ""
     * FunctionUtils.trimToEmpty("abc")         = "abc"
     * FunctionUtils.trimToEmpty("    abc    ") = "abc"
     * </pre>
     *
     * @param str  the String to be trimmed, may be null
     * @return the trimmed String, or an empty String if <code>null</code> input
     * @since 2.0
     */
    public static String trimToEmpty(CharSequence str) {
        return str == null ? EMPTY_STRING : str.toString().trim();
    }





    // Stripping
    //-----------------------------------------------------------------------


    /**
     * <p>Strips the begining and trailing quote characters
     * 
     * <p>So a single/double quote becomes the characters empty character
     *
     * <p>Example:
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn't say, "Stop!
     * </pre>
     * </p>
     *
     * @param str  String to escape values in, may be null
     * @return String with stripped values, <code>null</code> if null string input
     */
    public static String stripQuotes(String str) {
        if (str == null) return str;
        //Remove begining and trailing quoted to welldone javascript/html/etc scape
        str = Pattern.compile("^[\"\']").matcher(str).replaceAll("");
        str = Pattern.compile("[\"\']$").matcher(str).replaceAll("");
        return str;
    }





    // Equals
    //-----------------------------------------------------------------------


    /**
     * <p>Compares two Strings, returning <code>true</code> if they are equal.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.</p>
     *
     * <pre>
     * FunctionUtils.equals(null, null)   = true
     * FunctionUtils.equals(null, "abc")  = false
     * FunctionUtils.equals("abc", null)  = false
     * FunctionUtils.equals("abc", "abc") = true
     * FunctionUtils.equals("abc", "ABC") = false
     * </pre>
     *
     * @see java.lang.String#equals(Object)
     * @param str1  the first String, may be null
     * @param str2  the second String, may be null
     * @return <code>true</code> if the Strings are equal, case sensitive, or
     *  both <code>null</code>
     */
    public static boolean equals(CharSequence str1, CharSequence str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    /**
     * <p>Compares two Strings, returning <code>true</code> if they are equal ignoring
     * the case.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered equal. Comparison is case insensitive.</p>
     *
     * <pre>
     * FunctionUtils.equalsIgnoreCase(null, null)   = true
     * FunctionUtils.equalsIgnoreCase(null, "abc")  = false
     * FunctionUtils.equalsIgnoreCase("abc", null)  = false
     * FunctionUtils.equalsIgnoreCase("abc", "abc") = true
     * FunctionUtils.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @see java.lang.String#equalsIgnoreCase(String)
     * @param str1  the first String, may be null
     * @param str2  the second String, may be null
     * @return <code>true</code> if the Strings are equal, case insensitive, or
     *  both <code>null</code>
     */
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return str1 == null ? str2 == null : str1.toString().equalsIgnoreCase(str2.toString());
    }





    // Character Tests
    //-----------------------------------------------------------------------


    /**
     * <p>Checks if the String contains only unicode letters.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * FunctionUtils.isAlpha(null)   = false
     * FunctionUtils.isAlpha("")     = true
     * FunctionUtils.isAlpha("  ")   = false
     * FunctionUtils.isAlpha("abc")  = true
     * FunctionUtils.isAlpha("ab2c") = false
     * FunctionUtils.isAlpha("ab-c") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains letters, and is non-null
     */
    public static boolean isAlpha(CharSequence str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLetter(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode letters and
     * space (' ').</p>
     *
     * <p><code>null</code> will return <code>false</code>
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * FunctionUtils.isAlphaSpace(null)   = false
     * FunctionUtils.isAlphaSpace("")     = true
     * FunctionUtils.isAlphaSpace("  ")   = true
     * FunctionUtils.isAlphaSpace("abc")  = true
     * FunctionUtils.isAlphaSpace("ab c") = true
     * FunctionUtils.isAlphaSpace("ab2c") = false
     * FunctionUtils.isAlphaSpace("ab-c") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains letters and space,
     *  and is non-null
     */
    public static boolean isAlphaSpace(CharSequence str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isLetter(str.charAt(i)) == false) && (str.charAt(i) != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode letters or digits.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * FunctionUtils.isAlphanumeric(null)   = false
     * FunctionUtils.isAlphanumeric("")     = true
     * FunctionUtils.isAlphanumeric("  ")   = false
     * FunctionUtils.isAlphanumeric("abc")  = true
     * FunctionUtils.isAlphanumeric("ab c") = false
     * FunctionUtils.isAlphanumeric("ab2c") = true
     * FunctionUtils.isAlphanumeric("ab-c") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains letters or digits,
     *  and is non-null
     */
    public static boolean isAlphanumeric(CharSequence str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLetterOrDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode letters, digits
     * or space (<code>' '</code>).</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * FunctionUtils.isAlphanumeric(null)   = false
     * FunctionUtils.isAlphanumeric("")     = true
     * FunctionUtils.isAlphanumeric("  ")   = true
     * FunctionUtils.isAlphanumeric("abc")  = true
     * FunctionUtils.isAlphanumeric("ab c") = true
     * FunctionUtils.isAlphanumeric("ab2c") = true
     * FunctionUtils.isAlphanumeric("ab-c") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains letters, digits or space,
     *  and is non-null
     */
    public static boolean isAlphanumericSpace(CharSequence str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isLetterOrDigit(str.charAt(i)) == false) && (str.charAt(i) != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the string contains only ASCII printable characters.</p>
     * 
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     * 
     * <pre>
     * FunctionUtils.isAsciiPrintable(null)     = false
     * FunctionUtils.isAsciiPrintable("")       = true
     * FunctionUtils.isAsciiPrintable(" ")      = true
     * FunctionUtils.isAsciiPrintable("Ceki")   = true
     * FunctionUtils.isAsciiPrintable("ab2c")   = true
     * FunctionUtils.isAsciiPrintable("!ab-c~") = true
     * FunctionUtils.isAsciiPrintable("\u0020") = true
     * FunctionUtils.isAsciiPrintable("\u0021") = true
     * FunctionUtils.isAsciiPrintable("\u007e") = true
     * FunctionUtils.isAsciiPrintable("\u007f") = false
     * FunctionUtils.isAsciiPrintable("Ceki G\u00fclc\u00fc") = false
     * </pre>
     *
     * @param str the string to check, may be null
     * @return <code>true</code> if every character is in the range
     *  32 thru 126
     * @since 2.1
     */
    public static boolean isAsciiPrintable(CharSequence str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (isAsciiPrintable(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode digits.
     * A decimal point is not a unicode digit and returns false.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * FunctionUtils.isNumeric(null)   = false
     * FunctionUtils.isNumeric("")     = true
     * FunctionUtils.isNumeric("  ")   = false
     * FunctionUtils.isNumeric("123")  = true
     * FunctionUtils.isNumeric("12 3") = false
     * FunctionUtils.isNumeric("ab2c") = false
     * FunctionUtils.isNumeric("12-3") = false
     * FunctionUtils.isNumeric("12.3") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumeric(CharSequence str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode digits or space
     * (<code>' '</code>).
     * A decimal point is not a unicode digit and returns false.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * FunctionUtils.isNumeric(null)   = false
     * FunctionUtils.isNumeric("")     = true
     * FunctionUtils.isNumeric("  ")   = true
     * FunctionUtils.isNumeric("123")  = true
     * FunctionUtils.isNumeric("12 3") = true
     * FunctionUtils.isNumeric("ab2c") = false
     * FunctionUtils.isNumeric("12-3") = false
     * FunctionUtils.isNumeric("12.3") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains digits or space,
     *  and is non-null
     */
    public static boolean isNumericSpace(CharSequence str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isDigit(str.charAt(i)) == false) && (str.charAt(i) != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only whitespace.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * FunctionUtils.isWhitespace(null)   = false
     * FunctionUtils.isWhitespace("")     = true
     * FunctionUtils.isWhitespace("  ")   = true
     * FunctionUtils.isWhitespace("abc")  = false
     * FunctionUtils.isWhitespace("ab2c") = false
     * FunctionUtils.isWhitespace("ab-c") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains whitespace, and is non-null
     * @since 2.0
     */
    public static boolean isWhitespace(CharSequence str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only lowercase characters.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>false</code>.</p>
     *
     * <pre>
     * FunctionUtils.isAllLowerCase(null)   = false
     * FunctionUtils.isAllLowerCase("")     = false
     * FunctionUtils.isAllLowerCase("  ")   = false
     * FunctionUtils.isAllLowerCase("abc")  = true
     * FunctionUtils.isAllLowerCase("abC") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains lowercase characters, and is non-null
     * @since 2.5
     */
    public static boolean isAllLowerCase(CharSequence str) {
        if (str == null || isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLowerCase(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only uppercase characters.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>false</code>.</p>
     *
     * <pre>
     * FunctionUtils.isAllUpperCase(null)   = false
     * FunctionUtils.isAllUpperCase("")     = false
     * FunctionUtils.isAllUpperCase("  ")   = false
     * FunctionUtils.isAllUpperCase("ABC")  = true
     * FunctionUtils.isAllUpperCase("aBC") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains uppercase characters, and is non-null
     * @since 2.5
     */
    public static boolean isAllUpperCase(CharSequence str) {
        if (str == null || isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isUpperCase(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }





    // Defaults
    //-----------------------------------------------------------------------


    /**
     * <p>Returns either the passed in String,
     * or if the String is <code>null</code>, an empty String ("").</p>
     *
     * <pre>
     * FunctionUtils.defaultString(null)  = ""
     * FunctionUtils.defaultString("")    = ""
     * FunctionUtils.defaultString("bat") = "bat"
     * </pre>
     *
     * @see String#valueOf(Object)
     * @param str  the String to check, may be null
     * @return the passed in String, or the empty String if it
     *  was <code>null</code>
     */
    public static String defaultString(String str) {
        return str == null ? EMPTY_STRING : str;
    }

    /**
     * <p>Returns either the passed in String, or if the String is
     * <code>null</code>, the value of <code>defaultStr</code>.</p>
     *
     * <pre>
     * FunctionUtils.defaultString(null, "NULL")  = "NULL"
     * FunctionUtils.defaultString("", "NULL")    = ""
     * FunctionUtils.defaultString("bat", "NULL") = "bat"
     * </pre>
     *
     * @see String#valueOf(Object)
     * @param str  the String to check, may be null
     * @param defaultStr  the default String to return
     *  if the input is <code>null</code>, may be null
     * @return the passed in String, or the default if it was <code>null</code>
     */
    public static String defaultString(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    /**
     * <p>Returns either the passed in String, or if the String is
     * empty or <code>null</code>, the value of <code>defaultStr</code>.</p>
     *
     * <pre>
     * FunctionUtils.defaultIfEmpty(null, "NULL")  = "NULL"
     * FunctionUtils.defaultIfEmpty("", "NULL")    = "NULL"
     * FunctionUtils.defaultIfEmpty("bat", "NULL") = "bat"
     * FunctionUtils.defaultIfEmpty("", null)      = null
     * </pre>
     *
     * @see FunctionUtils#defaultString(String, String)
     * @param str  the String to check, may be null
     * @param defaultStr  the default String to return
     *  if the input is empty ("") or <code>null</code>, may be null
     * @return the passed in String, or the default
     */
    public static String defaultIfEmpty(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    /**
     * <p>Returns either the passed in String, or if the String is
     * empty or <code>null</code>, the value of <code>defaultStr</code>.</p>
     *
     * <pre>
     * FunctionUtils.defaultIfBlank(null, "NULL")  = "NULL"
     * FunctionUtils.defaultIfBlank("", "NULL")    = "NULL"
     * FunctionUtils.defaultIfBlank("bat", "NULL") = "bat"
     * FunctionUtils.defaultIfBlank("", null)      = null
     * FunctionUtils.defaultIfBlank("  ", "NULL")    = "NULL"
     * FunctionUtils.defaultIfBlank(" bat ", "NULL")    = " bat "
     * </pre>
     *
     * @see FunctionUtils#defaultString(String, String)
     * @param str  the String to check, may be null
     * @param defaultStr  the default String to return
     *  if the input is empty ("") or <code>null</code>, may be null
     * @return the passed in String, or the default
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }





    // startsWith
    //-----------------------------------------------------------------------


    /**
     * <p>Check if a String starts with a specified prefix.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.</p>
     *
     * <pre>
     * FunctionUtils.startsWith(null, null)      = true
     * FunctionUtils.startsWith(null, "abc")     = false
     * FunctionUtils.startsWith("abcdef", null)  = false
     * FunctionUtils.startsWith("abcdef", "abc") = true
     * FunctionUtils.startsWith("ABCDEF", "abc") = false
     * </pre>
     *
     * @see java.lang.String#startsWith(String)
     * @param str  the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case sensitive, or
     *  both <code>null</code>
     * @since 2.4
     */
    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    /**
     * <p>Case insensitive check if a String starts with a specified prefix.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case insensitive.</p>
     *
     * <pre>
     * FunctionUtils.startsWithIgnoreCase(null, null)      = true
     * FunctionUtils.startsWithIgnoreCase(null, "abc")     = false
     * FunctionUtils.startsWithIgnoreCase("abcdef", null)  = false
     * FunctionUtils.startsWithIgnoreCase("abcdef", "abc") = true
     * FunctionUtils.startsWithIgnoreCase("ABCDEF", "abc") = true
     * </pre>
     *
     * @see java.lang.String#startsWith(String)
     * @param str  the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case insensitive, or
     *  both <code>null</code>
     * @since 2.4
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    /**
     * <p>Check if a String starts with a specified prefix (optionally case insensitive).</p>
     *
     * @see java.lang.String#startsWith(String)
     * @param str  the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case
     *  (case insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or
     *  both <code>null</code>
     */
    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }
    
    /**
     * <p>Check if a String starts with any of an array of specified strings.</p>
     * 
     * <pre>
     * FunctionUtils.startsWithAny(null, null)      = false
     * FunctionUtils.startsWithAny(null, new String[] {"abc"})  = false
     * FunctionUtils.startsWithAny("abcxyz", null)     = false
     * FunctionUtils.startsWithAny("abcxyz", new String[] {""}) = false
     * FunctionUtils.startsWithAny("abcxyz", new String[] {"abc"}) = true
     * FunctionUtils.startsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
     * </pre>
     *
     * @see #startsWith(String, String)
     * @param string  the String to check, may be null
     * @param searchStrings the Strings to find, may be null or empty
     * @return <code>true</code> if the String starts with any of the the prefixes, case insensitive, or
     *  both <code>null</code>
     * @since 2.5
     */
    public static boolean startsWithAny(String string, String[] searchStrings) {
        if (isEmpty(string) || (searchStrings == null || searchStrings.length == 0)) {
            return false;
        }
        for (int i = 0; i < searchStrings.length; i++) {
            String searchString = searchStrings[i];
            if (FunctionUtils.startsWith(string, searchString)) {
                return true;
            }
        }
        return false;
    }





    // endsWith
    //-----------------------------------------------------------------------


    /**
     * <p>Check if a String ends with a specified suffix.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.</p>
     *
     * <pre>
     * FunctionUtils.endsWith(null, null)      = true
     * FunctionUtils.endsWith(null, "def")     = false
     * FunctionUtils.endsWith("abcdef", null)  = false
     * FunctionUtils.endsWith("abcdef", "def") = true
     * FunctionUtils.endsWith("ABCDEF", "def") = false
     * FunctionUtils.endsWith("ABCDEF", "cde") = false
     * </pre>
     *
     * @see java.lang.String#endsWith(String)
     * @param str  the String to check, may be null
     * @param suffix the suffix to find, may be null
     * @return <code>true</code> if the String ends with the suffix, case sensitive, or
     *  both <code>null</code>
     * @since 2.4
     */
    public static boolean endsWith(String str, String suffix) {
        return endsWith(str, suffix, false);
    }

    /**
     * <p>Case insensitive check if a String ends with a specified suffix.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case insensitive.</p>
     *
     * <pre>
     * FunctionUtils.endsWithIgnoreCase(null, null)      = true
     * FunctionUtils.endsWithIgnoreCase(null, "def")     = false
     * FunctionUtils.endsWithIgnoreCase("abcdef", null)  = false
     * FunctionUtils.endsWithIgnoreCase("abcdef", "def") = true
     * FunctionUtils.endsWithIgnoreCase("ABCDEF", "def") = true
     * FunctionUtils.endsWithIgnoreCase("ABCDEF", "cde") = false
     * </pre>
     *
     * @see java.lang.String#endsWith(String)
     * @param str  the String to check, may be null
     * @param suffix the suffix to find, may be null
     * @return <code>true</code> if the String ends with the suffix, case insensitive, or
     *  both <code>null</code>
     * @since 2.4
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, true);
    }

    /**
     * <p>Check if a String ends with a specified suffix (optionally case insensitive).</p>
     *
     * @see java.lang.String#endsWith(String)
     * @param str  the String to check, may be null
     * @param suffix the suffix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case
     *  (case insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or
     *  both <code>null</code>
     */
    private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
    }





    // ASCII checks
    //-----------------------------------------------------------------------


    public static boolean isAscii(char ch)
    {
        return ch < '\200';
    }

    public static boolean isAsciiPrintable(char ch)
    {
        return ch >= ' ' && ch < '\177';
    }

    public static boolean isAsciiControl(char ch)
    {
        return ch < ' ' || ch == '\177';
    }

    public static boolean isAsciiAlpha(char ch)
    {
        return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z';
    }

    public static boolean isAsciiAlphaUpper(char ch)
    {
        return ch >= 'A' && ch <= 'Z';
    }

    public static boolean isAsciiAlphaLower(char ch)
    {
        return ch >= 'a' && ch <= 'z';
    }

    public static boolean isAsciiNumeric(char ch)
    {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isAsciiAlphanumeric(char ch)
    {
        return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9';
    }





    // String validators
    //-----------------------------------------------------------------------


    /**
     * Checks if a string have an email valid format
     * @param str
     * @return
     */
    public static boolean isEmail(final String str)
    {
        return GenericValidator.isEmail(str);
    }


    /**
     * Checks if a string have an credit card valid format
     * @param str
     * @return
     */
    public static boolean isCreditCard(final String str)
    {
        return GenericValidator.isCreditCard(str);
    }





    // Token generators
    //-----------------------------------------------------------------------


    /**
     * Generate a random token with the given string length
     */
    public static String createRandomToken(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = length; i > 0; i -= 12) {
            int n = Math.min(12, Math.abs(i));
            String s = Long.toString(Math.round(Math.random() * Math.pow(36, n)), 36);
            //Pad left
            //Local
            sb.append(String.format("#1$#" + n + "s", s));
            //Integracion
            //sb.append(String.format("%1$#" + n + "s", s));
        }
        return sb.toString();
    }

    /**
     * Codifica el parametro para que sea compatible con los hash unicos de usuario
     * en el sistema de archivos
     * 
     * @param str normalmente el email del usuario
     * @return
     */
    public static String toAvatarBuilderUmd5(String str) {
    	final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
    	final String encoded = encoder.encodePassword(str, null);
    	return encoded;
    }

    /**
     * Codifica el parametro para que sea compatible 
     * con la codificacion de passwords de la aplicacion
     * 
     * @param str la password en formato raw
     * @return
     */
    public static String encodePassword(String rawPass)
    {
    	Md5PasswordEncoder encoder = new Md5PasswordEncoder();
    	String encoded = encoder.encodePassword(rawPass, null);
    	return encoded;
    }





}
