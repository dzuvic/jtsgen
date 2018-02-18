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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TSModuleInfoTest {


    @Test
    void tsPackageFriendly() {
        assertThrows(IllegalArgumentException.class,
                () -> TSModuleInfoBuilder.builder().moduleName("-a").build()

        );
    }

    @Test
    void test_TSModuleINfo_withModuleData() {
        TSModuleInfo original=TSModuleInfoBuilder.builder().build();

        assertEquals(original, original.withModuleData(null,null,null,null,null,null,null));
        assertEquals(original, original.withModuleData(original.getModuleVersion(),null,null,null,null,null,null));
        assertEquals(original, original.withModuleData(null,original.getModuleDescription(),null,null,null,null,null));
        assertEquals(original, original.withModuleData(null,null,original.getModuleAuthor(),null,null,null,null));
        assertEquals(original, original.withModuleData(null,null,null,original.getModuleLicense(),null,null,null));
        assertEquals(original, original.withModuleData(null,null,null,null,original.getModuleAuthorUrl(),null,null));
        assertEquals(original, original.withModuleData(null,null,null,null,null,original.getModuleName(),null));
        assertEquals(original, original.withModuleData(null,null,null,null,null,null,original.getOutputType()));
    }
}