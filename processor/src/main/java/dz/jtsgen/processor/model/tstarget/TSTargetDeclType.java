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
import java.util.stream.Collectors;

/**
 *  represents a simple type direct conversion for declaration types
 */
final class TSTargetDeclType implements TSTargetType, TSTargetInternal {

    private  final String javaType;

    /**
     * the resulting target type, currently as String
     */
    private  final String tsTargetType;
    private final List<String> typeParameters;
    private  final Map<String,TSTargetType> typeParametersTypes;
    private final ConversionCoverage conversionCoverage;

    TSTargetDeclType(String javaType, String tsTargetType, List<String> typeParameters, Map<String, TSTargetType> typeParametersTypes, ConversionCoverage conversionCoverage) {
        this.javaType = javaType;
        this.tsTargetType = tsTargetType;
        this.typeParameters = typeParameters == null ? Collections.emptyList() : typeParameters;
        this.conversionCoverage = conversionCoverage == null ? ConversionCoverage.DIRECT : conversionCoverage;
        this.typeParametersTypes = typeParametersTypes == null ? new HashMap<>() : typeParametersTypes;
    }

    public String getJavaType() {
        return javaType;
    }

    @Override
    public ConversionCoverage conversionCoverage() {
        return this.conversionCoverage;
    }

    /**
     * this toString is meant as an textual representation for the renderer
     */
    @Override
    public String toString() {
        StringBuilder result=new StringBuilder();
        result.append(tsTargetType);
        if (typeParametersTypes.size() > 0) {
            result.append("<");
            result.append(
                    typeParameters.stream().map(x -> (typeParametersTypes.get(x) != null) ? typeParametersTypes.get(x).toString() : null)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(", ")));
            result.append(">");
        }
        return result.toString();
    }

    @Override
    public boolean isReferenceType() {
        return true;
    }

    @Override
    public List<String> typeParameters() {
        return this.typeParameters;   
    }

    @Override
    public Map<String, TSTargetType> typeParameterTypes() {
        return this.typeParametersTypes;
    }

    @Override
    public String tsTargetType() {
        return this.tsTargetType;
    }
}

