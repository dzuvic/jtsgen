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

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.Assert.*;

public class OneLineFormatterTest {
    @Test
    public void test_oneline_format() throws Exception {
        OneLineFormatter f = new OneLineFormatter();
        LogRecord x = new LogRecord(Level.INFO,"test");
        x.setThrown(new IllegalArgumentException());
        assertThat(f.format(x), CoreMatchers.containsString("exception: java.lang.IllegalArgumentException"));
    }

}