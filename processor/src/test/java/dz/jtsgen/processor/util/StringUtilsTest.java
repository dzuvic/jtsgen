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

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static dz.jtsgen.processor.util.StringUtils.*;
import static org.junit.jupiter.api.Assertions.*;


class StringUtilsTest {

    @Test
    void testCountMatches() {
        assertEquals(countMatches("a.b.c"),2);
        assertEquals(countMatches("a"),0);
        assertEquals(countMatches(""),0);
        assertEquals(countMatches(null),0);
    }

    @Test
    void tesrCarOfString() {
         assertEquals("de", car("de.dz"));
         assertEquals("de", car("de"));
         assertEquals("", car(""));
         assertEquals("a", car("a.b.c.d"));
     }

    @Test
    void testMessageWithNulls() {
        assertEquals("", arrayFormat("", new Object[]{"a"}));
        assertEquals("", arrayFormat(null, new Object[]{"a"}));
        assertEquals("", arrayFormat("{}", new Object[]{""}));
        assertEquals("?", arrayFormat("{}", null));
        assertEquals("null", arrayFormat("{}", new Object[]{null, null}));
    }

    @Test
    void testMessageWithSomeObjects() {
        assertEquals("a", arrayFormat("{}", new Object[]{"a"}));
        assertEquals("1", arrayFormat("{}", new Object[]{1}));
        assertEquals("1?", arrayFormat("{}{}", new Object[]{1}));
        assertEquals("12", arrayFormat("{}{}", new Object[]{1,2}));
        assertEquals("1 2", arrayFormat("{} {}", new Object[]{1,2,3}));
        assertEquals("abc", arrayFormat("abc", new Object[]{1,2,3}));
    }

    @Test
    void testMessageWithList() {
        assertEquals("1,2,3", arrayFormat("{}", new Object[]{  Arrays.asList(1, 2, 3) } ));
    }

    @Test
    void testRemoveTypeArgs() {
        assertEquals("",withoutTypeArgs(null));
        assertEquals("a",withoutTypeArgs("a<b>"));
        assertEquals("a",withoutTypeArgs("a<b<c>>"));
    }

    @Test
    void qualifiedTests() {
       assertEquals("",lastOf(""));
       assertEquals("d",lastOf("a.b.c.d"));
       assertEquals("d",lastOf("d"));

       assertEquals("",untill(""));
       assertEquals("a.b.c",untill("a.b.c.d"));
       assertEquals("",untill("d"));
    }

    @Test
    void lastOf_boundary_tests() {
        assertEquals("a,b",lastOf("a,b",(String[]) null));
        assertEquals("a,b",lastOf("a,b",""));
        assertEquals("b",lastOf("a,b",","));
        assertEquals("a,b",lastOf("a,b",(String) null));
        assertEquals("", lastOf("a."));
        assertEquals("a",lastOf(".a"));
        assertEquals("", lastOf(null));
    }

    @Test
    void separator_null_test() {
        assertEquals(".",separator());
    }


    @Test
    void testDotToDash() {
       assertEquals("",dotToDash(""));
       assertEquals("a-b",dotToDash("a.b"));
       assertEquals("a-b-c",dotToDash("a.b.c"));
    }

    @Test
    void testCamelCaseToDash() {
        assertEquals("abc-abc",camelCaseToDash("abcAbc"));
    }

    @Test
    void testCamelCaseToDash_Fail() {
        assertThrows(IllegalArgumentException.class,
                () -> camelCaseToDash(null)

        );
    }

    @Test
    void testdotDash_Fail() {
        assertThrows(IllegalArgumentException.class,
                () -> dotToDash(null)

                );
    }

    @Test
    void testdotToUpperCamelCase_Fail() {
        assertThrows(IllegalArgumentException.class,
                () -> dotToUpperCamelCase(null)

                );
    }


    @Test
    void isPackageFriendly_boundary_cases() {
        assertFalse(isPackageFriendly(null));
    }

    @Test
    void test_splitInTwo() {
        assertEquals(Optional.empty(),StringUtils.splitIntoTwo(null));
        assertEquals(Optional.empty(),StringUtils.splitIntoTwo(""));
        assertEquals(Optional.empty(),StringUtils.splitIntoTwo("->->"));
        assertEquals(Optional.of(new Tuple<>("a", "b")),StringUtils.splitIntoTwo("a->b"));
    }

    @Test
    void notEmptyTrimmed_boundary_cases() {
        assertEquals("",notEmptytrimmed(null));
    }

    @Test
    void testDotToCamelUpperCase() {
        assertEquals("AbCd", dotToUpperCamelCase("ab.cd"));
        assertEquals("Ab", dotToUpperCamelCase("ab"));
        assertEquals("ABCD", dotToUpperCamelCase("a.b.c.d"));
    }

    @Test
    void testCheckPackageFriendly() {
        assertTrue(isPackageFriendly("a.b"));
        assertTrue(isPackageFriendly("a"));
        assertTrue(isPackageFriendly("a.b.c"));
        assertFalse(isPackageFriendly("a-b.c"));
        assertFalse(isPackageFriendly("0a.b.c"));
    }
}