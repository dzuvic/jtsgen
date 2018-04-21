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

class SnakeCaseNameMapperTest {

    private NameMapper testee = new SnakeCaseNameMapper();


    private Map<String,String> standardTest = ImmutableMap.<String,String>builder()
            .put("", "")
            .put("a", "a")
            .put("abc", "abc")
            .put("1", "1")
            .put("123", "123")
            .put("1a", "1a")
            .put("a1", "a1")
            .put("$", "$")
            .put("$a", "$a")
            .put("a$", "a$")
            .put("$_a", "$_a")
            .put("a$a", "a$a")
            .put("$A", "$_a")
            .put("$_A", "$_a")
            .put("_", "_")
            .put("__", "_")
            .put("___", "__")
            .put("A", "a")
            .put("A1", "a1")
            .put("1A", "1_a")
            .put("_a", "a")
            .put("_A", "a")
            .put("a_a", "a_a")
            .put("a_A", "a_a")
            .put("A_A", "a_a")
            .put("A_a", "a_a")
            .put("WWW", "www")
            .put("someUPPER", "some_upper")
            .put("someUPPERa", "some_uppera")
            .put("Some", "some")
            .put("_Some", "some")
            .put("_some", "some")
            .put("__some", "_some")
            .put("__Some", "_some")
            .put("___some", "__some")
            .put("___Some", "__some")
            .put("someName", "some_name")
            .put("some_name", "some_name")
            .put("some__name", "some__name")
            .put("SomeName", "some_name")
            .put("Some_Name", "some_name")
            .put("Some__Name", "some__name")
            .put("_some_name", "some_name")
            .put("_SomeName", "some_name")
            .put("_Some_Name", "some_name")
            .put("ALL_UPPER_CASE", "all_upper_case")
            .build();


    @Test
    @DisplayName("Checking snake case mapping with standard names")
    void checkSnakeMapping() {
        testee.mapMemberName("$_a");
        standardTest.forEach(
                (x,y) -> assertEquals( y, testee.mapMemberName(x), "For '"+x+"' Expected '" + y +"' got '" + testee.mapMemberName(x) )
        );
    }

    @Test
    @DisplayName("Checking jackson mapping with null")
    void checkJacksonMappingWithNull() {
        assertNull(testee.mapMemberName(null));
    }


}
