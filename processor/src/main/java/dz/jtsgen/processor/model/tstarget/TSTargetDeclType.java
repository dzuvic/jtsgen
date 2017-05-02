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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *  represents a simple type direct conversion.
 */
final class TSTargetDeclType implements TSTargetType, TSTargetInternal {

    /**
     * the type the processor should look for (without any type params or nested types.
     */
    private  final String javaType;

    /**
     * the resulting target type, currently as String
     */
    private  final String tsTargetType;
    private final List<String> typeParameters;
    private  final Map<String,TSTargetType> typeParametersTypes;

    TSTargetDeclType(String javaType, String tsTargetType, List<String> typeParameters, Map<String, TSTargetType> typeParametersTypes) {
        this.javaType = javaType;
        this.tsTargetType = tsTargetType;
        this.typeParameters = typeParameters;
        this.typeParametersTypes = typeParametersTypes == null ? new HashMap<>() : typeParametersTypes;
    }

    public String getJavaType() {
        return javaType;
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

