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


import dz.jtsgen.processor.model.rendering.TSTypeVisitor;
import org.junit.Test;

import javax.lang.model.element.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;

public class TSTypeTest {

    private static class Testee extends TSType {
        Testee(Element e, String namespace, String name) {
            super(e, namespace, name);
        }

        @Override
        public String getKeyword() {
            return "";
        }

        @Override
        public void accept(TSTypeVisitor visitor, int ident) {
            // not used
        }
    }

    @Test
    public void testTsType_currently_not_used() throws Exception {
        TSType testee = new Testee(null,"a","b");
        assertNull(testee.getDocumentString());
        assertEquals(testee.getSuperTypes(),new ArrayList<>());
        assertEquals(testee.getElement(), Optional.empty());

        // the toString is only used for logging, so the result does not matter
        assertNotNull(testee.toString());

        testee.addSuperTypes(Arrays.asList(testee, new Testee(null,"x","y")));
        assertNotNull(testee.toString());

        testee.addMembers(Collections.singleton(new TSMember("a",null,false)));
        assertNotNull(testee.toString());

    }

    @Test(expected = AssertionError.class)
    public void testNull_firstArg() throws Exception {
        TSType testee=new Testee(null,null,"b");
    }

    @Test(expected = AssertionError.class)
    public void testNull_secondArg() throws Exception {
        TSType testee=new Testee(null,"a",null);
    }
}