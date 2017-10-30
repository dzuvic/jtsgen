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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TSNameSpaceTest {
    @Test
    public void test_findOrCreate() {
        TSNameSpace testee = new TSNameSpace();
        TSNameSpace subNameSpace = testee.findOrCreate("de.dz.sample");
        assertEquals(subNameSpace.getName(),"sample");
        assertEquals(testee.getName(),"");
        assertEquals(testee.getChildren().entrySet().iterator().next().getValue().getName(),"de");
        assertEquals(testee.getChildren().entrySet().iterator().next().getValue().getChildren().entrySet().iterator().next().getValue().getName(),"dz");
    }

    @Test(expected = AssertionError.class)
    public void test_findOrCreateAssertion() {
        new TSNameSpace().findOrCreate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalSubnameSpace() {
        TSNameSpace testee = new TSNameSpace();
        testee.getSubNameSapce("");
    }
}