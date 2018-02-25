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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TSTargetPrimitiveTypeTest {

    @Test
    @DisplayName("check some attributes of an primitive target type")
    void test_primitiveMapping_info() {
        TSTargetPrimitiveType testee = new TSTargetPrimitiveType("null","null");
        assertEquals(testee.conversionCoverage(), ConversionCoverage.DIRECT);
        assertEquals(testee.getJavaType(),"null");
        assertEquals(testee.toString(),"null");
        assertEquals(testee.tsTypeName(),"null");
        assertEquals(testee.typeParameters(), Collections.emptyList());
        assertEquals(testee.typeParameterTypes(), new HashMap<>());
        assertFalse(testee.isReferenceType());
        assertEquals(testee.tsTypeName(),"null");
    }

}