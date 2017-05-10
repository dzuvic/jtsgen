/*
 * Copyright 2017 Dragan Zuvic
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
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static dz.jtsgen.processor.util.StringUtils.*;
import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class StringUtilsTest {

    @Test
    public void testCountMatches() throws Exception {
        assertEquals(countMatches("a.b.c"),2);
        assertEquals(countMatches("a"),0);
        assertEquals(countMatches(""),0);
        assertEquals(countMatches(null),0);
    }

    @Test
     public void tesrCarOfString() throws Exception {
         assertEquals("de", car("de.dz"));
         assertEquals("de", car("de"));
         assertEquals("", car(""));
         assertEquals("a", car("a.b.c.d"));
     }

    @Test
    public void testMessageWithNulls() throws Exception {
        assertEquals("", arrayFormat("", new Object[]{"a"}));
        assertEquals("", arrayFormat(null, new Object[]{"a"}));
        assertEquals("", arrayFormat("{}", new Object[]{""}));
        assertEquals("?", arrayFormat("{}", null));
        assertEquals("null", arrayFormat("{}", new Object[]{null, null}));
    }

    @Test
    public void testMessageWithSomeObjects() throws Exception {
        assertEquals("a", arrayFormat("{}", new Object[]{"a"}));
        assertEquals("1", arrayFormat("{}", new Object[]{1}));
        assertEquals("1?", arrayFormat("{}{}", new Object[]{1}));
        assertEquals("12", arrayFormat("{}{}", new Object[]{1,2}));
        assertEquals("1 2", arrayFormat("{} {}", new Object[]{1,2,3}));
        assertEquals("abc", arrayFormat("abc", new Object[]{1,2,3}));
    }

    @Test
    public void testMessageWithList() throws Exception {
        assertEquals("1,2,3", arrayFormat("{}", new Object[]{  Arrays.asList(1, 2, 3) } ));
    }

    @Test
    public void testRemoveTypeArgs() {
        assertEquals("",withoutTypeArgs(null));
        assertEquals("a",withoutTypeArgs("a<b>"));
        assertEquals("a",withoutTypeArgs("a<b<c>>"));
    }

    @Test
    public void qualifiedTests() throws Exception {
       assertEquals("",lastOf(""));
       assertEquals("d",lastOf("a.b.c.d"));
       assertEquals("d",lastOf("d"));

       assertEquals("",untill(""));
       assertEquals("a.b.c",untill("a.b.c.d"));
       assertEquals("",untill("d"));
    }

    @Test
    public void testDotToDash() throws Exception {
       assertEquals("",dotToDash(""));
       assertEquals("a-b",dotToDash("a.b"));
       assertEquals("a-b-c",dotToDash("a.b.c"));
    }

    @Test
    public void testCamelCaseToDash() {
        assertEquals("abc-abc",camelCaseToDash("abcAbc"));
    }

    @Test
    public void testDotToCamelUpperCase() {
        assertEquals("AbCd", dotToUpperCamelCase("ab.cd"));
        assertEquals("Ab", dotToUpperCamelCase("ab"));
        assertEquals("ABCD", dotToUpperCamelCase("a.b.c.d"));
    }

    @Test
    public void testCheckPackageFriendly() {
        assertTrue(isPackageFriendly("a.b"));
        assertTrue(isPackageFriendly("a"));
        assertTrue(isPackageFriendly("a.b.c"));
        assertFalse(isPackageFriendly("a-b.c"));
        assertFalse(isPackageFriendly("0a.b.c"));
    }
}