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
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Quite useless. At least it checks the contract.
 */
public class TSTargetEnumValueTypeTest {

    @Test
    public void checkTstargetEnumContract() throws Exception {
        TSTargetEnumValueType testee=new TSTargetEnumValueType();
        assertEquals(testee.conversionCoverage(), ConversionCoverage.DIRECT);
        assertEquals(testee.getJavaType(),"");
        assertEquals(testee.isReferenceType(), false);
        assertEquals(testee.toString(), "");
        assertEquals(testee.typeParameters(), Collections.emptyList());
        assertEquals(testee.typeParameterTypes(), new HashMap<>());
    }
}