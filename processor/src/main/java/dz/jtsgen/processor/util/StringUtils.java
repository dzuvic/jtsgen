/*
 * Copyright 2017 Dragan Zuvic
 *
 * This file is part of jtsgen.
 *
 * jtsgen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * jtsgen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jtsgen.  If not, see http://www.gnu.org/licenses/
 *
 */
package dz.jtsgen.processor.util;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Some String utilities: Unfortunately no dependencies means reimplementing
 */
public final class StringUtils {

    private static final String PATTERN_STRING = "(\\{\\})";
    private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

    //  @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.8">JVM Spec 3.8</a>
    public static final Pattern PACKAGE_PATTERN = Pattern.compile("^[a-zA-Z_$][a-zA-Z0-9_$]*(\\.[a-z0-9A-Z_$]+)*$");

    /**
     * @param format simple format String with slf4j like '{}' as
     * @param items
     * @return the formatted string or an empty String if format string is null
     */
    public static String arrayFormat(String format, Object[] items) {
        if (format == null) return "";

        StringBuffer builder = new StringBuffer(format.length() + 50);
        Matcher m = PATTERN.matcher(format);

        int i = 0;
        while (m.find()) {
            m.appendReplacement(builder,
                    ((items != null && i < items.length)) ?
                            callToString(items[i])
                            : "?"
            );
            i += 1;
        }
        m.appendTail(builder);

        return builder.toString();
    }

    private static String callToString(Object item) {
        if (item == null) return "null";
        if (item instanceof Collection) {
            Collection<String> ofStrings = ((Collection<?>) item).stream()
                    .filter((x) -> x != null)
                    .map((x) -> x.toString())
                    .collect(Collectors.toList());
            return String.join(",", ofStrings);
        }
        return item.toString();
    }

    public static Optional<String> cdr(String name, String... sep) {
        String realSep = separator(sep);
        final int i = name.indexOf(realSep);
        if (i > 0) return Optional.of(name.substring(i + 1));
        else return Optional.empty();
    }

    public static String car(String name, String... sep) {
        String realSep = separator(sep);
        final int i = name.indexOf(realSep);
        if (i > 0) return name.substring(0, i - 1);
        else return name;
    }

    public static String lastOf(String qualifiedName, String... sep) {
        String realSep = separator(sep);
        int lastIndexOf = qualifiedName.lastIndexOf(realSep);
        if (lastIndexOf < qualifiedName.length()) return qualifiedName.substring(lastIndexOf + 1);
        return "";
    }

    public static String untill(String qualifiedName, String... sep) {
        String realSep = separator(sep);
        int lastIndexOf = qualifiedName.lastIndexOf(realSep);
        if (lastIndexOf > 0) return qualifiedName.substring(0, lastIndexOf);
        return "";
    }

    /**
     * @param input must not be null
     * @return upper camel case , e.g. ab.cd getsTo AbCd
     */
    public static String dotToUpperCamelCase(String input) throws IllegalArgumentException {
        if (input == null) throw new IllegalArgumentException("dotToDash argument must not be null");
        StringBuilder nameBuilder = new StringBuilder(input.length());
        boolean capitalize = true;

        for (char c : input.toCharArray())
            if (c == '.') {
                capitalize = true;
            } else if (capitalize) {
                nameBuilder.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                nameBuilder.append(Character.toLowerCase(c));
                capitalize = false;
            }

        return nameBuilder.toString();
    }

    /**
     * @param input mus not be null
     * @return dot notation to dash notation
     * @throws IllegalArgumentException if null
     */
    public static String dotToDash(String input) throws IllegalArgumentException {
        if (input == null) throw new IllegalArgumentException("dotToDash argument must not be null");
        return input.replaceAll("\\.", "-");
    }

    /**
     * @param input mus not be null
     * @return
     */
    public static String camelCaseToDash(String input) {
        if (input == null) throw new IllegalArgumentException("camelCaseToDash argument must not be null");
        return input.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
    }

    private static String separator(String... sep) {
        if (sep != null && sep.length > 1 && sep[0] != null) return sep[0];
        return ".";
    }

    public static boolean isPackageFriendly(String moduleName) {
        return moduleName!=null && PACKAGE_PATTERN.matcher(moduleName).matches();
    }
}
