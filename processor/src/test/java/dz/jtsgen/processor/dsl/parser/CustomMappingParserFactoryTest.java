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

package dz.jtsgen.processor.dsl.parser;

import dz.jtsgen.processor.dsl.CustomMappingParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomMappingParserFactoryTest {

    @Test
    // not a useful
    public void parser() {
        CustomMappingParserFactory x = new CustomMappingParserFactory();
        assertEquals(x,x);
    }

    @Test
    public void checknewInstance() {
        CustomMappingParser x1 = CustomMappingParserFactory.parser();
        CustomMappingParser x2 = CustomMappingParserFactory.parser();
        assertNotEquals(x1,x2);
    }
}