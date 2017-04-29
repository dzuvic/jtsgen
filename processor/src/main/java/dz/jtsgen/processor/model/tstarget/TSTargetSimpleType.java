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

/**
 *  represents a simple type direct conversion.
 */
final class TSTargetSimpleType implements TSTargetType{

    /**
     * the type the processor should look for (without any type params or nested types.
     */
    private  final String javaType;

    /**
     * the resulting target type, currently as String
     */
    private  final String tsTargetType;

    TSTargetSimpleType(String javaType, String tsTargetType) {
        this.javaType = javaType;
        this.tsTargetType = tsTargetType;
    }

    public String getJavaType() {
        return javaType;
    }

    /**
     * this toString is meant as an textual representation for the renderer
     */
    @Override
    public String toString() {
        return tsTargetType;
    }
}

