/*
 * Copyright (c) 2018 Dragan Zuvic
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

package dz.jtsgen.processor.jtp.info;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import java.util.List;

public interface ExecutableElementHelper {

    boolean isGetterOrSetter(ExecutableElement x);

    boolean isGetter(ExecutableElement x);

    String nameFromMethod(String s);
}


/**
 * some helper functions
 */
final class ExecutableElementHelperImpl implements ExecutableElementHelper {

    private final List<String> getterPrefixes;
    private final List<String> setterPrefixes;


    ExecutableElementHelperImpl(List<String> getterPrefixes, List<String> setterPrefixes) {
        this.getterPrefixes = getterPrefixes;
        this.setterPrefixes = setterPrefixes;
    }

    @Override
    public  boolean isGetterOrSetter(ExecutableElement x) {
        return isGetter(x) || isSetter(x);
    }

    @Override
    public  String nameFromMethod(String s) {
        assert isGetterOrSetter(s);
        String nameWithoutGetSet = nameExtractor(s);
        return Character.toLowerCase(nameWithoutGetSet.charAt(0)) + nameWithoutGetSet.substring(1);
    }

    @Override
    public  boolean isGetter(ExecutableElement x) {
        final String simpleName = x != null && x.getSimpleName() != null ? x.getSimpleName().toString() : null;
        if (simpleName != null && TypeKind.BOOLEAN == x.getReturnType().getKind()) {
            return isBooleanGetter(simpleName);
        }
        return isGetter(simpleName);
    }

    boolean isSetter(Element x) {
        return isSetter(x != null && x.getSimpleName() != null ? x.getSimpleName().toString() : null);
    }
    /* ---- */

    boolean isGetterOrSetter(String x) {
        return isGetter(x) || isSetter(x) || isBooleanGetter(x);
    }

    private  boolean isSetter(String name) {
        return name != null && name.length() > 3 && name.startsWith("set");
    }

    private boolean isGetter(String name) {
        return name != null && name.length() > 3 && name.startsWith("get");
    }

    private boolean isBooleanGetter(String name) {
        return name != null && name.length() > 2 && name.startsWith("is");
    }

    private  String nameExtractor(String s) {
        if (s.startsWith("get")) {
            return s.replaceFirst("get", "");
        } else if (s.startsWith("is")) {
            return s.replaceFirst("is", "");
        } else {
            return s.replaceFirst("set", "");
        }
    }

}
