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

package dz.jtsgen.processor.helper;

import org.junit.jupiter.api.Test;

import javax.lang.model.type.DeclaredType;

import java.util.Optional;

import static dz.jtsgen.processor.helper.DeclTypeHelper.declaredTypeToTypeElement;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DeclTypeHelperTest {

    @Test
    void check_Absent_in_declaredTypeToTypeElement() {

        // following is not really useful
        DeclTypeHelper declTypeHelper = new DeclTypeHelper();

        DeclaredType mockedType =  mock(DeclaredType.class);
        assertEquals(declaredTypeToTypeElement(null), Optional.empty());
        assertEquals(declaredTypeToTypeElement(mockedType), Optional.empty());
    }
}