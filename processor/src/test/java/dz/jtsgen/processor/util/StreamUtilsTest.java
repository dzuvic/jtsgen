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

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class StreamUtilsTest {
    @Test
    public void test_stream_zip() throws Exception {

        Map<Integer, Integer> testee = StreamUtils.zip(
                IntStream.range(0, 3).boxed(),
                IntStream.range(0, 3).boxed().map(x->2*x))
                .collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));
        checkSameMap(testee);
    }

    @Test
    public void test_stream_zip_second_loger() throws Exception {

        Map<Integer, Integer> testee = StreamUtils.zip(
                IntStream.range(0, 3).boxed(),
                IntStream.range(0, 4).boxed().map(x->2*x))
                .collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));
        checkSameMap(testee);
    }

    @Test
    public void test_stream_zip_first_loger() throws Exception {

        Map<Integer, Integer> testee = StreamUtils.zip(
                IntStream.range(0, 4).boxed(),
                IntStream.range(0, 3).boxed().map(x->2*x))
                .collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));
        checkSameMap(testee);
    }

    private void checkSameMap(Map<Integer, Integer> testee) {
        assertEquals(testee.size(),3);
        assertEquals(testee.get(0).intValue(),0);
        assertEquals(testee.get(1).intValue(),2);
        assertEquals(testee.get(2).intValue(),4);
    }


}