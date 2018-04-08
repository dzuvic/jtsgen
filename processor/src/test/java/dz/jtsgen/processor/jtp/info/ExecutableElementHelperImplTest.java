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

package dz.jtsgen.processor.jtp.info;

import org.junit.jupiter.api.Test;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExecutableElementHelperImplTest {

    private ExecutableElementHelperImpl testee = ExecutableElementHelperImplBuilder.builder().build();

    @Test
    void isGetterOrSetter() {
        assertFalse(testee.isGetterOrSetter((String) null));
        assertFalse(testee.isGetterOrSetter((ExecutableElement) null));
        assertFalse(testee.isGetterOrSetter(createDummy("set")));
        assertFalse(testee.isGetterOrSetter(createDummy("get")));
        assertFalse(testee.isGetterOrSetter(createBooleanDummy("is")));
        assertFalse(testee.isGetterOrSetter(createDummy("bla")));
        assertTrue(testee.isGetterOrSetter(createDummy("setA")));
        assertTrue(testee.isGetterOrSetter(createDummy("getA")));
        assertTrue(testee.isGetterOrSetter(createBooleanDummy("isA")));
    }

    @Test
    void isSetter_tests() {
        assertFalse(testee.isSetter(null));
        assertTrue(testee.isSetter(createDummy("setA")));
        assertTrue(testee.isSetter(createDummy("set0A")));
        assertTrue(testee.isSetter(createDummy("set0")));
        assertFalse(testee.isSetter(createDummy("set")));
        assertFalse(testee.isSetter(createDummy(null)));
        assertFalse(testee.isSetter(createDummySimpleNameIsNull()));

        // let the naming strategy decide
        assertTrue(testee.isSetter(createDummy("seta")));
    }

    @Test
    void isGetter_tests() {
        assertFalse(testee.isGetter((ExecutableElement) null));
        assertTrue(testee.isGetter(createDummy("getA")));
        assertTrue(testee.isGetter(createBooleanDummy("isA")));
        assertFalse(testee.isGetter(createDummy(null)));
        assertFalse(testee.isGetter(createDummySimpleNameIsNull()));
        assertFalse(testee.isGetter(createDummy("get")));
        assertFalse(testee.isGetter(createBooleanDummy("is")));
    }

    private ExecutableElement createDummy(final String simpleName) {
        final ExecutableElement mock = mock(ExecutableElement.class);
        final Name nameMock = mock(Name.class);
        final TypeMirror kindMock = mock(TypeMirror.class);
        when(nameMock.toString()).thenReturn(simpleName);
        when(mock.getSimpleName()).thenReturn(nameMock);
        when(mock.getReturnType()).thenReturn(kindMock);
        return mock;
    }

    private ExecutableElement createDummySimpleNameIsNull() {
        final ExecutableElement mock = mock(ExecutableElement.class);
        when(mock.getSimpleName()).thenReturn(null);
        return mock;
    }

    private ExecutableElement createBooleanDummy(final String simpleName) {
        final ExecutableElement mock = mock(ExecutableElement.class);
        final Name nameMock = mock(Name.class);
        final TypeMirror kindMock = mock(TypeMirror.class);
        when(nameMock.toString()).thenReturn(simpleName);
        when(mock.getSimpleName()).thenReturn(nameMock);
        when(mock.getReturnType()).thenReturn(kindMock);
        when(kindMock.getKind()).thenReturn(TypeKind.BOOLEAN);
        return mock;
    }

    @Test
    void extract_methodName_fromGetterSetter() {
        assertEquals("x", testee.nameFromMethod("setX"));
        assertEquals("x", testee.nameFromMethod("getX"));
        assertEquals("x", testee.nameFromMethod("isX"));
        assertEquals("x_b", testee.nameFromMethod("getX_b"));
        assertEquals("x_b", testee.nameFromMethod("isX_b"));
        assertEquals("x_b", testee.nameFromMethod("setX_b"));
        assertEquals("x_with_getter_setter", testee.nameFromMethod("setX_with_getter_setter"));
    }

}