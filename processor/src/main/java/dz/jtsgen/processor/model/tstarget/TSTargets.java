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

package dz.jtsgen.processor.model.tstarget;

import dz.jtsgen.processor.model.TSTargetType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dz.jtsgen.processor.model.tstarget.TSTargetFactory.createTSTargetByDSL;

/**
 * fixed Mappings
 */
public final class TSTargets {
    public static final TSTargetType NONE = new TSTargetPrimitiveType("", "Never");
    public static final TSTargetType NULL = new TSTargetPrimitiveType("null","null");
    public static final TSTargetType ANY = createTSTargetByDSL(" any -> any").orElse(NONE);
    public static final TSTargetType VOID = createTSTargetByDSL("java.lang.Void -> void").orElse(NONE);

    //TODO check if this is correct
    public static final TSTargetType OBJECT = createTSTargetByDSL(" java.lang.Object -> Object").orElse(NONE);
    public static final TSTargetType STRING = createTSTargetByDSL("java.lang.String -> string").orElse(NONE);
    public static final TSTargetType CHARACTER = createTSTargetByDSL("java.lang.Character -> string").orElse(NONE);
    public static final TSTargetType NUMBER = createTSTargetByDSL("java.lang.Number -> number").orElse(NONE);
    public static final TSTargetType BOOLEAN =  createTSTargetByDSL("java.lang.Boolean -> boolean").orElse(NONE);

    public static final TSTargetType COLLECTION = createTSTargetByDSL("java.util.Collection<T> -> `T`[]").orElse(NONE);
    public static final TSTargetType MAPS = createTSTargetByDSL("java.util.Map<U,V> -> { [key: `U`]: `V`; }").orElse(NONE);

    public static List<TSTargetType> defaultDeclaredTypeConversion() {
        return Stream.of(STRING, CHARACTER, BOOLEAN, NUMBER, COLLECTION, MAPS, OBJECT).collect(Collectors.toList());
    }
}
