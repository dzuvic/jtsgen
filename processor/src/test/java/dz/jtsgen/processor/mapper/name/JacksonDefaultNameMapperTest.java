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

import static org.junit.jupiter.api.Assertions.*;

class JacksonDefaultNameMapperTest {
    private NameMapper testee = new JacksonDefaultNameMapper();

    private Map<String,String> standardTest = ImmutableMap.<String,String>builder()
            .put("","")
            .put("a","a")
            .put("A","a")
            .put("AA","aA")
            .put("aA","aA")
            .build();

    @Test
    @DisplayName("Checking jackson mapping with standard names")
    void checkJacksonMapping() {
        standardTest.forEach(
                (x,y) -> assertEquals( testee.mapMemberName(x), y, "Expected '" + y +"' got '" + testee.mapMemberName(x) )
        );
    }

    @Test
    @DisplayName("Checking jackson mapping with null")
    void checkJacksonMappingWithNull() {
        assertNull(testee.mapMemberName(null));
    }
}