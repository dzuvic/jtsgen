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

package dz.jtsgen.processor.model.tstarget;

import dz.jtsgen.processor.dsl.model.TypeMappingExpression;
import dz.jtsgen.processor.dsl.parser.CustomMappingParserFactory;
import dz.jtsgen.processor.model.ConversionCoverage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static dz.jtsgen.processor.model.ConversionCoverage.SUBTYPES;
import static org.junit.jupiter.api.Assertions.*;

class TSTargetDeclTypeTest {

    @Test
    @DisplayName("check mapping expression to be SUBTYPE for regular mapping in TSTargetDeclType")
    void conversionCoverage() {
        TypeMappingExpression expr = CustomMappingParserFactory.parser().parse("String->sting").orElse(null);
        TSTargetDeclType testee = new TSTargetDeclType(expr,new HashMap<>(),false);

        assertEquals(testee.conversionCoverage(),SUBTYPES);
    }

    @Test
    @DisplayName("IllegalArgumentException when creating TSTargetDeclType without mapping expression")
    void checkNullMappingFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> new TSTargetDeclType(null, new HashMap<>(),true));
    }
}