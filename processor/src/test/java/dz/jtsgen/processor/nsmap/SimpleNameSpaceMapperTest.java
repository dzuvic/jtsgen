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

package dz.jtsgen.processor.nsmap;

import dz.jtsgen.processor.model.NameSpaceMapping;
import dz.jtsgen.processor.model.NameSpaceMappingBuilder;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SimpleNameSpaceMapperTest {

    @Test
    public void checkDefaultConstructor() {
        SimpleNameSpaceMapper x1 = new SimpleNameSpaceMapper(null);
        assertEquals(x1, x1);
    }

    @Test(expected = AssertionError.class)
    public void checkNullAssertion() {
        SimpleNameSpaceMapper testee = new SimpleNameSpaceMapper(null);
        testee.mapNameSpace(null);

    }

    @Test
    public void mapNameSpace() {
        NameSpaceMapping oneNsMapping = NameSpaceMappingBuilder.of("a.b","");
        List<NameSpaceMapping> oneMapping = Collections.singletonList(oneNsMapping);
        
        SimpleNameSpaceMapper snsm = new SimpleNameSpaceMapper(oneMapping);
        assertEquals(snsm.mapNameSpace("a.b"),"");
        assertEquals(snsm.mapNameSpace("a.b.c"),"c");
        assertEquals(snsm.mapNameSpace("a.d"),"a.d");
        assertEquals(snsm.mapNameSpace(""),"");
    }

    @Test
    public void mapNameSpaceExact() {
        NameSpaceMapping oneNsMapping = NameSpaceMappingBuilder.of("a.b","").withExact(true);
        List<NameSpaceMapping> oneMapping = Collections.singletonList(oneNsMapping);

        SimpleNameSpaceMapper snsm = new SimpleNameSpaceMapper(oneMapping);
        assertEquals(snsm.mapNameSpace("a.b"),"");
        assertEquals(snsm.mapNameSpace("a.b.c"),"a.b.c");
        assertEquals(snsm.mapNameSpace("a.d"),"a.d");
        assertEquals(snsm.mapNameSpace(""),"");
    }

}