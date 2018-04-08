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

package dz.jtsgen.processor.mapper.name;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VerySimpleNameMapperTest {
    private NameMapper testee = new VerySimpleNameMapper();

    private Map<String,String> standardTest = ImmutableMap.<String,String>builder()
            .put("","")
            .put("a","a")
            .put("A","A")
            .put("AA","AA")
            .put("aA","aA")
            .build();

    @Test
    @DisplayName("Checking simple mappings with some standard names")
    void checkSimpleMapping() {
        standardTest.forEach(
                (x,y) -> assertEquals( testee.mapMemberName(x), y, "Expected '" + y +"' got '" + testee.mapMemberName(x) )
        );
    }

    @Test
    @DisplayName("Checking jackson mapping with null")
    void checkSimpleMappingWithNull() {
        assertNull(testee.mapMemberName(null));
    }
}