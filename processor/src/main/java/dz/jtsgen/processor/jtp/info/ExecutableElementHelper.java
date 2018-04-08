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

import dz.jtsgen.annotations.TSModule;
import org.immutables.value.Value;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ExecutableElementHelper {

    boolean isGetterOrSetter(ExecutableElement x);

    boolean isGetter(ExecutableElement x);

    /**
     *
     * @param methodName the method name to parse
     * @return the raw member name extracted by first matching prefixExpression
     */
    Optional<String> extractRawMemberName(String methodName);
}


/**
 * some helper functions
 */
@Value.Immutable
abstract class ExecutableElementHelperImpl implements ExecutableElementHelper {

    @Value.Default
    List<String> getterPrefixes() {
        return Arrays.asList(TSModule.GETTER_EXRPESSION, TSModule.IS_EXPRESSION);
    }

    @Value.Default
    List<String> setterPrefixes() {
        return Collections.singletonList(TSModule.SETTER_EXPRESSION);
    }

    @Value.Lazy
    List<Pattern> compiledSetterPrefixes() {
        return setterPrefixes().stream().map( Pattern::compile).collect(Collectors.toList());
    }

    @Value.Lazy
    List<Pattern> compiledGetterPrefixes() {
        return getterPrefixes().stream().map( Pattern::compile).collect(Collectors.toList());
    }

    @Override
    public  boolean isGetterOrSetter(ExecutableElement x) {
        return isGetter(x) || isSetter(x);
    }

    @Override
    public Optional<String> extractRawMemberName(String s) {
        return Stream.concat(compiledGetterPrefixes().stream(),compiledSetterPrefixes().stream())
                .map( x -> {
                    Matcher m = x.matcher(s);
                    return m.matches() && m.groupCount() >= 1 && m.group(1) != null ?
                            m.group(1)
                            : null;
                } )
                .filter(Objects::nonNull)
                .findFirst();

    }

    @Override
    public  boolean isGetter(ExecutableElement x) {
        final String simpleName = x != null && x.getSimpleName() != null ? x.getSimpleName().toString() : null;
        return isGetter(simpleName);
    }

    boolean isSetter(Element x) {
        return isSetter(x != null && x.getSimpleName() != null ? x.getSimpleName().toString() : null);
    }

    boolean isGetterOrSetter(String x) {
        return isGetter(x) || isSetter(x);
    }

    private  boolean isSetter(String name) {
        return name != null && matches(name, this.compiledSetterPrefixes());
    }

    private boolean matches(String name, List<Pattern> patternList) {
        return patternList.stream().anyMatch(
                x -> x.matcher(name).matches()
        );
    }

    private boolean isGetter(String name) {
        return name != null && matches(name, this.compiledGetterPrefixes());
    }
}
