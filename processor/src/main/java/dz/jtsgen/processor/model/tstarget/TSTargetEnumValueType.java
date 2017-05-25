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

import dz.jtsgen.processor.model.ConversionCoverage;
import dz.jtsgen.processor.model.TSTargetType;

import java.util.*;

/**
 * This Target Type represents a single enum value, used only internally for enum values
 */
public class TSTargetEnumValueType implements TSTargetType {
    @Override
    public String getJavaType() {
        return "";
    }

    @Override
    public ConversionCoverage conversionCoverage() {
        return ConversionCoverage.DIRECT;
    }

    @Override
    public boolean isReferenceType() {
        return false;
    }

    @Override
    public List<String> typeParameters() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, TSTargetType> typeParameterTypes() {
        return new HashMap<>();
    }

    @Override
    public String toString() {
        return "";
    }
}
