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

import static dz.jtsgen.processor.model.tstarget.TSTargetFactory.createTSTargetByMapping;

/**
 * fixed Mappings
 */
public final class TSTargets {
    public static final TSTargetType NONE = new TSTargetPrimitiveType("", "Never");
    public static final TSTargetType NULL = new TSTargetPrimitiveType("null","null");
    public static final TSTargetType ANY = createTSTargetByMapping("java.lang.Object:any").orElse(NONE);
    public static final TSTargetType VOID = createTSTargetByMapping("java.lang.Void:Void").orElse(NONE);

    public static final TSTargetType STRING = createTSTargetByMapping("java.lang.String -> string").orElse(NONE);
    public static final TSTargetType INTEGER = createTSTargetByMapping("java.lang.Integer -> number").orElse(NONE);
    public static final TSTargetType DOUBLE = createTSTargetByMapping("java.lang.Double -> number").orElse(NONE);
    public static final TSTargetType NUMBER = createTSTargetByMapping("java.lang.Number -> number").orElse(NONE);
    public static final TSTargetType LONG = createTSTargetByMapping("java.lang.Long -> number").orElse(NONE);
    public static final TSTargetType SHORT = createTSTargetByMapping("java.lang.Short -> number").orElse(NONE);
    public static final TSTargetType BOOLEAN =  createTSTargetByMapping("java.lang.Boolean -> boolean").orElse(NONE);

    public static final TSTargetType COLLECTION = createTSTargetByMapping("java.util.Collection<T> -> Array<T>").orElse(NONE);
}
