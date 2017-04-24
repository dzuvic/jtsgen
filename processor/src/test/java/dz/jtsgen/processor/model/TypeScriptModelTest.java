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

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeScriptModelTest {
    @Test
    public void testAddOne() {
        TypeScriptModel tsm = TypeScriptModel.newModel();
        tsm.addTSTypes(singletonList(createTsType("a.B")));
        assertEquals(1,tsm.getTsTypes().size());
    }

    @Test
    public void testAddTwoDifferent() {
        TypeScriptModel tsm = TypeScriptModel.newModel();
        tsm.addTSTypes(singletonList(createTsType("a.B")));
        tsm.addTSTypes(singletonList(createTsType("a.A")));
        assertEquals(2,tsm.getTsTypes().size());
    }

    @Test
    public void testAddTwoSame() {
        TypeScriptModel tsm = TypeScriptModel.newModel();
        tsm.addTSTypes(singletonList(createTsType("a.B")));
        tsm.addTSTypes(singletonList(createTsType("a.B")));
        assertEquals(1,tsm.getTsTypes().size());
    }

    private TSType createTsType(final String canonicalName) {
        final TypeElement mockedTypeElement = mock(TypeElement.class);
        final Name mockedName = mock(Name.class);
        when(mockedName.toString()).thenReturn(canonicalName);
        when(mockedTypeElement.getQualifiedName()).thenReturn(mockedName);
        return new TSInterface(mockedTypeElement);

    }

}