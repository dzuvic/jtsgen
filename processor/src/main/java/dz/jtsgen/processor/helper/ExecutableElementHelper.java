/*
 * Copyright (c) 2017 Dragan Zuvic
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

package dz.jtsgen.processor.helper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;

/**
 * some helper functions
 */
public final class ExecutableElementHelper {

    public static boolean isGetterOrSetter(ExecutableElement x) {
        return isGetter(x) || isSetter(x);
    }

    public static boolean isGetter(ExecutableElement x) {
        final String simpleName = x != null && x.getSimpleName() != null ? x.getSimpleName().toString() : null;
        if (simpleName != null && TypeKind.BOOLEAN == x.getReturnType().getKind()) {
            return isBooleanGetter(simpleName);
        }
        return isGetter(simpleName);
    }

    public static boolean isSetter(Element x) {
        return isSetter(x != null && x.getSimpleName() != null ? x.getSimpleName().toString() : null);
    }

    /* ---- */
    public static boolean isGetterOrSetter(String x) {
        return isGetter(x) || isSetter(x) || isBooleanGetter(x);
    }

    private static boolean isSetter(String name) {
        return name != null && name.length() > 3 && name.startsWith("set");
    }

    public static boolean isGetter(String name) {
        return name != null && name.length() > 3 && name.startsWith("get");
    }

    public static boolean isBooleanGetter(String name) {
        return name != null && name.length() > 2 && name.startsWith("is");
    }

    public static String nameFromMethod(String s) {
        assert isGetterOrSetter(s);
        String nameWithoutGetSet = nameExtractor(s);
        return Character.toLowerCase(nameWithoutGetSet.charAt(0)) + nameWithoutGetSet.substring(1);
    }

    private static String nameExtractor(String s) {
        if (s.startsWith("get")) {
            return s.replaceFirst("get", "");
        } else if (s.startsWith("is")) {
            return s.replaceFirst("is", "");
        } else {
            return s.replaceFirst("set", "");
        }
    }

}
