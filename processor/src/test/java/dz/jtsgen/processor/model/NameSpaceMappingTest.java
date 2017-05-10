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

package dz.jtsgen.processor.model; /**
 * Created by zuvic on 10.05.17.
 */

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class NameSpaceMappingTest {
    @Test
    public void testnamespaceEquals() throws Exception {

        assertEquals(
                new NameSpaceMapping("a.b", ""),
                new NameSpaceMapping("a.b", "")
        );

        assertNotEquals(
                new NameSpaceMapping("a.b", ""),
                new NameSpaceMapping("a.c", "")
        );

        assertEquals(
                Collections.singletonList(new NameSpaceMapping("a.b", "")),
                Collections.singletonList(new NameSpaceMapping("a.b", ""))
        );

    }
}