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

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TSTargetTypeTest {

    @Test
    void test_withTypeParams_default() {
        TSTargetTypeTestee testee = new TSTargetTypeTestee();
        assertSame(testee.withTypeParams(null), testee);
    }
}

class TSTargetTypeTestee  implements TSTargetType {

    @Override
    public String getJavaType() {
        return null;
    }

    @Override
    public ConversionCoverage conversionCoverage() {
        return null;
    }

    @Override
    public boolean isReferenceType() {
        return false;
    }

    @Override
    public List<String> typeParameters() {
        return null;
    }

    @Override
    public Map<String, TSTargetType> typeParameterTypes() {
        return null;
    }
}