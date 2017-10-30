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

package dz.jtsgen.processor.model;

import java.util.List;
import java.util.Map;

/**
 *  A kind of target representation for the conversion a specific java type.
 */
public interface TSTargetType {

    /**
     * @return the type the processor should look for (without any type params or nested types.
     */
    String getJavaType();

    /*
     * @return the coverage of the type mapping
     */
    ConversionCoverage conversionCoverage();


    /**
     * @return this toString is meant as an textual representation for the renderer
     */
    @Override
    String toString();

    /**
     * @return returns true if target was mapped from a declaration type (reference type)
     *
      */
    boolean isReferenceType();

    /**
     * @return a list of type variables
     */
    List<String> typeParameters();

    /**
     * @return a map of type variables types
     */
    Map<String,TSTargetType> typeParameterTypes();

    /**
     * @param nsm some namespace mapper instance
     * @return an instance, that has the namespace mapped
     */
    default TSTargetType mapNameSpace(NameSpaceMapper nsm) {
        return this;
    }

    /**
     * @param typeParams the new type params
     * @return an instance, with changed type params
     */
    default TSTargetType withTypeParams(Map<String,TSTargetType> typeParams) {
        return this;
    }
}

