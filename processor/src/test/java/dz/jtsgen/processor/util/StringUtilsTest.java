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

import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class StringUtilsTest {

    @Test
     public void tesrCarOfString() throws Exception {
         assertEquals("de", StringUtils.car("de.dz"));
         assertEquals("de", StringUtils.car("de"));
         assertEquals("", StringUtils.car(""));
         assertEquals("a", StringUtils.car("a.b.c.d"));
     }

    @Test
    public void testMessageWithNulls() throws Exception {
        assertEquals("", StringUtils.arrayFormat("", new Object[]{"a"}));
        assertEquals("", StringUtils.arrayFormat(null, new Object[]{"a"}));
        assertEquals("", StringUtils.arrayFormat("{}", new Object[]{""}));
        assertEquals("?", StringUtils.arrayFormat("{}", null));
        assertEquals("null", StringUtils.arrayFormat("{}", new Object[]{null, null}));
    }

    @Test
    public void testMessageWithSomeObjects() throws Exception {
        assertEquals("a", StringUtils.arrayFormat("{}", new Object[]{"a"}));
        assertEquals("1", StringUtils.arrayFormat("{}", new Object[]{1}));
        assertEquals("1?", StringUtils.arrayFormat("{}{}", new Object[]{1}));
        assertEquals("12", StringUtils.arrayFormat("{}{}", new Object[]{1,2}));
        assertEquals("1 2", StringUtils.arrayFormat("{} {}", new Object[]{1,2,3}));
        assertEquals("abc", StringUtils.arrayFormat("abc", new Object[]{1,2,3}));
    }

    @Test
    public void testMessageWithList() throws Exception {
        assertEquals("1,2,3", StringUtils.arrayFormat("{}", new Object[]{  Arrays.asList(1, 2, 3) } ));
    }

    @Test
    public void testRemoveTypeArgs() {
        assertEquals("",StringUtils.withoutTypeArgs(null));
        assertEquals("a",StringUtils.withoutTypeArgs("a<b>"));
        assertEquals("a",StringUtils.withoutTypeArgs("a<b<c>>"));
    }

    @Test
    public void qualifiedTests() throws Exception {
       assertEquals("",StringUtils.lastOf(""));
       assertEquals("d",StringUtils.lastOf("a.b.c.d"));
       assertEquals("d",StringUtils.lastOf("d"));

       assertEquals("",StringUtils.untill(""));
       assertEquals("a.b.c",StringUtils.untill("a.b.c.d"));
       assertEquals("",StringUtils.untill("d"));
    }

    @Test
    public void testDotToDash() throws Exception {
       assertEquals("",StringUtils.dotToDash(""));
       assertEquals("a-b",StringUtils.dotToDash("a.b"));
       assertEquals("a-b-c",StringUtils.dotToDash("a.b.c"));
    }

    @Test
    public void testCamelCaseToDash() {
        assertEquals("abc-abc",StringUtils.camelCaseToDash("abcAbc"));
    }

    @Test
    public void testDotToCamelUpperCase() {
        assertEquals("AbCd", StringUtils.dotToUpperCamelCase("ab.cd"));
        assertEquals("Ab", StringUtils.dotToUpperCamelCase("ab"));
        assertEquals("ABCD", StringUtils.dotToUpperCamelCase("a.b.c.d"));
    }

    @Test
    public void testCheckPackageFriendly() {
        assertTrue(StringUtils.isPackageFriendly("a.b"));
        assertTrue(StringUtils.isPackageFriendly("a"));
        assertTrue(StringUtils.isPackageFriendly("a.b.c"));
        assertFalse(StringUtils.isPackageFriendly("a-b.c"));
        assertFalse(StringUtils.isPackageFriendly("0a.b.c"));
    }
}