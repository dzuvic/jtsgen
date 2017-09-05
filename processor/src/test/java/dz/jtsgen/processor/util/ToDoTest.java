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

import org.junit.Test;

import static org.junit.Assert.*;

// just check no NPE occurs
public class ToDoTest {

    @Test(expected = dz.jtsgen.processor.util.NotImplented.class)
    public void test_todo1() throws Exception {
        ToDo.todo("null");
    }

    @Test(expected = dz.jtsgen.processor.util.NotImplented.class)
    public void test_todo0() throws Exception {
        ToDo.todo();
    }

    @Test(expected = dz.jtsgen.processor.util.NotImplented.class)
    public void test_todo2() throws Exception {
        ToDo.todo((String) null);
    }

    @Test(expected = dz.jtsgen.processor.util.NotImplented.class)
    public void test_todo3() throws Exception {
        ToDo.todo((String[]) null);
    }


}