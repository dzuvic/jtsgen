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

/**
 * some helper functions
 */
public class ExecutableElementHelper {


    private ExecutableElementHelper() {
        // no instance
    }

    public static boolean isGetterOrSetter(Element x) {
        return isGetter(x) || isSetter(x);
    }

    public static boolean isGetter(Element x) {
        return isGetter(x != null && x.getSimpleName() != null ? x.getSimpleName().toString() : null);
    }

    public static boolean isSetter(Element x) {
        return isSetter(x != null && x.getSimpleName() != null ? x.getSimpleName().toString() : null);
    }

    /* ---- */
    public static boolean isGetterOrSetter(String x) {
        return isGetter(x) || isSetter(x);
    }

    private static boolean isSetter(String name) {
        return name != null && name.length() > 3 && name.startsWith("set");
    }

    public static boolean isGetter(String name) {
        return name != null && name.length() > 3 && name.startsWith("get");
    }


    public static String nameFromMethod(String s) {
        assert isGetterOrSetter(s);
        String nameWithoutGetSet = s.startsWith("get") ? s.replaceFirst("get", "") : s.replaceFirst("set", "");
        return Character.toLowerCase(nameWithoutGetSet.charAt(0)) + nameWithoutGetSet.substring(1);
    }

}
