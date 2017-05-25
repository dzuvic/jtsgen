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

import java.util.Optional;

import static dz.jtsgen.processor.model.ConversionCoverage.DIRECT;
import static dz.jtsgen.processor.model.ConversionCoverage.SUBTYPES;
import static dz.jtsgen.processor.util.StringUtils.notEmptytrimmed;

/**
 * simple representation of parsed arrow expression
 */
final class TypeMappingExpression {
    private final String first;
    private final String second;
    private final ConversionCoverage conversionCoverage;

    private TypeMappingExpression(String first, String second, ConversionCoverage conversionCoverage) {
        this.first = first;
        this.second = second;
        this.conversionCoverage = conversionCoverage;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public ConversionCoverage getConversionCoverage() {
        return conversionCoverage;
    }

    /**
     *
     * @param mappingString a string that is with an arrow
     * @return a tuple of two strings
     */
    static Optional<TypeMappingExpression> splitIntoTwo(String mappingString) {
        if (mappingString == null || !mappingString.contains("->")) return Optional.empty();
        final ConversionCoverage conversionType = mappingString.contains(SUBTYPES.arrowLiteral()) ? SUBTYPES: DIRECT;
        String[] params = mappingString.split(conversionType.arrowLiteral());
        if (params.length != 2) {
            return Optional.empty();
        }
        return Optional.of(new TypeMappingExpression(notEmptytrimmed(params[0]), notEmptytrimmed(params[1]), conversionType));
    }
}
