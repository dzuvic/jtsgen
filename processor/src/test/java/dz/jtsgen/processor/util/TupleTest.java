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

package dz.jtsgen.processor.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {
    @Test
    void test_Tuple_equals() {
        Tuple same=new Tuple<>("a", "b");
        assertTrue(same.equals(same));
        assertFalse(same.equals(new Integer(0)));
        assertEquals(new Tuple<>("a", "b"), new Tuple<>("a", "b"));
        assertNotEquals(new Tuple<>("B", "b"), new Tuple<>("a", "b"));
        assertNotEquals(new Tuple<>("a", "B"), new Tuple<>("a", "b"));
    }

    @Test
    void tuple_hashCode() {
        assertEquals(new Tuple<>("a", "b").hashCode(),new Tuple<>("a", "b").hashCode());
        assertNotEquals(new Tuple<>("a", "b").hashCode(),new Tuple<>("a", "c").hashCode());
    }
}