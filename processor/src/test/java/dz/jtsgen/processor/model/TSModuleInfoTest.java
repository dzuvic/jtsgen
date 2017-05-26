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

import dz.jtsgen.annotations.OutputType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TSModuleInfoTest {

    @Test
    public void tsmodule_equals() throws Exception {
        assertEquals(new TSModuleInfo("a","p"), new TSModuleInfo("a","p"));
        assertEquals(new TSModuleInfo("a","p").withModuleData("a","b","c","c","d","f", OutputType.DECLARED_NAMESPACE)
                , new TSModuleInfo("a","p").withModuleData("a","b","c","c","d","f", OutputType.DECLARED_NAMESPACE));

        assertNotEquals(new TSModuleInfo("a","p").withModuleData("a","b","c","c","d","f", OutputType.DECLARED_NAMESPACE)
                , new TSModuleInfo("a","p").withModuleData("a","b","c","c","d","f", OutputType.TS_MODULE_DECLARED_NAMESPACE));

    }
}